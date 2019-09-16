package com.example.dpmestacionamientos;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.content.SharedPreferences;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.Toast;

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

    EditText editTextName, editTextAddress, editTextMaps, editTextDist, editTextPhone;

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
        editTextMaps = view.findViewById(R.id.editTextMaps);
        editTextDist = view.findViewById(R.id.editTextDist);
        editTextPhone = view.findViewById(R.id.editTextPhone);

        // Boton Grabar
        Button button = (Button) view.findViewById(R.id.buttonNext);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(grabar(v))
                {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.contenedor, new FrEstacionamiento2()).commit();
                }
            }
        });

        return view;
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

    public Boolean grabar(View v){
        // Se capturan los controles de cajas de texto
        // EditText editTextName = (EditText) getView().findViewById(R.id.editTextName);
        if(!validacion())
        {
            return false;
        }

        String ls_name = editTextName.getText().toString();
        String ls_address = editTextAddress.getText().toString();
        String ls_maps = editTextMaps.getText().toString();
        String ls_dist = editTextDist.getText().toString();
        String ls_phone = editTextPhone.getText().toString();

        SharedPreferences prefs = getActivity().getSharedPreferences("ESTACIONAMIENTO", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("NAME", ls_name);
        editor.putString("ADDRESS", ls_address);
        editor.putString("MAPS", ls_maps);
        editor.putString("DIST", ls_dist);
        editor.putString("PHONE", ls_phone);
        editor.commit();
        Toast toast= Toast.makeText(getActivity().getApplicationContext(), "Datos grabados en el SharedPreferences", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();

        return true;
    }

    private Boolean validacion() {
        Boolean lb_error = false;
        String ls_name = editTextName.getText().toString();
        String ls_address = editTextAddress.getText().toString();
        String ls_dist = editTextDist.getText().toString();
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
            editTextDist.setError("Requerido");
            lb_error = true;
        }

        if(ls_phone.equals(""))
        {
            editTextPhone.setError("Requerido");
            lb_error = true;
        }

        return !lb_error;
    }
}
