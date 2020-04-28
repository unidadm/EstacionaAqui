package com.example.estacionaaqui;

import android.content.Context;
import android.graphics.Color;
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

import android.content.SharedPreferences;
import android.view.Gravity;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FrEstacionamiento1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FrEstacionamiento1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FrEstacionamiento1 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    EditText editTextName, editTextAddress, editTextMaps, editTextPhone;
    Spinner spinnerDistrito;
    Button buttonNext, buttonServicios;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    String is_accion, is_latitud, is_longitud;

    public FrEstacionamiento1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FrEstacionamiento1.
     */
    // TODO: Rename and change types and number of parameters
    public static FrEstacionamiento1 newInstance(String param1, String param2) {
        FrEstacionamiento1 fragment = new FrEstacionamiento1();
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
        View view = inflater.inflate(R.layout.fragment_fr_estacionamiento1, container, false);

        // Se capturan los controles de cajas de texto
        editTextName = view.findViewById(R.id.editTextName);
        editTextAddress = view.findViewById(R.id.editTextAddress);
        //editTextMaps = view.findViewById(R.id.editTextMaps);
        spinnerDistrito = view.findViewById(R.id.spinnerDistrito);
        editTextPhone = view.findViewById(R.id.editTextPhone);
        buttonNext = view.findViewById(R.id.buttonNext);
        buttonServicios = view.findViewById(R.id.buttonServicios);

        // Llenado del combo de Distrito
        final String[] distritos = new String[] {"", "Barranco", "La Molina", "La Victoria", "Lima", "San Miguel", "Surco" };

        ArrayAdapter<String> adaptadorDistritos = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, distritos);
        adaptadorDistritos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerDistrito.setAdapter(adaptadorDistritos);

        // Se inicializa Firebase
        inicializarFirebase();

        // Se leen los parámetros
        SharedPreferences prefs = getActivity().getSharedPreferences("ESTACIONAMIENTO", Context.MODE_PRIVATE);
        is_accion = prefs.getString("ACCION", "");
        is_latitud = "0.0";
        is_longitud = "0.0";

        if(is_accion.equals("M"))
        {
            cargarDatos();
        }
        else{
            buttonServicios.setVisibility(View.INVISIBLE);
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
                    fragmentManager.beginTransaction().replace(R.id.contenedor, new FrEstacionamiento4()).addToBackStack(null).commit();
                }
            }
        });

        // Botón Servicios Adicionales
        buttonServicios.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contenedor, new FrListaEstacServicios()).addToBackStack(null).commit();
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

                    editTextName.setText(p.getNombre());
                    editTextAddress.setText(p.getDireccion());
                    //editTextMaps.setText(p.getDirecciongooglemaps());
                    is_latitud = p.getLatitud().toString();
                    is_longitud = p.getLongitud().toString();
                    spinnerDistrito.setSelection(((ArrayAdapter)spinnerDistrito.getAdapter()).getPosition(p.getDistrito()));
                    editTextPhone.setText(p.getTelefono());

                    SharedPreferences prefs = getActivity().getSharedPreferences("ESTACIONAMIENTO", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("NAME", editTextName.getText().toString());
                    editor.commit();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public Boolean grabar(View v){
        // Se capturan los controles de cajas de texto
        // EditText editTextName = (EditText) getView().findViewById(R.id.editTextName);
        if(!validar())
        {
            return false;
        }

        String ls_name = editTextName.getText().toString();
        String ls_address = editTextAddress.getText().toString();
        String ls_maps = "";//editTextMaps.getText().toString();
        String ls_dist = spinnerDistrito.getSelectedItem().toString();
        String ls_phone = editTextPhone.getText().toString();

        SharedPreferences prefs = getActivity().getSharedPreferences("ESTACIONAMIENTO", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("NAME", ls_name);
        editor.putString("ADDRESS", ls_address);
        editor.putString("MAPS", ls_maps);
        editor.putString("LATITUD", is_latitud);
        editor.putString("LONGITUD", is_longitud);
        editor.putString("DIST", ls_dist);
        editor.putString("PHONE", ls_phone);
        editor.commit();

        Toast toast= Toast.makeText(getActivity().getApplicationContext(), "Datos grabados en el SharedPreferences", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();

        return true;
    }

    private Boolean validar() {
        Boolean lb_error = false;
        String ls_name = editTextName.getText().toString();
        String ls_address = editTextAddress.getText().toString();
        String ls_dist = spinnerDistrito.getSelectedItem().toString();
        String ls_phone = editTextPhone.getText().toString();

        if(ls_name.equals(""))
        {
            editTextName.setError("Requerido");
            lb_error = true;
        }

        if(ls_address.equals(""))
        {
            editTextAddress.setError("Requerido");
            lb_error = true;
        }

        if(ls_dist.equals(""))
        {
            //spinnerDistrito.setEr;
            TextView errorText = (TextView)spinnerDistrito.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Requerido");//changes the selected item text to this
            lb_error = true;
        }

        if(ls_phone.equals(""))
        {
            editTextPhone.setError("Requerido");
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
