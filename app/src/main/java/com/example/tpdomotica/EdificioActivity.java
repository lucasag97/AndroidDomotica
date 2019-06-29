package com.example.tpdomotica;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;

public class EdificioActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {
    CheckBox iluminacion,gases,movimiento,temperatura;
    boolean ilu,gas,movi,temp;
    String longitud,latitud;
    RadioButton localizame;
    LocationManager locManager;
    Button btn_edificio_guardar;
    Location loc;
    SharedPreferences pref;

    View.OnClickListener guardar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ActivityCompat.requestPermissions(EdificioActivity.this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);
            if(ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(), "No se han definido los permisos necesarios", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),EdificioActivity.class);
                startActivity(intent);
            }else{
                /*locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                longitud = String.valueOf(loc.getLongitude());
                latitud = String.valueOf(loc.getLatitude());*/

                ConexionSQLite conn = new ConexionSQLite(getApplicationContext(), "db_domotica", null, 1);
                SQLiteDatabase db = conn.getWritableDatabase();
                ContentValues values_edi = new ContentValues();
                ContentValues values_sensor = new ContentValues();

                if(ilu == true){
                    //revisar por que no mete los datos del sensor en la bd
                    Toast.makeText(getApplicationContext(),"ilumicnacion",Toast.LENGTH_SHORT).show();
                    values_sensor.put(Utilidades.SENSOR_TIPO,"iluminacion");
                    values_sensor.put(Utilidades.SENSOR_UMBRAL,"100");
                    db.insert(Utilidades.TABLA_SENSOR,null,values_sensor);
                    values_sensor.clear();
                }
                if (gas == true){
                    Toast.makeText(getApplicationContext(),"gases",Toast.LENGTH_SHORT).show();
                    values_sensor.put(Utilidades.SENSOR_TIPO,"Gases toxicos");
                    values_sensor.put(Utilidades.SENSOR_UMBRAL,"10");
                    db.insert(Utilidades.TABLA_SENSOR,null,values_sensor);
                    values_sensor.clear();
                }
                if (movi == true){
                    Toast.makeText(getApplicationContext(),"movimiento",Toast.LENGTH_SHORT).show();
                    values_edi.put(Utilidades.SENSOR_TIPO,"movimiento");
                    values_sensor.put(Utilidades.SENSOR_TIPO,"movimiento");
                    values_sensor.put(Utilidades.SENSOR_UMBRAL,"0");
                    db.insert(Utilidades.TABLA_SENSOR,null,values_sensor);
                    values_sensor.clear();
                }
                if (temp == true){
                    Toast.makeText(getApplicationContext(),"temperatura",Toast.LENGTH_SHORT).show();
                    values_edi.put(Utilidades.SENSOR_TIPO,"temperatura");
                    values_sensor.put(Utilidades.SENSOR_TIPO,"temperatura");
                    values_sensor.put(Utilidades.SENSOR_UMBRAL,"50");
                    db.insert(Utilidades.TABLA_SENSOR,null,values_sensor);
                    values_sensor.clear();
                }/*
                values_edi.put(Utilidades.EDI_DIRECCION_LAT,latitud);
                values_edi.put(Utilidades.EDI_DIRECCION_LONG,longitud);
                values_edi.put(Utilidades.EDI_ID_USUARIO,pref.getString("id",""));
                db.insert(Utilidades.TABLA_EDIFICIO,null,values_edi);*/
                Toast.makeText(getApplicationContext(),"Se habilito el edificio con exito",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edificio);

        pref = getSharedPreferences("MisPreferencias",MODE_PRIVATE);

        iluminacion = (CheckBox) findViewById(R.id.edificio_iluminacion);
        iluminacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = ((CheckBox)v).isChecked();
                if(isChecked) {
                    ilu = true;
                }else{
                    ilu = false;
                }
            }
        });

        gases = (CheckBox) findViewById(R.id.edificio_gases);
        gases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = ((CheckBox)v).isChecked();
                if(isChecked){
                    gas = true;
                }else{
                    gas = false;
                }
            }
        });

        movimiento = (CheckBox) findViewById(R.id.edificio_movimiento);
        movimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = ((CheckBox)v).isChecked();
                if(isChecked){
                    movi=true;
                }else {
                    movi = false;
                }
            }
        });

        temperatura =  (CheckBox) findViewById(R.id.edificio_temperatura);
        temperatura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = ((CheckBox)v).isChecked();
                if(isChecked){
                    temp = true;
                }else{
                    temp = false;
                }
            }
        });

        localizame = (RadioButton) findViewById(R.id.edificio_localizame);


        btn_edificio_guardar = (Button) findViewById(R.id.btn_edificio_guardar);
        btn_edificio_guardar.setOnClickListener(guardar);
    }

}
