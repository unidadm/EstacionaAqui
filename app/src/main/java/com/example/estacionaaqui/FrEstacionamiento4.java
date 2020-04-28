package com.example.estacionaaqui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FrEstacionamiento4.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FrEstacionamiento4#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FrEstacionamiento4 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    SupportMapFragment mapFragment;
    Button buttonNext;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    String is_accion, is_nombre, is_latitud, is_longitud;

    private FragmentActivity myContext;

    public FrEstacionamiento4() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FrEstacionamiento4.
     */
    // TODO: Rename and change types and number of parameters
    public static FrEstacionamiento4 newInstance(String param1, String param2) {
        FrEstacionamiento4 fragment = new FrEstacionamiento4();
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
        View view = inflater.inflate(R.layout.fragment_fr_estacionamiento4, container, false);

        // Se capturan los controles de cajas de texto
        buttonNext = view.findViewById(R.id.buttonNext);

        // Se inicializa Firebase
        inicializarFirebase();

        // Se leen los parámetros
        SharedPreferences prefs = getActivity().getSharedPreferences("ESTACIONAMIENTO", Context.MODE_PRIVATE);
        is_accion = prefs.getString("ACCION", "");
        is_nombre = prefs.getString("NAME", "");
        is_latitud = prefs.getString("LATITUD", "");
        is_longitud = prefs.getString("LONGITUD", "");

        //Se captura el mapa y se crean sus eventos
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback(){

            @Override
            public void onMapReady(final GoogleMap googleMap) {

                Double ldbl_latitud = Double.parseDouble(is_latitud);
                Double ldbl_longitud = Double.parseDouble(is_longitud);

                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.setTrafficEnabled(true);

                if(is_accion.equals("M"))
                {
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(ldbl_latitud, ldbl_longitud))
                            .title(is_nombre)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(ldbl_latitud, ldbl_longitud), 15));
                }

                //Click largo en el mapa
                googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

                    @Override
                    public void onMapLongClick(LatLng latLng) {

                        // Creating a marker
                        MarkerOptions markerOptions = new MarkerOptions();

                        // Setting the position for the marker
                        markerOptions.position(latLng);

                        // Setting the title for the marker.
                        // This will be displayed on taping the marker
                        markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                        // Color
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

                        // Clears the previously touched position
                        googleMap.clear();

                        // Animating to the touched position
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                        // Placing a marker on the touched position
                        googleMap.addMarker(markerOptions);

                        //Set Data
                        is_latitud = latLng.latitude + "";
                        is_longitud = latLng.longitude + "";
                    }
                });
            }
        });

        // Boton Grabar
        buttonNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(grabar(v))
                {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.contenedor, new FrEstacionamiento2()).addToBackStack(null).commit();
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

    public Boolean grabar(View v){
        if(!validar())
        {
            return false;
        }

        SharedPreferences prefs = getActivity().getSharedPreferences("ESTACIONAMIENTO", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("LATITUD", is_latitud);
        editor.putString("LONGITUD", is_longitud);
        editor.commit();

        Toast toast= Toast.makeText(getActivity().getApplicationContext(), "Datos grabados en el SharedPreferences", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();

        return true;
    }

    private Boolean validar(){
        Boolean lb_error = false;

        if(is_latitud.equals("0.0") || is_longitud.equals("0.0"))
        {
            Toast toast= Toast.makeText(getActivity().getApplicationContext(),   "Seleccione la ubicación del estacionamiento en el mapa" , Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
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
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
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
