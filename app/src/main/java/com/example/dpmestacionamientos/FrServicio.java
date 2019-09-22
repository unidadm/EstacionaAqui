package com.example.dpmestacionamientos;

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
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FrServicio.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FrServicio#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FrServicio extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    EditText editTextTipo, editTextDescripcion;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    String is_accion, is_id;

    private FirebaseAuth mAuth ;

    public FrServicio() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FrServicio.
     */
    // TODO: Rename and change types and number of parameters
    public static FrServicio newInstance(String param1, String param2) {
        FrServicio fragment = new FrServicio();
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
        View view = inflater.inflate(R.layout.fragment_fr_servicio, container, false);

        // Se capturan los controles de cajas de texto
        editTextTipo = view.findViewById(R.id.editTextTipo);
        editTextDescripcion = view.findViewById(R.id.editTextDescripcion);

        // Se inicializa Firebase
        inicializarFirebase();

        // Se leen los parámetros
        SharedPreferences prefs = getActivity().getSharedPreferences("SERVICIO", Context.MODE_PRIVATE);
        is_accion = prefs.getString("ACCION", "");

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
                    fragmentManager.beginTransaction().replace(R.id.contenedor, new FrListaServicios()).addToBackStack(null).commit();
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
        SharedPreferences prefs = getActivity().getSharedPreferences("SERVICIO", Context.MODE_PRIVATE);
        is_id = prefs.getString("ID", "");

        databaseReference.child("servicio").orderByChild("id").equalTo(is_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    Servicio p = objSnapshot.getValue(Servicio.class);

                    editTextTipo.setText(p.getTipo());
                    editTextDescripcion.setText(p.getDescripcion());
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
        //Se obtiene el usuario autenticado
        mAuth = FirebaseAuth.getInstance();
        String ls_userid =   mAuth.getCurrentUser().getUid();

        String ls_tipo = editTextTipo.getText().toString();
        String ls_descripcion = editTextDescripcion.getText().toString();

        Servicio p = new Servicio();
        if(is_accion.equals("M"))
        {
            p.setId(is_id);
        }
        else {
            p.setId(UUID.randomUUID().toString());
        }
        p.setIdpersona(ls_userid);
        p.setTipo(ls_tipo);
        p.setDescripcion(ls_descripcion);

        databaseReference.child("servicio").child(p.getId()).setValue(p);
        Toast.makeText(getActivity(), "Datos grabados", Toast.LENGTH_LONG).show();

        return true;
    }

    private Boolean validar() {
        Boolean lb_error = false;
        String ls_tipo = editTextTipo.getText().toString();
        String ls_descripcion = editTextDescripcion.getText().toString();

        if(ls_tipo.equals(""))
        {
            editTextTipo.setError("Requerido");
            lb_error = true;
        }

        if(ls_descripcion.equals(""))
        {
            editTextDescripcion.setError("Requerido");
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
