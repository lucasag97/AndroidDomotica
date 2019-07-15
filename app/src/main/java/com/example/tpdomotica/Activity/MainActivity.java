package com.example.tpdomotica.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.tpdomotica.BaseDatos.ConexionSQLite;
import com.example.tpdomotica.R;
import com.example.tpdomotica.Entidades.Servicio;
import com.example.tpdomotica.Utilidades.Utilidades;

import java.util.Locale;

public class MainActivity extends AppCompatActivity{

    Button service, VerEdificios, cantEdi;
    SharedPreferences pref;
    boolean ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = getSharedPreferences("MisPreferencias",MODE_PRIVATE);

        ft = pref.getBoolean("firstTime", true);

        String lang = pref.getString("lang", "");

        loadLocale(lang);

        //Si no esta logueado va a login
        boolean isLogged = pref.getBoolean("logged", false);
        if (!isLogged){
            Intent login = new Intent(this, LoginActivity.class);
            finish();
            startActivity(login);
        }
        else{
            Utilidades.edis.clear();
            String id = pref.getString("id", "");
            ConexionSQLite db = new ConexionSQLite(this, "db_domotica", null, 1);
            SQLiteDatabase db1 = db.getReadableDatabase();
            Cursor c = db1.rawQuery("SELECT DISTINCT "+ Utilidades.EDI_ID+" FROM "+Utilidades.TABLA_EDIFICIO+" WHERE "+Utilidades.EDI_ID_USUARIO+" = "+id+" AND "+Utilidades.EDI_ESTADO+" = 1", null);
            if (c.moveToFirst()){
                for (int i=0; i<c.getCount(); i++) {
                    Utilidades.edis.add(c.getInt(0));
                    c.moveToNext();
                }
            }
            if (Utilidades.edis.size() >= 1 && !Servicio.isRunning()) {
                startService(new Intent(MainActivity.this, Servicio.class));
            }
            db1.close();
            Intent intent = new Intent(this, ContenedorActivity.class);
            finish();
            startActivity(intent);
        }

        if(ft){
            Intent first = new Intent(this, FirstTimeActivity.class);
            finish();
            startActivity(first);
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

        cantEdi = findViewById(R.id.btn_edis);
        cantEdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String size = Integer.toString(Utilidades.edis.size());
                Toast.makeText(getApplicationContext(), size, Toast.LENGTH_SHORT).show();
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
                editor.remove("id");
                Utilidades.edis.clear();
                editor.commit();
                stopService(new Intent(MainActivity.this,Servicio.class));
                stopService(new Intent(MainActivity.this,Servicio.class));
                startActivity(cerrar_sesion);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void loadLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }
}