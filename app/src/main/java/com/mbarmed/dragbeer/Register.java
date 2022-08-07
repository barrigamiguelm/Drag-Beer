package com.mbarmed.dragbeer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private Button registerRegister;
    private EditText passwordRegister, confirmPasswordRegister, emailRegister;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //inicializar

        Button registerRegister = findViewById(R.id.registerRegister);
        EditText password = findViewById(R.id.passwordRegister);
        EditText confirmPassword = findViewById(R.id.confirmPasswordRegister);
        EditText email = findViewById(R.id.emailRegister);

        //firebase
        mDatabase = FirebaseDatabase.getInstance("https://dragbeer-83331-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        mAuth = FirebaseAuth.getInstance();


        //boton registro
        registerRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check(password,email,confirmPassword);
                authFirebase(email,password);
            }
        });


    }

    private void authFirebase(EditText email, EditText password) {
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())

                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("FUE BIEN", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            mDatabase.child("DatosUsuario").child("user_id").push().setValue(mAuth.getCurrentUser().getUid());

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("fueMal", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void check(EditText password, EditText email,EditText confirmPassword) {
        if(password.getText().toString().isEmpty() || confirmPassword.getText().toString().isEmpty() || email.getText().toString().isEmpty() ){
            Toast.makeText(this, "Por favor rellene todos los campos", Toast.LENGTH_SHORT).show();
        }else{
            if(password.getText().toString().equals(confirmPassword.getText().toString())){
                Toast.makeText(this, "Todo ha funcionado", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            }
    }

    ///Metodos

    
    //Firebase
   




    }

    private void openLogin() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        Toast.makeText(this, "Usuario creado correctamente", Toast.LENGTH_SHORT).show();
    }



}