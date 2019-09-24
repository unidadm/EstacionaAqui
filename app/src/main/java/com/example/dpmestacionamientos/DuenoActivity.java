package com.example.dpmestacionamientos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class DuenoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FrEstacionamiento1.OnFragmentInteractionListener,
        FrEstacionamiento2.OnFragmentInteractionListener,
        FrEstacionamiento3.OnFragmentInteractionListener,
        FrEstacionamiento4.OnFragmentInteractionListener,
        FrBusqueda.OnFragmentInteractionListener,
        FrListaEstacionamientos.OnFragmentInteractionListener,
        FrListaServicios.OnFragmentInteractionListener,
        FrServicio.OnFragmentInteractionListener,
        FrLlamarDueno.OnFragmentInteractionListener,
        FrBusquedaAlquileres.OnFragmentInteractionListener,
        FrListaAlquileres.OnFragmentInteractionListener,
        FrListaEstacServicios.OnFragmentInteractionListener,
        FrEstacionamientoServicio.OnFragmentInteractionListener,
        FrEditar_Perfil.OnFragmentInteractionListener{

    private FirebaseAuth mAuth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dueno);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //Se obtiene el usuario autenticado
        mAuth = FirebaseAuth.getInstance();
        String ls_userid =   mAuth.getCurrentUser().getUid();

        //Se abre la búsqueda
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contenedor, new FrBusqueda()).commit();
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
        getMenuInflater().inflate(R.menu.dueno, menu);
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
        } else if (id == R.id.nav_new_estacionamiento) {

            SharedPreferences prefs = this.getSharedPreferences("ESTACIONAMIENTO", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("ACCION", "N");
            editor.commit();

            fragmentManager.beginTransaction().replace(R.id.contenedor, new FrEstacionamiento1()).addToBackStack(null).commit();
        } else if (id == R.id.nav_busqueda) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new FrBusqueda()).addToBackStack(null).commit();
        } else if (id == R.id.nav_servicios) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new FrListaServicios()).addToBackStack(null).commit();
        } else if (id == R.id.nav_alquileres) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new FrBusquedaAlquileres()).addToBackStack(null).commit();
        } else if (id == R.id.nav_llamar) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new FrLlamarDueno()).addToBackStack(null).commit();
        } else if (id == R.id.editar_perfil) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new FrEditar_Perfil()).commit();
        } else if (id == R.id.CerrarSesion) {
            mAuth.signOut();
            startActivity(new Intent(DuenoActivity.this,LoginnActivity.class));
            finish();
        } else if (id == R.id.nav_share) {

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
