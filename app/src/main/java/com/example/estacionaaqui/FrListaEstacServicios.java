package com.example.estacionaaqui;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
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
 * {@link FrListaEstacServicios.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FrListaEstacServicios#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FrListaEstacServicios extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    TextView textViewEstacionamiento;

    private List<EstacionamientoServicio> estacionamientoservicioList = new ArrayList<>();
    private RecyclerView recyclerView;
    private EstacionamientoServiciosAdapter mAdapter;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public FrListaEstacServicios() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FrListaEstacServicios.
     */
    // TODO: Rename and change types and number of parameters
    public static FrListaEstacServicios newInstance(String param1, String param2) {
        FrListaEstacServicios fragment = new FrListaEstacServicios();
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
        View view = inflater.inflate(R.layout.fragment_fr_lista_estac_servicios, container, false);

        // Se capturan los controles de cajas de texto
        textViewEstacionamiento = view.findViewById(R.id.textViewEstacionamiento);

        //LLenado del RecyclerView
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        mAdapter = new EstacionamientoServiciosAdapter(estacionamientoservicioList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        //Evento click del RecyclerView
        mAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences prefs = getActivity().getSharedPreferences("ESTACIONAMIENTOSERVICIO", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("ACCION", "M");
                editor.putString("ID", estacionamientoservicioList.get(recyclerView.getChildAdapterPosition(view)).getId());
                editor.commit();

                Toast.makeText(getActivity(), "Selección:" + estacionamientoservicioList.get(recyclerView.getChildAdapterPosition(view)).getDescripcion(), Toast.LENGTH_LONG).show();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contenedor, new FrEstacionamientoServicio()).addToBackStack(null).commit();
            }
        });

        recyclerView.setAdapter(mAdapter);

        // Se inicializa Firebase
        inicializarFirebase();

        listarDatos();
        //prepareEstacionamientoServicioData();

        //Botón Nuevo
        Button button = (Button) view.findViewById(R.id.buttonNew);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // do something
                SharedPreferences prefs = getActivity().getSharedPreferences("ESTACIONAMIENTOSERVICIO", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("ACCION", "N");
                editor.commit();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contenedor, new FrEstacionamientoServicio()).addToBackStack(null).commit();
            }
        });

        return view;
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(getActivity());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void listarDatos(){
        // Se capturan los valores del SharedPreferences
        SharedPreferences prefs = getActivity().getSharedPreferences("ESTACIONAMIENTO", Context.MODE_PRIVATE);
        String ls_id = prefs.getString("ID", "");
        String ls_nombre = prefs.getString("NAME", "");

        textViewEstacionamiento.setText(ls_nombre);

        databaseReference.child("estacionamientoservicio").orderByChild("idestacionamiento").equalTo(ls_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                estacionamientoservicioList.clear();
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    EstacionamientoServicio p = objSnapshot.getValue(EstacionamientoServicio.class);
                    estacionamientoservicioList.add(p);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void prepareEstacionamientoServicioData() {
        EstacionamientoServicio servicio = new EstacionamientoServicio("Lavado de Vehículo", 2.50);
        estacionamientoservicioList.add(servicio);

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
