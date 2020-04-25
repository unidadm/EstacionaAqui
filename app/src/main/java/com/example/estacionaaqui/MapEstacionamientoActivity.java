package com.example.estacionaaqui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapEstacionamientoActivity extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private DatabaseReference mDatabase;
    private ArrayList<Marker> tmpRealTimeMarkers = new ArrayList<>();
    private ArrayList<Marker> realTimeMarkers = new ArrayList<>();
    String is_id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map_estacionamiento_activity, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();

        is_id = "";
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        //LatLng sydney = new LatLng(-34, 151);
        //googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        googleMap.getUiSettings().setZoomControlsEnabled(true);

        mDatabase.child("estacionamiento").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (Marker marker:realTimeMarkers){
                    marker.remove();
                }
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Estacionamiento estacionamiento = snapshot.getValue(Estacionamiento.class);
                    Double latitud = estacionamiento.getLatitud();
                    Double longitud = estacionamiento.getLongitud();

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLng(latitud,longitud));
                    markerOptions.title(estacionamiento.getNombre());
                    markerOptions.snippet("Precio por Hora: " + estacionamiento.getPreciohora().toString());

                    Marker marker = googleMap.addMarker(markerOptions);
                    marker.setTag(estacionamiento.getId());

                    tmpRealTimeMarkers.add(marker);

                }
                realTimeMarkers.clear();
                realTimeMarkers.addAll(tmpRealTimeMarkers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-12.04592, -77.030565), 15));

        //Click en el marcador
        googleMap.setOnMarkerClickListener(this);
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        String ls_lat, ls_lon, ls_id;

        ls_id = marker.getTag().toString();
        ls_lat = Double.toString(marker.getPosition().latitude);
        ls_lon = Double.toString(marker.getPosition().longitude);

        if (is_id.equals(ls_id)){

            SharedPreferences prefs = getActivity().getSharedPreferences("ESTACIONAMIENTO", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("ACCION", "M");
            editor.putString("ID", ls_id);
            editor.commit();

            Toast.makeText(getActivity(), "Selección:" + marker.getTitle(), Toast.LENGTH_LONG).show();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contenedor, new FrAlquiler()).addToBackStack(null).commit();

        } else {
            is_id = ls_id;
        }

        //Toast.makeText(getActivity().getApplicationContext(),   ls_lat + " - " + ls_lon, Toast.LENGTH_SHORT).show();

        return false;
    }
}
