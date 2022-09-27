package com.mbarmed.dragbeer;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CreacionUsuario extends AppCompatActivity {

    private EditText dateUser;
    private EditText username;
    private EditText biografia;
    private FirebaseAuth mAuth;
    private ImageView fotoPerfil;
    Uri imageUri;
    StorageReference storage;
    FirebaseUser firebaseUser;
    String email;
    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creacion_usuario);

        //inicializarr
        TextView tvSaltar = (TextView) findViewById(R.id.tvSaltar);
        dateUser = findViewById(R.id.dateUser);
        fotoPerfil = findViewById(R.id.fotoPerfil);
        username = findViewById(R.id.username);
        EditText emailCrear = findViewById(R.id.emailCrear);
        Button btnRegistrar = findViewById(R.id.btnRegistrar);
        biografia = findViewById(R.id.biografia);

        //Firebase
        mDatabase = FirebaseDatabase.getInstance("https://dragbeer-83331-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        fotoPerfil.setImageDrawable(getResources().getDrawable(R.drawable.perfil));

        //autocomplete email with firebase authentication
        if (firebaseUser != null) {
            email = firebaseUser.getEmail();
            emailCrear.setText(email);
        }

        fotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });



        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mandarDatos(email);
                uploadImage();
            }
        });



    }

    //mandar datos a firebase
    private void mandarDatos(String email) {

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
            ReadWriteUserDetails user = new ReadWriteUserDetails(usuario, bio, age, email);
            mDatabase.child(firebaseUser.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(CreacionUsuario.this, "Funcionado ( borrame ) ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CreacionUsuario.this,Etiquetas.class);
                        // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        // startActivity(intent);
                        // finish();
                    }else{
                        Toast.makeText(CreacionUsuario.this, "Hubo un problema, porfavor intenta de nuevo", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }

    private void uploadImage() {

        storage = FirebaseStorage.getInstance("gs://dragbeer-83331.appspot.com/").getReference("FotosPerfil");

        storage.child(mAuth.getCurrentUser().getUid()).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(CreacionUsuario.this, "Se ha mandado", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreacionUsuario.this, "Erroreees", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && data != null && data.getData() != null) {

            imageUri = data.getData();

            fotoPerfil.setImageURI(imageUri);

        }
    }

}

