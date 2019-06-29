package com.example.tpdomotica;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button registro;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConexionSQLite conn = new ConexionSQLite(this, "db_domotica", null, 1);

        registro = (Button) findViewById(R.id.btn_registro);

        registro.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent registro = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(registro);
            }
        });

        login = (Button) findViewById(R.id.btn_login1);
        login.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent login = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(login);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_edificio:
                Intent intent = new Intent(getApplicationContext(),EdificioActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_logout:
                Intent cerrar_sesion = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(cerrar_sesion);
                return true;
            case R.id.menu_forget_logout:
                Intent cerrar_sesion_olvidar = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(cerrar_sesion_olvidar);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}