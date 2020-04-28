package com.example.estacionaaqui;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FrAlquiler.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FrAlquiler#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FrAlquiler extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    EditText editTextName, editTextDist, editTextFecha, editTextPhone, editTextAddress, editTextPrecio;
    Calendar myCalendar = Calendar.getInstance();

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private FirebaseAuth mAuth ;

    String is_accion, is_idpersonadueno, is_idestacionamiento, is_origen;

    public FrAlquiler() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FrAlquiler.
     */
    // TODO: Rename and change types and number of parameters
    public static FrAlquiler newInstance(String param1, String param2) {
        FrAlquiler fragment = new FrAlquiler();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fr_alquiler, container, false);

        // Se capturan los controles de cajas de texto
        editTextName = view.findViewById(R.id.editTextName);
        editTextDist = view.findViewById(R.id.editTextDist);
        editTextFecha = view.findViewById(R.id.editTextFecha);
        editTextPhone = view.findViewById(R.id.editTextPhone);
        editTextAddress = view.findViewById(R.id.editTextAddress);
        editTextPrecio = view.findViewById(R.id.editTextPrecio);

        // Se inicializa Firebase
        inicializarFirebase();

        // Se setean los eventos del calendario
        editTextFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // Se leen los parámetros
        SharedPreferences prefs = getActivity().getSharedPreferences("ESTACIONAMIENTO", Context.MODE_PRIVATE);
        is_accion = prefs.getString("ACCION", "");
        is_origen = prefs.getString("ORIGEN", "");

        if(is_accion.equals("M"))
        {
            cargarDatos();
        }

        // Boton Grabar
        Button button = (Button) view.findViewById(R.id.buttonSave);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(grabar(v))
                {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    if(is_origen.equals("LISTA"))
                    {
                        fragmentManager.beginTransaction().replace(R.id.contenedor, new FrCliListaEstacionamientos()).commit();
                    } else {
                        fragmentManager.beginTransaction().replace(R.id.contenedor, new MapEstacionamientoActivity()).commit();
                    }
                }
            }
        });

        // Boton Llamar
        Button buttoncall = (Button) view.findViewById(R.id.buttonCall);
        buttoncall.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+editTextPhone.getText().toString()));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(callIntent);
            }
        });

        return view;
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(getActivity());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void cargarDatos(){
        SharedPreferences prefs = getActivity().getSharedPreferences("ESTACIONAMIENTO", Context.MODE_PRIVATE);
        String ls_id = prefs.getString("ID", "");

        databaseReference.child("estacionamiento").orderByChild("id").equalTo(ls_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    Estacionamiento p = objSnapshot.getValue(Estacionamiento.class);

                    is_idpersonadueno = p.getIdpersona();
                    is_idestacionamiento = p.getId();
                    editTextName.setText(p.getNombre());
                    editTextDist.setText(p.getDistrito());
                    editTextPhone.setText(p.getTelefono());
                    editTextPrecio.setText(p.getPreciohora().toString());
                    editTextAddress.setText(p.getDireccion());

                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    String ls_date = df.format(Calendar.getInstance().getTime());

                    editTextFecha.setText(ls_date);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public Boolean grabar(View v){
        //Se obtiene el usuario autenticado
        mAuth = FirebaseAuth.getInstance();
        String ls_userid =   mAuth.getCurrentUser().getUid();

        String ls_fecha = editTextFecha.getText().toString();
        String ls_dia, ls_mes, ls_anno;
        Integer li_fecha;

        if (ls_fecha.equals("")){
            li_fecha = 0;
        }else{
            ls_dia = ls_fecha.substring(0, 2);
            ls_mes = ls_fecha.substring(3, 5);
            ls_anno = ls_fecha.substring(6, 10);

            li_fecha = (Integer.parseInt(ls_anno) * 10000) + (Integer.parseInt(ls_mes) * 100) + (Integer.parseInt(ls_dia));
        }

        Alquiler p = new Alquiler();
        p.setId(UUID.randomUUID().toString());
        p.setIdpersonadueno(is_idpersonadueno);
        p.setIdestacionamiento(is_idestacionamiento);
        p.setIdpersonacliente(ls_userid);
        p.setEstacionamiento(editTextName.getText().toString());
        p.setDistrito(editTextDist.getText().toString());
        p.setFechainiciostring(editTextFecha.getText().toString());
        p.setFechafinstring(editTextFecha.getText().toString());
        p.setFechainiciointeger(li_fecha);
        p.setFechafininteger(li_fecha);

        databaseReference.child("alquiler").child(p.getId()).setValue(p);
        Toast.makeText(getActivity(), "Datos grabados", Toast.LENGTH_LONG).show();

        return true;
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            actualizarInput();
        }

    };

    private void actualizarInput() {
        String formatoDeFecha = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha);

        editTextFecha.setText(sdf.format(myCalendar.getTime()));
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
