package com.example.estacionaaqui;

import android.content.Context;
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

import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FrEstacionamiento2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FrEstacionamiento2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FrEstacionamiento2 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    EditText editTextPrecio, editTextLargo, editTextAncho;
    Spinner spinnerTipo, spinnerUbicacion;
    Button buttonNext;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    String is_accion, is_id;

    public FrEstacionamiento2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FrEstacionamiento2.
     */
    // TODO: Rename and change types and number of parameters
    public static FrEstacionamiento2 newInstance(String param1, String param2) {
        FrEstacionamiento2 fragment = new FrEstacionamiento2();
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
        View view = inflater.inflate(R.layout.fragment_fr_estacionamiento2, container, false);

        // Se capturan los controles de cajas de texto
        editTextPrecio = view.findViewById(R.id.editTextPrecio);
        editTextLargo = view.findViewById(R.id.editTextLargo);
        editTextAncho = view.findViewById(R.id.editTextAncho);
        spinnerTipo = view.findViewById(R.id.spinnerTipo);
        spinnerUbicacion = view.findViewById(R.id.spinnerUbicacion);
        buttonNext = view.findViewById(R.id.buttonNext);

        //editTextPrecio.setText("0.00");
        //editTextLargo.setText("0.00");
        //editTextAncho.setText("0.00");

        // Llenado del combo de Tipo y Ubicacion
        final String[] tipos = new String[] {"", "Exterior", "Interior", "Aire Libre" };
        final String[] ubicaciones = new String[] {"", "Primer Piso", "Azotea", "Sótano" };

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, tipos);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adaptadorUbicaciones = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, ubicaciones);
        adaptadorUbicaciones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerTipo.setAdapter(adaptador);
        spinnerUbicacion.setAdapter(adaptadorUbicaciones);

        // Se inicializa Firebase
        inicializarFirebase();

        // Se leen los parámetros
        SharedPreferences prefs = getActivity().getSharedPreferences("ESTACIONAMIENTO", Context.MODE_PRIVATE);
        is_accion = prefs.getString("ACCION", "");

        if(is_accion.equals("M"))
        {
            cargarDatos();
        }

        // Boton Grabar
        buttonNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(grabar(v))
                {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.contenedor, new FrEstacionamiento3()).addToBackStack(null).commit();
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

    private void cargarDatos(){
        SharedPreferences prefs = getActivity().getSharedPreferences("ESTACIONAMIENTO", Context.MODE_PRIVATE);
        is_id = prefs.getString("ID", "");

        databaseReference.child("estacionamiento").orderByChild("id").equalTo(is_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    Estacionamiento p = objSnapshot.getValue(Estacionamiento.class);

                    editTextPrecio.setText(p.getPreciohora().toString());
                    editTextLargo.setText(p.getLargo().toString());
                    editTextAncho.setText(p.getAncho().toString());
                    spinnerTipo.setSelection(((ArrayAdapter)spinnerTipo.getAdapter()).getPosition(p.getTipo()));
                    spinnerUbicacion.setSelection(((ArrayAdapter)spinnerUbicacion.getAdapter()).getPosition(p.getUbicacion()));
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

        String ls_precio = editTextPrecio.getText().toString();
        String ls_largo = editTextLargo.getText().toString();
        String ls_ancho = editTextAncho.getText().toString();
        String ls_tipo = spinnerTipo.getSelectedItem().toString();
        String ls_ubicacion = spinnerUbicacion.getSelectedItem().toString();

        SharedPreferences prefs = getActivity().getSharedPreferences("ESTACIONAMIENTO", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("PRECIO", ls_precio);
        editor.putString("LARGO", ls_largo);
        editor.putString("ANCHO", ls_ancho);
        editor.putString("TIPO", ls_tipo);
        editor.putString("UBICACION", ls_ubicacion);
        editor.commit();

        Toast toast= Toast.makeText(getActivity().getApplicationContext(), "Datos grabados en el SharedPreferences", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();

        return true;
    }

    private Boolean validar() {
        Boolean lb_error = false;
        String ls_precio = editTextPrecio.getText().toString();
        String ls_largo = editTextLargo.getText().toString();
        String ls_ancho = editTextAncho.getText().toString();

        Double ldbl_precio = 0.0;

        if(ls_precio.equals(""))
        {
            editTextPrecio.setError("Requerido");
            lb_error = true;
        }
        else {
            ldbl_precio = Double.parseDouble(editTextPrecio.getText().toString());
        }
        if(ldbl_precio <= 0.0){
            editTextPrecio.setError("Requerido");
            lb_error = true;
        }

        if(ls_largo.equals(""))
        {
            editTextLargo.setError("Requerido");
            lb_error = true;
        }

        if(ls_ancho.equals(""))
        {
            editTextAncho.setError("Requerido");
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
