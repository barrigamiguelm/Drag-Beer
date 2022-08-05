package com.mbarmed.dragbeer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private Button registerRegister;
    private EditText userRegister,passwordRegister,confirmPasswordRegister,emailRegister;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        //inicializar

        Button registerRegister = findViewById(R.id.registerRegister);
        EditText user = (EditText) findViewById(R.id.userRegister);
        EditText password = findViewById(R.id.passwordRegister);
        EditText confirmPassword = findViewById(R.id.confirmPasswordRegister);
        EditText email = findViewById(R.id.emailRegister);

        //firebase
        mDatabase = FirebaseDatabase.getInstance("https://dragbeer-83331-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

        //boton registro
        registerRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendInfoFirebase(user,password,email);
                //openLogin();
            }
        });


    }

    ///Metodos

    private void sendInfoFirebase(EditText user, EditText password, EditText email) {


        mDatabase.child("Usuarios").push().setValue(user.getText().toString());
        mDatabase.child("Usuarios").child("Datos Usuarios").child("Email").push().setValue(email.getText().toString());
        mDatabase.child("Usuarios").child("Datos Usuarios").child("Password").push().setValue(password.getText().toString());

    }

    private void openLogin() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}