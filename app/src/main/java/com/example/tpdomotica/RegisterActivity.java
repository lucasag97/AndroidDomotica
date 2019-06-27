package com.example.tpdomotica;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class RegisterActivity extends AppCompatActivity {

    EditText nombre, apellido, username, dni, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nombre = (EditText) findViewById(R.id.form_nombre);
        apellido = (EditText) findViewById(R.id.form_apellido);
        username = (EditText) findViewById(R.id.form_username);
        dni = (EditText) findViewById(R.id.form_dni);
        password = (EditText) findViewById(R.id.form_pwd);
    }

    public void onClick(View view){

        registrarUsuario();
    }

    public void registrarUsuario() {
        ConexionSQLite conn = new ConexionSQLite(this, "db_domotica", null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Utilidades.USER_NOMBRE, nombre.getText().toString());
        values.put(Utilidades.USER_APELLIDO, apellido.getText().toString());
        values.put(Utilidades.USER_DNI, dni.getText().toString());
        values.put(Utilidades.USER_USERNAME, username.getText().toString());
        values.put(Utilidades.USER_ROL, "user");
        values.put(Utilidades.USER_PASSWORD, password.getText().toString());

        Long idResult = db.insert(Utilidades.TABLA_USUARIO,Utilidades.USER_ID, values);

        Toast.makeText(getApplicationContext(), "ID Registro: "+ idResult, Toast.LENGTH_SHORT).show();
        db.close();
    }
}