package com.mbarmed.dragbeer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserHandle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;

public class CreacionUsuario extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private TextView tvSaltar;
    private ImageView imageView;
    private EditText dateUser, username, emailCrear, biografia;
    private Button btnRegistrar, uploadPhoto;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private StorageReference mStorage;
    private FirebaseUser firebaseUser;
    private Uri uriImage;

    FirebaseUser user;
    DatePickerDialog.OnDateSetListener setListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creacion_usuario);

        //inicializar
        tvSaltar = (TextView) findViewById(R.id.tvSaltar);
        imageView = findViewById(R.id.imageView);
        dateUser = findViewById(R.id.dateUser);
        username = findViewById(R.id.username);
        emailCrear = findViewById(R.id.emailCrear);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        uploadPhoto = findViewById(R.id.uploadPhoto);
        biografia = findViewById(R.id.biografia);

        //Firebase
        mDatabase = FirebaseDatabase.getInstance("https://dragbeer-83331-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance("gs://dragbeer-83331.appspot.com").getReference("Profile pics");


        //autocomplete email with firebase authentication
        if (firebaseUser != null) {
            String correo = firebaseUser.getEmail();
            emailCrear.setText(correo);
        }

        //Profile picture
        Uri uri = firebaseUser.getPhotoUrl();

        Picasso.with(CreacionUsuario.this).load(uri).into(imageView);

        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFile();
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPhoto();
               // mandarDatos();
            }
        });


    }

    private void openFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            uriImage = data.getData();
            imageView.setImageURI(uriImage);

        }
    }

    //mandar datos a firebase
    private void mandarDatos() {

        String usuario = username.getText().toString();
        String bio = biografia.getText().toString();
        String age = dateUser.getText().toString();


        if (TextUtils.isEmpty(usuario)) {
            username.requestFocus();
            username.setError("Debe introducir una contraseña");

        } else if (TextUtils.isEmpty(age)) {
            dateUser.requestFocus();
            dateUser.setError("Pon tu edad");
        } else {

        }

    }

    //subir foto a firebase
    private void uploadPhoto() {

        if (uriImage == null) {
            StorageReference fileReference = mStorage.child(mAuth.getCurrentUser().getUid() + "." +
                    getFileExtension(uriImage));

            fileReference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uriImage) {
                            Uri downloaUri = uriImage;
                            firebaseUser = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(downloaUri).build();
                            firebaseUser.updateProfile(profileUpdates);
                            Toast.makeText(CreacionUsuario.this, "He llegado", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = this.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }


}

