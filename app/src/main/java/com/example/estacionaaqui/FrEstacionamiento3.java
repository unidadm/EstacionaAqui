package com.example.estacionaaqui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FrEstacionamiento3.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FrEstacionamiento3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FrEstacionamiento3 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    Button buttonSave, buttonFoto, buttonGaleria;
    ImageView imageEstacionamiento;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    String is_accion, is_id, is_rutaimagen;

    private FirebaseAuth mAuth ;

    private StorageReference mStorage;

    private static final int GALLERY_INTENT = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;

    private ProgressDialog mProgressDialog;

    public FrEstacionamiento3() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FrEstacionamiento3.
     */
    // TODO: Rename and change types and number of parameters
    public static FrEstacionamiento3 newInstance(String param1, String param2) {
        FrEstacionamiento3 fragment = new FrEstacionamiento3();
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
        View view = inflater.inflate(R.layout.fragment_fr_estacionamiento3, container, false);

        // Se capturan los controles de cajas de texto
        buttonSave = view.findViewById(R.id.buttonSave);
        buttonFoto = view.findViewById(R.id.buttonFoto);
        buttonGaleria = view.findViewById(R.id.buttonGaleria);
        imageEstacionamiento = view.findViewById(R.id.imageEstacionamiento);
        mProgressDialog = new ProgressDialog(getActivity());

        // Se inicializa la variable de imagen
        is_rutaimagen="";

        // Se inicializa Firebase
        inicializarFirebase();

        // Se leen los parámetros
        SharedPreferences prefs = getActivity().getSharedPreferences("ESTACIONAMIENTO", Context.MODE_PRIVATE);
        is_accion = prefs.getString("ACCION", "");

        if(is_accion.equals("M"))
        {
            cargarDatos();
        }

        // Boton Grabar
        buttonSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(grabar(v))
                {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.contenedor, new FrListaEstacionamientos()).addToBackStack(null).commit();
                }
            }
        });

        // Boton Subir de la Galería
        buttonGaleria.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                subirGaleria();
            }
        });

        // Boton Subir Foto
        buttonFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subirFoto();
            }
        });

        return view;
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(getActivity());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        mStorage = FirebaseStorage.getInstance().getReference();
    }

    private void cargarDatos(){
        SharedPreferences prefs = getActivity().getSharedPreferences("ESTACIONAMIENTO", Context.MODE_PRIVATE);
        is_id = prefs.getString("ID", "");

        databaseReference.child("estacionamiento").orderByChild("id").equalTo(is_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    Estacionamiento p = objSnapshot.getValue(Estacionamiento.class);

                    is_rutaimagen = p.getRutaimagen().toString();

                    if (is_rutaimagen != ""){
                        Glide.with(getActivity())
                                .load(is_rutaimagen)
                                .fitCenter()
                                .centerCrop()
                                .into(imageEstacionamiento);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void subirGaleria(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_INTENT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Imagen de la Galería
        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){

            mProgressDialog.setTitle("Subiendo...");
            mProgressDialog.setMessage("Subiendo imagen al Firebase");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();

            Uri uri = data.getData();

            StorageReference filePath = mStorage.child("imagenes").child(uri.getLastPathSegment());

            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            mProgressDialog.dismiss();
                            Uri descargarFoto = uri;

                            Glide.with(getActivity())
                            .load(descargarFoto)
                            .fitCenter()
                            .centerCrop()
                            .into(imageEstacionamiento);

                            is_rutaimagen = descargarFoto.toString();

                            Toast.makeText(getActivity(), "Se subió exitosamente la imagen", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

        //Foto de la cámara
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            mProgressDialog.setTitle("Subiendo...");
            mProgressDialog.setMessage("Subiendo foto al Firebase");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            String timeStamp = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(new Date());

            // Creamos una referencia a la carpeta y el nombre de la imagen donde se guardara
            StorageReference mountainImagesRef = mStorage.child("imagenes/"+timeStamp);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] datas = baos.toByteArray();

            // Empezamos con la subida a Firebase
            UploadTask uploadTask = mountainImagesRef.putBytes(datas);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(getActivity(),"Hubo un error",Toast.LENGTH_SHORT);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            mProgressDialog.dismiss();
                            Uri descargarFoto = uri;

                            Glide.with(getActivity())
                                    .load(descargarFoto)
                                    .fitCenter()
                                    .centerCrop()
                                    .into(imageEstacionamiento);

                            is_rutaimagen = descargarFoto.toString();

                            Toast.makeText(getActivity(), "Se subió exitosamente la foto", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    private void subirFoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public Boolean grabar(View v){
        //Se obtiene el usuario autenticado
        mAuth = FirebaseAuth.getInstance();
        String ls_userid =   mAuth.getCurrentUser().getUid();

        // Se capturan los valores del SharedPreferences
        SharedPreferences prefs = getActivity().getSharedPreferences("ESTACIONAMIENTO", Context.MODE_PRIVATE);
        String ls_name = prefs.getString("NAME", "");
        String ls_address = prefs.getString("ADDRESS", "");
        String ls_maps = prefs.getString("MAPS", "");
        String ls_dist = prefs.getString("DIST", "");
        String ls_phone = prefs.getString("PHONE", "");
        Double ldbl_precio = Double.parseDouble(prefs.getString("PRECIO", ""));
        Double ldbl_largo = Double.parseDouble(prefs.getString("LARGO", ""));
        Double ldbl_ancho = Double.parseDouble(prefs.getString("ANCHO", ""));
        String ls_tipo = prefs.getString("TIPO", "");
        String ls_ubicacion = prefs.getString("UBICACION", "");

        Estacionamiento p = new Estacionamiento();
        if(is_accion.equals("M"))
        {
            p.setId(is_id);
        }
        else {
            p.setId(UUID.randomUUID().toString());
        }
        p.setIdpersona(ls_userid);
        p.setNombre(ls_name);
        p.setDireccion(ls_address);
        p.setDirecciongooglemaps(ls_maps);
        p.setDistrito(ls_dist);
        p.setTelefono(ls_phone);
        p.setPreciohora(ldbl_precio);
        p.setLargo(ldbl_largo);
        p.setAncho(ldbl_ancho);
        p.setTipo(ls_tipo);
        p.setUbicacion(ls_ubicacion);
        p.setRutaimagen(is_rutaimagen);

        databaseReference.child("estacionamiento").child(p.getId()).setValue(p);
        Toast.makeText(getActivity(), "Datos grabados", Toast.LENGTH_LONG).show();

        ///////////////////////////////
        /*Alquiler q = new Alquiler();
        q.setId(UUID.randomUUID().toString());
        q.setFechainiciostring("23/09/2019");
        q.setFechainiciointeger(20190923);
        q.setFechafinstring("24/09/2019");
        q.setFechafininteger(20190924);
        databaseReference.child("alquiler").child(q.getId()).setValue(q);*/
        ///////////////////////////////

        return true;
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
