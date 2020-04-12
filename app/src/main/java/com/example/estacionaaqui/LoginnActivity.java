package com.example.estacionaaqui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginnActivity extends AppCompatActivity {
    public void Registar_New(View view)
    {

        startActivity(new Intent(this, CreaActivity.class));

    }
    private EditText etCorreo, etContraseña;
    private Button nbtn;


    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginn);
        etCorreo= findViewById(R.id.username);
        etContraseña= findViewById(R.id.password);
            nbtn =findViewById(R.id.login);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        
            nbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                     String Correo = etCorreo.getText().toString();
                     String Contraseña = etContraseña.getText().toString();

                    if(!Correo.isEmpty() && !Contraseña.isEmpty())
                    {
                        mAuth.signInWithEmailAndPassword(Correo,Contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    String id =   mAuth.getCurrentUser().getUid();
                                    mDatabase.child("persona").child(id).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists())
                                            {
                                                String tipo = dataSnapshot.child("tipo").getValue().toString();
                                                if(tipo.equals("due")){
                                                    startActivity(new Intent(LoginnActivity.this,DuenoActivity.class));
                                                    finish();
                                                }
                                                else{
                                                   startActivity(new Intent(LoginnActivity.this,ClienteActivity.class));
                                                   finish();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    });


                                }
                                else
                                {
                                    Toast.makeText(LoginnActivity.this, "No se pudo iniciar seccion", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(LoginnActivity.this, "Complete los campos", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    }





}
