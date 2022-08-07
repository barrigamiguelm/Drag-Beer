package com.mbarmed.dragbeer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
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
        registerRegister = findViewById(R.id.registerRegister);
        passwordRegister = findViewById(R.id.passwordRegister);
        confirmPasswordRegister = findViewById(R.id.confirmPasswordRegister);
        emailRegister = findViewById(R.id.emailRegister);

        //firebase
        mDatabase = FirebaseDatabase.getInstance("https://dragbeer-83331-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        mAuth = FirebaseAuth.getInstance();


        //boton registro
        registerRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               registerRegister.setOnClickListener(view1 -> {
                   createUser();
               });

            }
        });


    }

    private void createUser() {
        String email = emailRegister.getText().toString();
        String password = passwordRegister.getText().toString();
        String repetircontraseña = confirmPasswordRegister.getText().toString();


        if(TextUtils.isEmpty(email)){
            emailRegister.setError("Debe introducir un correo");
            emailRegister.requestFocus();
        }else if(TextUtils.isEmpty(password)) {
            passwordRegister.setError("Debe introducir una contraseña");
            passwordRegister.requestFocus();
        }else if(TextUtils.isEmpty(repetircontraseña)) {
            confirmPasswordRegister.setError("Debe repetir su contraseña");
            confirmPasswordRegister.requestFocus();
        }else if (!password.equals(repetircontraseña)){
            confirmPasswordRegister.setError("Las contraseñas no coinciden");

        }else{
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(Register.this, "Creado correctamente", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Register.this,Login.class));
                    }else if (!task.isSuccessful()) {
                      //Toast.makeText(Register.this, "Hubo un error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        try {
                            throw task.getException();
                        } catch(FirebaseAuthUserCollisionException e) {
                            Toast.makeText(Register.this, "El correo ya esta en uso", Toast.LENGTH_SHORT).show();
                        }  catch(Exception e) {
                            Log.e("TAG", e.getMessage());
                        }
                    }
                }
            });
        }
    }
    //Firebase

    private void authFirebase(EditText email, EditText password) {
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("sesion", "createUserWithEmail:success");
                            Toast.makeText(Register.this, "Cuenta creada correctamente", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            openMain();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("sesion", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    /*private void check(EditText password, EditText email, EditText confirmPassword) {
        if (password.getText().toString().isEmpty() || confirmPassword.getText().toString().isEmpty() || email.getText().toString().isEmpty()) {
            Toast.makeText(this, "Por favor rellene todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            if (password.getText().toString().equals(confirmPassword.getText().toString())) {
            } else {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            }
        }

        }

*/


///Metodos



    private void openMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Usuario creado correctamente", Toast.LENGTH_SHORT).show();
    }


}