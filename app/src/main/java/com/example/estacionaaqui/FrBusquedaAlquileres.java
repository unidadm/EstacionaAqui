package com.example.estacionaaqui;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FrBusquedaAlquileres.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FrBusquedaAlquileres#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FrBusquedaAlquileres extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    EditText editTextDesde, editTextHasta, editTextGeneral;
    Spinner comboDistritos, comboEstacionamientos;
    Calendar myCalendar = Calendar.getInstance();

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private FirebaseAuth mAuth ;

    ArrayList<String> ias_estacionamientos = new ArrayList<String>();

    public FrBusquedaAlquileres() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FrBusquedaAlquileres.
     */
    // TODO: Rename and change types and number of parameters
    public static FrBusquedaAlquileres newInstance(String param1, String param2) {
        FrBusquedaAlquileres fragment = new FrBusquedaAlquileres();
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
        View view = inflater.inflate(R.layout.fragment_fr_busqueda_alquileres, container, false);

        // Se capturan los controles de cajas de texto
        editTextDesde = view.findViewById(R.id.editTextDesde);
        editTextHasta = view.findViewById(R.id.editTextHasta);
        comboDistritos = view.findViewById(R.id.spinnerDistrito);
        comboEstacionamientos = view.findViewById(R.id.spinnerEstacionamiento);

        // Se inicializa Firebase
        inicializarFirebase();

        // Se llenan los combos
        llenarSpinners();

        // Se setean los eventos del calendario
        editTextDesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextGeneral = editTextDesde;
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        editTextHasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextGeneral = editTextHasta;
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Botón Buscar
        Button button = (Button) view.findViewById(R.id.buttonBuscar);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // do something
                SharedPreferences prefs = getActivity().getSharedPreferences("FILTROS", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("FECHAINICIO", editTextDesde.getText().toString());
                editor.putString("FECHAFIN", editTextHasta.getText().toString());
                editor.putString("DISTRITO", comboDistritos.getSelectedItem().toString());
                editor.putString("ESTACIONAMIENTO", comboEstacionamientos.getSelectedItem().toString());
                editor.commit();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contenedor, new FrListaAlquileres()).addToBackStack(null).commit();
            }
        });

        return view;
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(getActivity());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void llenarSpinners(){
        //Se obtiene el usuario autenticado
        mAuth = FirebaseAuth.getInstance();
        String ls_userid =   mAuth.getCurrentUser().getUid();

        //Llenado del combo de distritos
        final String[] distritos = new String[] {"", "Barranco", "La Molina", "La Victoria", "Lima", "San Miguel", "Surco" };

        ArrayAdapter<String> adaptadorDistritos = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, distritos);
        adaptadorDistritos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        comboDistritos.setAdapter(adaptadorDistritos);


        //Llenado del combo de estacionamientos
        ias_estacionamientos.clear();
        ias_estacionamientos.add("");

        databaseReference.child("estacionamiento").orderByChild("idpersona").equalTo(ls_userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    Estacionamiento p = objSnapshot.getValue(Estacionamiento.class);

                    ias_estacionamientos.add(p.getNombre().toString());
                }

                ArrayAdapter<String> adaptadorEstacionamientos = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, ias_estacionamientos);
                adaptadorEstacionamientos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                comboEstacionamientos.setAdapter(adaptadorEstacionamientos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

        editTextGeneral.setText(sdf.format(myCalendar.getTime()));
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
