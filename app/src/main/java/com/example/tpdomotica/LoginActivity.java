package com.example.tpdomotica;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.tpdomotica.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText username, password;
    private Button login,registrar;

    private ConexionSQLite db;

    View.OnClickListener RegistroListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(intent);
        }
    };
    View.OnClickListener LoginListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            Verificar();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new ConexionSQLite(this, "db_domotica", null, 1);

        username = (EditText)findViewById(R.id.login_username);
        password = (EditText)findViewById(R.id.login_pwd);
        login = (Button)findViewById(R.id.btn_login);
        registrar = (Button) findViewById(R.id.btn_toregistro);

        registrar.setOnClickListener(RegistroListener);
        login.setOnClickListener(LoginListener);

    }
    public void Verificar(){
        SQLiteDatabase db_consulta = db.getReadableDatabase();
        String [] parametros = {username.getText().toString()};
        String [] campos = {"_id,nombre,apellido,dni,username,password,rol"};

        //SELECT * FROM usuarios WHERE Email = 'example@';

        try {
            //consulta para sacar un usuario
            Cursor cursor = db_consulta.query("usuarios",campos,"username=?",parametros,null,null,null);
            cursor.moveToFirst();
            //creo un usuario con los datos de la bd
            Usuario usuario = new Usuario(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4), cursor.getString(5));
            usuario.setId(cursor.getInt(0));
            usuario.setRol(cursor.getString(6));

            String contraseña_usuario = usuario.getPassword();
            String contraseña_ingresada = password.getText().toString();
            if(contraseña_usuario.equals(contraseña_ingresada)){
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(getApplicationContext(),"Contraseña incorrecta",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Usuario no registrado",Toast.LENGTH_SHORT).show();
        }




    }
}