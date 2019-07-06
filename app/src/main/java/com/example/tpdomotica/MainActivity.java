package com.example.tpdomotica;

import android.app.Notification;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
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

    Button crear_sensores, service, VerEdificios;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConexionSQLite conn = new ConexionSQLite(this, "db_domotica", null, 1);
        pref = getSharedPreferences("MisPreferencias",MODE_PRIVATE);
        crear_sensores = (Button) findViewById(R.id.btn_sensores);

        crear_sensores.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                /* Creamos sensores para uso en form de edificio */
                ConexionSQLite conn = new ConexionSQLite(getApplicationContext(), "db_domotica", null, 1);
                SQLiteDatabase db = conn.getWritableDatabase();
                ContentValues values_sensor = new ContentValues();
                //Iluminacion
                values_sensor.put(Utilidades.SENSOR_TIPO,"iluminacion");
                values_sensor.put(Utilidades.SENSOR_UMBRAL,"100");
                db.insert(Utilidades.TABLA_SENSOR,null,values_sensor);
                values_sensor.clear();
                //Gas
                values_sensor.put(Utilidades.SENSOR_TIPO,"gas");
                values_sensor.put(Utilidades.SENSOR_UMBRAL,"1");
                db.insert(Utilidades.TABLA_SENSOR,null,values_sensor);
                values_sensor.clear();
                //Movimiento
                values_sensor.put(Utilidades.SENSOR_TIPO,"movimiento");
                values_sensor.put(Utilidades.SENSOR_UMBRAL,"1");
                db.insert(Utilidades.TABLA_SENSOR,null,values_sensor);
                values_sensor.clear();
                //Temperatura
                values_sensor.put(Utilidades.SENSOR_TIPO,"temperatura");
                values_sensor.put(Utilidades.SENSOR_UMBRAL,"50");
                db.insert(Utilidades.TABLA_SENSOR,null,values_sensor);
                values_sensor.clear();

                db.close();
                Toast.makeText(getApplicationContext(), "Se insertaron los valores en la tabla", Toast.LENGTH_SHORT).show();
            }
        });

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
                startActivity(cerrar_sesion);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}