package com.example.estacionaaqui;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback {

    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setTrafficEnabled(true);

        googleMap.addMarker(new MarkerOptions().position(new LatLng(-12.04592, -77.030565)).title("Centro de Lima").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(-12.044956, -77.029831))
                .title("Palacio de Gobierno"));

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(-12.046661, -77.029544))
                .title("Catedral"));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-12.04592, -77.030565), 15));
    }
}
