package com.example.estacionaaqui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.view.Menu;

public class ClienteActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FrBusquedaAlquileres.OnFragmentInteractionListener,
        FrEditar_Perfil.OnFragmentInteractionListener,
        FrListaEstacionamientos.OnFragmentInteractionListener,
        FrLlamarDueno.OnFragmentInteractionListener,
        FrListaAlquileres.OnFragmentInteractionListener,
        FrCliBusqueda.OnFragmentInteractionListener,
        FrCliListaEstacionamientos.OnFragmentInteractionListener,
        FrAlquiler.OnFragmentInteractionListener
        {
        private FirebaseAuth mAuth ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance(); // objeto
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //Se abre la búsqueda
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contenedor, new FrCliBusqueda()).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cliente, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.editar_perfil) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new FrEditar_Perfil()).commit();
        }
        /*else if (id == R.id.Buscar_Estacionamiento) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new FrCliBusqueda()).commit();
        } else if (id == R.id.Lst_Estacionamiento) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new FrCliListaEstacionamientos()).commit();
        } else if (id == R.id.LammarDueno) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new FrLlamarDueno()).commit();
        } */
        else if (id == R.id.CerrarSeccion) {
                mAuth.signOut();
            startActivity(new Intent(ClienteActivity.this,LoginnActivity.class));
            finish();
        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
