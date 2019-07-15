package com.example.tpdomotica.Activity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tpdomotica.BaseDatos.ConexionSQLite;
import com.example.tpdomotica.R;
import com.example.tpdomotica.Utilidades.Utilidades;


public class RegisterActivity extends AppCompatActivity {

    EditText nombre, apellido, username, dni, password, password2;
    TextView toLogin;
    Button signup;
    boolean condicion = true;
    boolean existe = false;

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
        toLogin = (TextView) findViewById(R.id.link_login);
        signup = findViewById(R.id.btn_signup);

        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarUsuario();
            }
        });
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
        }
        else { condicion = false; }
        existe = false;
        Cursor c = db.rawQuery("SELECT "+Utilidades.USER_USERNAME+" FROM " +Utilidades.TABLA_USUARIO, null);
        if (c.moveToFirst()){
            for (int i=0; i<c.getCount(); i++){
                if (c.getString(0).equals(username.getText().toString())) {
                    existe = true;
                    c.moveToNext();
                }
            }
        }

        values.put(Utilidades.USER_ROL, "user");

        if (!password.getText().toString().equals("")) {
            if (password.getText().toString().equals(password2.getText().toString())) {
                values.put(Utilidades.USER_PASSWORD, password.getText().toString());
            }else { condicion = false; }
        }else { condicion = false; }

        if(condicion == true && !existe){
            db.insert(Utilidades.TABLA_USUARIO,Utilidades.USER_USERNAME, values);
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.regis), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        }else {
            if (nombre.getText().toString().equals(""))
                nombre.setError(getResources().getString(R.string.no_empty));
            if (password.getText().toString().equals(""))
                password.setError(getResources().getString(R.string.no_empty));
            if (apellido.getText().toString().equals(""))
                apellido.setError(getResources().getString(R.string.no_empty));
            if (dni.getText().toString().equals(""))
                dni.setError(getResources().getString(R.string.no_empty));
            if (username.getText().toString().equals(""))
                username.setError(getResources().getString(R.string.no_empty));
            if (!password.getText().toString().equals(password2.getText().toString()))
                password2.setError(getResources().getString(R.string.no_match));
            if (existe){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getResources().getString(R.string.existe))
                        .setCancelable(true)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
        db.close();
    }
}