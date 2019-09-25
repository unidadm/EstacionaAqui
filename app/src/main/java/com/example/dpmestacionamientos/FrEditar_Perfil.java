package com.example.dpmestacionamientos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FrEditar_Perfil.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FrEditar_Perfil#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FrEditar_Perfil extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    EditText etNombre, etApellido, etContraseña,etcorreo;
    Button btnEditar;
    RadioButton tipo1, tipo2;
    String id;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private StorageReference mStorage;

    public FrEditar_Perfil() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FrEditar_Perfil.
     */
    // TODO: Rename and change types and number of parameters
    public static FrEditar_Perfil newInstance(String param1, String param2) {
        FrEditar_Perfil fragment = new FrEditar_Perfil();
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


        View view = inflater.inflate(R.layout.fragment_fr_editar__perfil, container, false);
        btnEditar = view.findViewById(R.id.btn_Edit);
        etNombre = view.findViewById(R.id.et_NewName1);
        etApellido = view.findViewById(R.id.et_NewApe);
        etContraseña = view.findViewById(R.id.et_Newcontra);
        etcorreo = view.findViewById(R.id.etNewCorreo);
        tipo1 =view.findViewById(R.id.newDueño);
        tipo2 =view.findViewById(R.id.newCliente);
        inicializarFirebase();
        mAuth = FirebaseAuth.getInstance();
        id =  mAuth.getCurrentUser().getUid();



        btnEditar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String id = mAuth.getCurrentUser().getUid();
                final String Nom = etNombre.getText().toString();
                final  String Ape = etApellido.getText().toString();
                final String Contra = etContraseña.getText().toString();
                final String Corr = etcorreo.getText().toString();
                final boolean t1 = tipo1.isChecked();
                final boolean t2 = tipo2.isChecked();
                Map<String,Object> map = new HashMap<>();

                map.put("Nombre",Nom);
                map.put("Correo",Corr);
                map.put("Apellido",Ape);
                map.put("Contrasenia",Contra);

                if(t1)
                {
                    map.put("tipo","due");
                }
                if(t2)
                {
                    map.put("tipo","cli");
                }

                databaseReference.child("persona").child(id).setValue(map);

            }
        });





        return  view;
    }
    private void inicializarFirebase() {
        FirebaseApp.initializeApp(getActivity());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        mStorage = FirebaseStorage.getInstance().getReference();
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
