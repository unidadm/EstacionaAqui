package com.example.estacionaaqui;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FrListaAlquileres.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FrListaAlquileres#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FrListaAlquileres extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private List<Alquiler> alquilerList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AlquileresAdapter mAdapter;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    String is_distrito, is_estacionamiento;
    Integer ii_fechainicio, ii_fechafin;

    private FirebaseAuth mAuth ;

    public FrListaAlquileres() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FrListaAlquileres.
     */
    // TODO: Rename and change types and number of parameters
    public static FrListaAlquileres newInstance(String param1, String param2) {
        FrListaAlquileres fragment = new FrListaAlquileres();
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
        View view = inflater.inflate(R.layout.fragment_fr_lista_alquileres, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        mAdapter = new AlquileresAdapter(alquilerList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        recyclerView.setAdapter(mAdapter);

        // Se inicializa Firebase
        inicializarFirebase();

        listarDatos();
        //prepareAlquilerData();

        return view;
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(getActivity());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void listarDatos(){
        String ls_anno, ls_mes, ls_dia;

        // Se capturan los valores del SharedPreferences
        SharedPreferences prefs = getActivity().getSharedPreferences("FILTROS", Context.MODE_PRIVATE);
        String ls_fechainicio = prefs.getString("FECHAINICIO", "");
        String ls_fechafin = prefs.getString("FECHAFIN", "");
        is_distrito = prefs.getString("DISTRITO", "");
        is_estacionamiento = prefs.getString("ESTACIONAMIENTO", "");

        if (ls_fechainicio.equals("")){
            ii_fechainicio = 0;
        }else{
            ls_dia = ls_fechainicio.substring(0, 2);
            ls_mes = ls_fechainicio.substring(3, 5);
            ls_anno = ls_fechainicio.substring(6, 10);

            ii_fechainicio = (Integer.parseInt(ls_anno) * 10000) + (Integer.parseInt(ls_mes) * 100) + (Integer.parseInt(ls_dia));
        }

        if (ls_fechafin.equals("")){
            ii_fechafin = 0;
        }else{
            ls_dia = ls_fechafin.substring(0, 2);
            ls_mes = ls_fechafin.substring(3, 5);
            ls_anno = ls_fechafin.substring(6, 10);

            ii_fechafin = (Integer.parseInt(ls_anno) * 10000) + (Integer.parseInt(ls_mes) * 100) + (Integer.parseInt(ls_dia));
        }

        //Se obtiene el usuario autenticado
        mAuth = FirebaseAuth.getInstance();
        String ls_userid =   mAuth.getCurrentUser().getUid();

        databaseReference.child("alquiler").orderByChild("idpersonadueno").equalTo(ls_userid).addListenerForSingleValueEvent(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            String ls_distrito, ls_estacionamiento;
            Integer li_fechainicio;

            alquilerList.clear();
            for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                Alquiler p = objSnapshot.getValue(Alquiler.class);

                ls_estacionamiento = p.getEstacionamiento();
                ls_distrito = p.getDistrito();
                li_fechainicio = p.getFechainiciointeger();

                if (
                        (is_distrito.equals("") || is_distrito.equals(ls_distrito)) &&
                                (is_estacionamiento.equals("") || is_estacionamiento.equals(ls_estacionamiento)) &&
                                (ii_fechainicio.equals(0) || li_fechainicio >= ii_fechainicio) &&
                                (ii_fechafin.equals(0) || li_fechainicio <= ii_fechafin)

                ){
                    alquilerList.add(p);
                }

            }
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void prepareAlquilerData() {
        Alquiler alquiler = new Alquiler("Estacionamiento Don Pedrito", "10/08/2019 08:00 a.m.");
        alquilerList.add(alquiler);

        alquiler = new Alquiler("Estacionamiento Don Pedrito", "10/08/2019 10:00 a.m.");
        alquilerList.add(alquiler);

        alquiler = new Alquiler("Estacionamiento Don Pedrito", "10/08/2019 12:00 p.m.");
        alquilerList.add(alquiler);

        alquiler = new Alquiler("Estacionamiento Don Pedrito", "10/08/2019 03:00 p.m.");
        alquilerList.add(alquiler);

        alquiler = new Alquiler("Los Portalitos", "11/08/2019 09:00 a.m.");
        alquilerList.add(alquiler);

        alquiler = new Alquiler("Los Portalitos", "11/08/2019 11:00 a.m.");
        alquilerList.add(alquiler);

        mAdapter.notifyDataSetChanged();
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
