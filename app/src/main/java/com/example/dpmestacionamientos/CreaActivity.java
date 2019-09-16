package com.example.dpmestacionamientos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.dpmestacionamientos.data.model.LoggedInUser;
import com.example.dpmestacionamientos.data.model.Persona;
import com.example.dpmestacionamientos.ui.login.LoginActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class CreaActivity extends AppCompatActivity {

    EditText nombre, apellido, correo, contra;
    RadioButton tipo1, tipo2;
    FirebaseDatabase database;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crea);

        nombre = findViewById(R.id.etName);
        apellido= findViewById(R.id.etapellido);
        correo= findViewById(R.id.etCorreo);
        contra= findViewById(R.id.etContraseña);
        tipo1 =findViewById(R.id.rbDueño);
        tipo2 =findViewById(R.id.rbCliente);


    iniciofirebase();

    }

    private void iniciofirebase() {
        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
    }

    public void Crear_New(View view)
    {
        String nom = nombre.getText().toString();
        String Ape = apellido.getText().toString();
        String cor=correo.getText().toString();
        String contrase = contra.getText().toString();
        boolean t1 = tipo1.isChecked();
        boolean t2 = tipo2.isChecked();
        if(nom.equals(""))
        {
            validacion();

        }
        if (Ape.equals("")){
            validacion();

        }
        if (cor.equals("")){
            validacion();
        }
        if (contrase.equals("")){
            validacion();
        }
        if(t1==false || t2 ==false)
        {
            Toast.makeText(this,"Falta-Tipo",Toast.LENGTH_LONG).show();
        }
        if(t1==true && t2==true){
            Toast.makeText(this,"Solo-Tipo",Toast.LENGTH_LONG).show();

        }
        else {

            Persona P = new Persona();
            P.setId(UUID.randomUUID().toString());
            P.setNombre(nom);
            P.setCorreo(cor);
            P.setApellido(Ape);
            P.setContraseña(contrase);
            if(t1)
            {
                P.setTipo(t1);
            }
            if (t2)
            {
                P.setTipo(t2);
            }
            myRef.child("persona").child(P.getId()).setValue(P);
            Toast.makeText(this, "Registrado", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private void validacion() {
        String nom = nombre.getText().toString();
        String Ape = apellido.getText().toString();
        String cor=correo.getText().toString();
        String contrase = contra.getText().toString();


        if (nom.equals("")){
            nombre.setError("Required");
        }
        if (Ape.equals("")){
            apellido.setError("Required");
        }
        if (cor.equals("")){
            correo.setError("Required");
        }
        if (contrase.equals("")){
            contra.setError("Required");
        }

    }


}
