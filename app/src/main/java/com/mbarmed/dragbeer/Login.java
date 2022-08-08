package com.mbarmed.dragbeer;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaDrm;
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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.internal.api.FirebaseNoSignedInUserException;

import org.w3c.dom.Text;

public class Login extends AppCompatActivity {

    private EditText emailLogin, passwordLogin;
    private Button login;
    private TextView registerLogin;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        //inicializar
        login = findViewById(R.id.login);
        emailLogin = findViewById(R.id.emailLogin);
        passwordLogin = findViewById(R.id.passwordLogin);
        registerLogin = findViewById(R.id.registerLogin);

        mAuth = FirebaseAuth.getInstance();

        //metodos

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });


        registerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegister();
            }
        });


    }

    //firebase
    private void loginUser() {
        String email = emailLogin.getText().toString();
        String password = passwordLogin.getText().toString();

        if (TextUtils.isEmpty(email)) {
            emailLogin.setError("El correo no puede estar bacio");
            emailLogin.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            passwordLogin.setError("La contraseña no puede estar vacia");
            passwordLogin.requestFocus();
        } else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(Login.this, "Sesion iniciada", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Login.this, CreacionUsuario.class));
                    } else if (!task.isSuccessful()) {
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            Toast.makeText(Login.this, "Contraseña incorrecta", Toast.LENGTH_LONG).show();
                        } catch (FirebaseAuthInvalidUserException e) {
                            Toast.makeText(Login.this, "Cuenta no encontrada", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Log.e("TAG", e.getMessage());
                        }
                    }
                }
            });
        }
    }


    //metodos
    private void openRegister() {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(Login.this, MainActivity.class));
        }
    }


}