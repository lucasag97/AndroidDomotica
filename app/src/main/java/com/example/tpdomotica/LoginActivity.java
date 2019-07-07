package com.example.tpdomotica;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import com.example.tpdomotica.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText username, password;
    private Button login,registrar;
    private Switch remember;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Boolean isRemembered;
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
            verificar();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new ConexionSQLite(this, "db_domotica", null, 1);

        pref = getSharedPreferences("MisPreferencias",MODE_PRIVATE);
        editor = pref.edit();
        username = (EditText)findViewById(R.id.login_username);
        password = (EditText)findViewById(R.id.login_pwd);
        //rellenarCampos(username,password);
        login = (Button)findViewById(R.id.btn_login);
        registrar = (Button) findViewById(R.id.btn_toregistro);
        remember = (Switch) findViewById(R.id.recuerdame);
        registrar.setOnClickListener(RegistroListener);
        login.setOnClickListener(LoginListener);

        isRemembered = pref.getBoolean("remember", false);
        if (isRemembered){
            username.setText(pref.getString("username", ""));
            password.setText(pref.getString("password", ""));
            remember.setChecked(true);
        }

    }
    public void verificar(){
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
                recuerdaUsuario(cursor.getString(4),cursor.getString(5));
                iniciarSesion(cursor.getString(0),cursor.getString(6));
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                editor.putBoolean("logged", true);
                editor.commit();
                startActivity(intent);
            }
            else{
                Toast.makeText(getApplicationContext(),"Contraseña incorrecta",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Usuario no registrado",Toast.LENGTH_SHORT).show();
        }
    }
    private void recuerdaUsuario(String username,String password){
        if(remember.isChecked()){
            editor.putBoolean("remember", true);
            editor.putString("username",username);
            editor.putString("password",password);
            editor.commit();
        }
        else{
            editor.clear();
            editor.commit();
        }
    }
    private void iniciarSesion(String id,String rol){
        //SharedPreferences.Editor editor = pref.edit();
        editor.putString("id",id);
        editor.putString("rol",rol);
        editor.commit();
    }
}