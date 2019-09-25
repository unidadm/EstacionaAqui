package com.example.dpmestacionamientos;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FrCliBusqueda.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FrCliBusqueda#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FrCliBusqueda extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    Spinner comboTipos, comboDistritos, comboUbicaciones;

    public FrCliBusqueda() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FrCliBusqueda.
     */
    // TODO: Rename and change types and number of parameters
    public static FrCliBusqueda newInstance(String param1, String param2) {
        FrCliBusqueda fragment = new FrCliBusqueda();
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
        View view = inflater.inflate(R.layout.fragment_fr_cli_busqueda, container, false);

        final String[] tipos = new String[] {"", "Exterior", "Interior", "Aire Libre" };
        final String[] distritos = new String[] {"", "Barranco", "La Molina", "La Victoria", "Lima", "San Miguel", "Surco" };
        final String[] ubicaciones = new String[] {"", "Primer Piso", "Azotea", "Sótano" };

        ArrayAdapter<String> adaptadorTipos = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, tipos);
        adaptadorTipos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adaptadorDistritos = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, distritos);
        adaptadorDistritos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adaptadorUbicaciones = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, ubicaciones);
        adaptadorUbicaciones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        comboTipos = (Spinner) view.findViewById(R.id.spinnerTipo);
        comboTipos.setAdapter(adaptadorTipos);

        comboDistritos = (Spinner) view.findViewById(R.id.spinnerDistrito);
        comboDistritos.setAdapter(adaptadorDistritos);

        comboUbicaciones = (Spinner) view.findViewById(R.id.spinnerUbicacion);
        comboUbicaciones.setAdapter(adaptadorUbicaciones);

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
                editor.putString("TIPO", comboTipos.getSelectedItem().toString());
                editor.putString("DISTRITO", comboDistritos.getSelectedItem().toString());
                editor.putString("UBICACION", comboUbicaciones.getSelectedItem().toString());
                editor.commit();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contenedor, new FrCliListaEstacionamientos()).addToBackStack(null).commit();
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
}
