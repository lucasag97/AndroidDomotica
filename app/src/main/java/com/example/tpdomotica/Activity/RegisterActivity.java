package com.example.tpdomotica.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tpdomotica.BaseDatos.ConexionSQLite;
import com.example.tpdomotica.R;
import com.example.tpdomotica.Utilidades.Utilidades;


public class RegisterActivity extends AppCompatActivity {

    EditText nombre, apellido, username, dni, password, password2;
    boolean condicion = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nombre = (EditText) findViewById(R.id.form_nombre);
        apellido = (EditText) findViewById(R.id.form_apellido);
        username = (EditText) findViewById(R.id.form_username);
        dni = (EditText) findViewById(R.id.form_dni);
        password = (EditText) findViewById(R.id.form_pwd);
        password2 = (EditText)findViewById(R.id.pwd_repeat);
    }

    public void onClick(View view){

        registrarUsuario();
    }
    public void error(){
        Toast.makeText(getApplicationContext(),"Hubo un error al ingresar los datos", Toast.LENGTH_LONG).show();
        nombre.setText("");
        apellido.setText("");
        dni.setText("");
        username.setText("");
        password.setText("");
        password2.setText("");
        condicion = true;
    }

    public void registrarUsuario() {
        ConexionSQLite conn = new ConexionSQLite(this, "db_domotica", null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (!nombre.getText().toString().equals("")) {
            values.put(Utilidades.USER_NOMBRE, nombre.getText().toString());
        }else { condicion = false; }
        if (!apellido.getText().toString().equals("")) {
            values.put(Utilidades.USER_APELLIDO, apellido.getText().toString());
        }else { condicion = false; }
        if (!dni.getText().toString().equals("")) {
            values.put(Utilidades.USER_DNI, dni.getText().toString());
        }else { condicion = false; }
        if (!username.getText().toString().equals("")) {
            values.put(Utilidades.USER_USERNAME, username.getText().toString());
        }else { condicion = false; }

        values.put(Utilidades.USER_ROL, "user");

        if (!password.getText().toString().equals("")) {
            if (password.getText().toString().equals(password2.getText().toString())) {
                values.put(Utilidades.USER_PASSWORD, password.getText().toString());
            }else { condicion = false; }
        }else { condicion = false; }

        if(condicion == true){
            db.insert(Utilidades.TABLA_USUARIO,Utilidades.USER_USERNAME, values);
            Toast.makeText(getApplicationContext(), "Usuario generado", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        }else {
            error();
        }
        db.close();
    }
}