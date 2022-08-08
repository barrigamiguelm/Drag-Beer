package com.mbarmed.dragbeer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreacionUsuario extends AppCompatActivity {

    private Spinner sexo;
    private TextView tvSaltar;
    private EditText dateUser, username, emailCrear,biografia;
    private Calendar calendario;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creacion_usuario);

        //inicializar
        tvSaltar = (TextView) findViewById(R.id.tvSaltar);
        Spinner sexo = (Spinner) findViewById(R.id.sexoSpinner);
        dateUser = findViewById(R.id.dateUser);
        username = findViewById(R.id.username);


        calendario = Calendar.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://dragbeer-83331-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();


        //seleccionar sexo
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sexo, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexo.setAdapter(adapter);




        //calendario fecha nacimiento
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                calendario.set(Calendar.YEAR, i);
                calendario.set(Calendar.MONTH, i1);
                calendario.set(Calendar.DAY_OF_MONTH, i2);
                actualizarCalendario();
            }

            private void actualizarCalendario() {
                String formato = "dd/MM/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(formato, Locale.ENGLISH);
                dateUser.setText(sdf.format(calendario.getTime()));

            }
        };

        dateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(CreacionUsuario.this, date, calendario.get(Calendar.YEAR), calendario.get(Calendar.MONTH), calendario.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    //seleccionar sexo
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        parent.getItemAtPosition(pos);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
        Toast.makeText(this, "Porfavor elige un sexo", Toast.LENGTH_SHORT).show();
    }

    //mandar a firebase
    String uid = user.getUid();
    String usuario  = username.getText().toString();
    String correo = user.getEmail();
    String bio = biografia.getText().toString();








}

