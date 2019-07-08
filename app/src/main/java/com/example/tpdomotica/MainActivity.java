package com.example.tpdomotica;

import android.app.Notification;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{

    Button service, VerEdificios;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConexionSQLite conn = new ConexionSQLite(this, "db_domotica", null, 1);
        pref = getSharedPreferences("MisPreferencias",MODE_PRIVATE);

        //Si no esta logueado va a login
        boolean isLogged = pref.getBoolean("logged", false);
        if (!isLogged){
            Intent login = new Intent(this, LoginActivity.class);
            startActivity(login);
        }
        else{
            Utilidades.edis.clear();
            SQLiteDatabase db = conn.getReadableDatabase();
            Cursor c = db.rawQuery("SELECT DISTINCT "+Utilidades.EDI_ID+" FROM "+Utilidades.TABLA_EDIFICIO+" WHERE "+Utilidades.EDI_ID_USUARIO+" = "+pref.getString("id", "999"), null);
            if (c.moveToFirst()){
                Utilidades.edis.add(c.getInt(0));
            }
        }

        service = (Button) findViewById(R.id.btn_service);
        service.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (service.getText().toString().equals("Iniciar servicio")) {
                    service.setText("Detener servicio");
                    startService(new Intent(MainActivity.this, Servicio.class));
                }
                else {
                    service.setText("Iniciar servicio");
                    stopService(new Intent(MainActivity.this,Servicio.class));
                }
            }
        });
        VerEdificios = (Button) findViewById(R.id.idVerEdificios);
        VerEdificios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ver = new Intent(getApplicationContext(),ContenedorActivity.class);
                startActivity(ver);
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
                cerrar_sesion.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("logged", false);
                Utilidades.edis.clear();
                editor.commit();
                startActivity(cerrar_sesion);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}