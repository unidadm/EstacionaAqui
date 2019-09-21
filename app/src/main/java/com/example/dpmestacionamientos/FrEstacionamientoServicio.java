package com.example.dpmestacionamientos;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FrEstacionamientoServicio.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FrEstacionamientoServicio#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FrEstacionamientoServicio extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    TextView textViewEstacionamiento;
    EditText editTextTarifa;
    Spinner spinnerTipo;
    Button buttonSave;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    String is_accion, is_id;

    ArrayList<String> ias_tipos = new ArrayList<String>();

    public FrEstacionamientoServicio() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FrEstacionamientoServicio.
     */
    // TODO: Rename and change types and number of parameters
    public static FrEstacionamientoServicio newInstance(String param1, String param2) {
        FrEstacionamientoServicio fragment = new FrEstacionamientoServicio();
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
        View view = inflater.inflate(R.layout.fragment_fr_estacionamiento_servicio, container, false);

        // Se capturan los controles de cajas de texto
        textViewEstacionamiento = view.findViewById(R.id.textViewEstacionamiento);
        editTextTarifa = view.findViewById(R.id.editTextTarifa);
        spinnerTipo = view.findViewById(R.id.spinnerTipo);
        buttonSave = view.findViewById(R.id.buttonSave);

        // Se inicializa Firebase
        inicializarFirebase();

        // Se llenan los combos
        llenarSpinners();

        // Se leen los parámetros
        SharedPreferences prefs = getActivity().getSharedPreferences("ESTACIONAMIENTOSERVICIO", Context.MODE_PRIVATE);
        is_accion = prefs.getString("ACCION", "");

        if(is_accion.equals("M"))
        {
            cargarDatos();
        }

        // Titulo del estacionamiento
        prefs = getActivity().getSharedPreferences("ESTACIONAMIENTO", Context.MODE_PRIVATE);
        String ls_nombre = prefs.getString("NAME", "");
        textViewEstacionamiento.setText(ls_nombre);

        // Boton Grabar
        buttonSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(grabar(v))
                {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.contenedor, new FrListaEstacServicios()).addToBackStack(null).commit();
                }
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
        // Llenado del combo de Servicios
        ias_tipos.add("");

        databaseReference.child("servicio").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    Servicio p = objSnapshot.getValue(Servicio.class);

                    ias_tipos.add(p.getTipo().toString());
                }

                ArrayAdapter<String> adaptadorTipos = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, ias_tipos);
                adaptadorTipos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinnerTipo.setAdapter(adaptadorTipos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void cargarDatos(){
        SharedPreferences prefs = getActivity().getSharedPreferences("ESTACIONAMIENTOSERVICIO", Context.MODE_PRIVATE);
        is_id = prefs.getString("ID", "");

        databaseReference.child("estacionamientoservicio").orderByChild("id").equalTo(is_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    EstacionamientoServicio p = objSnapshot.getValue(EstacionamientoServicio.class);

                    editTextTarifa.setText(p.getTarifa().toString());
                    spinnerTipo.setSelection(((ArrayAdapter)spinnerTipo.getAdapter()).getPosition(p.getDescripcion()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public Boolean grabar(View v){
        if(!validar())
        {
            return false;
        }

        // Se capturan los valores del SharedPreferences
        SharedPreferences prefs = getActivity().getSharedPreferences("ESTACIONAMIENTO", Context.MODE_PRIVATE);
        String ls_idestacionamiento = prefs.getString("ID", "");

        String ls_tipo = spinnerTipo.getSelectedItem().toString();
        Double ldbl_tarifa = Double.parseDouble(editTextTarifa.getText().toString());

        EstacionamientoServicio p = new EstacionamientoServicio();
        if(is_accion.equals("M"))
        {
            p.setId(is_id);
        }
        else {
            p.setId(UUID.randomUUID().toString());
        }
        p.setDescripcion(ls_tipo);
        p.setTarifa(ldbl_tarifa);
        p.setIdestacionamiento(ls_idestacionamiento);

        databaseReference.child("estacionamientoservicio").child(p.getId()).setValue(p);
        Toast.makeText(getActivity(), "Datos grabados", Toast.LENGTH_LONG).show();

        return true;
    }

    private Boolean validar() {
        Boolean lb_error = false;
        String ls_tipo = spinnerTipo.getSelectedItem().toString();
        String ls_tarifa = editTextTarifa.getText().toString();

        Double ldbl_tarifa = 0.0;

        if(ls_tipo.equals(""))
        {
            //spinnerDistrito.setEr;
            TextView errorText = (TextView)spinnerTipo.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Requerido");//changes the selected item text to this
            lb_error = true;
        }

        if(ls_tarifa.equals(""))
        {
            editTextTarifa.setError("Requerido");
            lb_error = true;
        }
        else {
            ldbl_tarifa = Double.parseDouble(editTextTarifa.getText().toString());
        }
        if(ldbl_tarifa <= 0.0){
            editTextTarifa.setError("Requerido");
            lb_error = true;
        }

        return !lb_error;
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
