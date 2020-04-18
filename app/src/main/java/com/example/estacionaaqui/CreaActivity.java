package com.example.estacionaaqui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreaActivity extends AppCompatActivity {

    EditText nombre, apellido, correo, contra;
    CheckBox tipo1, tipo2;
    FirebaseDatabase database;
    DatabaseReference myRef;

    FirebaseAuth mAuth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crea);

        nombre = findViewById(R.id.etName);
        apellido = findViewById(R.id.etapellido);
        correo = findViewById(R.id.etCorreo);
        contra = findViewById(R.id.etContraseña);
        tipo1 = findViewById(R.id.ckbDueno);
        tipo2 = findViewById(R.id.ckbCliente);

        iniciofirebase();

        mAuth = FirebaseAuth.getInstance();
    }

    private void iniciofirebase() {
        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
    }

    public void Crear_New(View view) {
        final String nom = nombre.getText().toString();
        final String Ape = apellido.getText().toString();
        final String cor = correo.getText().toString();
        final String contrase = contra.getText().toString();
        final boolean t1 = tipo1.isChecked();
        final boolean t2 = tipo2.isChecked();
        if (nom.equals("")) {
            validacion();

        } else if (Ape.equals("")) {
            validacion();

        } else if (cor.equals("")) {
            validacion();
        } else if (contrase.equals("")) {
            validacion();
        } else if (t1 == false && t2 == false) {
            Toast.makeText(this, "Por favor, seleccione el tipo de usuario", Toast.LENGTH_LONG).show();
        } else {

            if (!cor.trim().matches(emailPattern)) {
                Toast.makeText(CreaActivity.this, "Por favor, ingrese un correo válido ", Toast.LENGTH_SHORT).show();
            } else if (contrase.length() < 6 || !isValidPassword(contrase)) {
                Toast.makeText(CreaActivity.this, "Por favor, ingrese una contraseña válida ", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.createUserWithEmailAndPassword(cor, contrase).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String id = mAuth.getCurrentUser().getUid();
                            Map<String, Object> map = new HashMap<>();

                            map.put("Nombre", nom);
                            map.put("Correo", cor);
                            map.put("Apellido", Ape);
                            map.put("Contrasenia", contrase);

                            if (t1 == true && t2 == true) {
                                map.put("tipo", "cd");
                            } else {
                                if (t1 == true) {
                                    map.put("tipo", "due");
                                }
                                if (t2 == true) {
                                    map.put("tipo", "cli");
                                }
                            }

                            myRef.child("persona").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task2) {
                                    if (task2.isSuccessful()) {
                                        if (t1 == true) {
                                            startActivity(new Intent(CreaActivity.this, DuenoActivity.class));
                                        }
                                        if (t2 == true) {
                                            startActivity(new Intent(CreaActivity.this, ClienteActivity.class));
                                        }
                                    } else {
                                        Toast.makeText(CreaActivity.this, "No se creo los datos ", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(CreaActivity.this, "No se pudo registrar este usuario ", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }

        }
    }

    private void validacion() {
        String nom = nombre.getText().toString();
        String Ape = apellido.getText().toString();
        String cor = correo.getText().toString();
        String contrase = contra.getText().toString();


        if (nom.equals("")) {
            nombre.setError("Por favor, ingrese su nombre");
        }
        if (Ape.equals("")) {
            apellido.setError("Por favor, ingrese su apellido");
        }
        if (cor.equals("")) {
            correo.setError("Por favor, ingrese su correo");
        }
        if (contrase.equals("")) {
            contra.setError("Por favor, ingrese una contraseña");
        }

    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }


}
