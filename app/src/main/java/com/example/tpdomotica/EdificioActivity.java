package com.example.tpdomotica;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class EdificioActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {
    CheckBox iluminacion,gases,movimiento,temperatura;
    EditText direccion;
    String Dirreccion_user;
    boolean ilu,gas,movi,temp,dir;
    Double longitud,latitud;
    Button btn_edificio_guardar,localizame;
    SharedPreferences pref;
    private SharedPreferences.Editor editor;


    View.OnClickListener guardar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ActivityCompat.requestPermissions(EdificioActivity.this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);
            if(ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(), "No se han definido los permisos necesarios", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),EdificioActivity.class);
                startActivity(intent);
            }else{

                ConexionSQLite conn = new ConexionSQLite(getApplicationContext(), "db_domotica", null, 1);
                SQLiteDatabase db = conn.getWritableDatabase();
                ContentValues values= new ContentValues();
                Dirreccion_user =  direccion.getText().toString();
                if(!Dirreccion_user.equals(null) && !Dirreccion_user.equals("")){
                    values.put(Utilidades.EDI_DIRECCION,direccion.getText().toString());
                    dir = true;
                }else {
                    dir =false;
                }
                values.put(Utilidades.EDI_DIRECCION_LAT, latitud);
                values.put(Utilidades.EDI_DIRECCION_LONG, longitud);
                values.put(Utilidades.EDI_ID_USUARIO, pref.getString("id",""));
                db.insert(Utilidades.TABLA_EDIFICIO, null, values);
                //db.close();
                values.clear();

                SQLiteDatabase read = conn.getReadableDatabase();
                Cursor cursor = read.rawQuery("SELECT * FROM "+Utilidades.TABLA_EDIFICIO+" ORDER BY "+Utilidades.EDI_ID+" DESC LIMIT 1", null);
                cursor.moveToFirst();
                int idEdi = cursor.getInt(0);

                if(ilu == true){
                    values.put(Utilidades.ID_SENSOR, 1);
                    values.put(Utilidades.ID_EDIFICIO, idEdi);
                    values.put(Utilidades.EDI_SENS_VALOR, "0");
                    db.insert(Utilidades.TABLA_EDIFICIO_SENSOR,null,values);
                    values.clear();
                }
                if (gas == true){
                    values.put(Utilidades.ID_SENSOR, 2);
                    values.put(Utilidades.ID_EDIFICIO, idEdi);
                    values.put(Utilidades.EDI_SENS_VALOR, "0");
                    db.insert(Utilidades.TABLA_EDIFICIO_SENSOR,null,values);
                    values.clear();
                }
                if (movi == true){
                    values.put(Utilidades.ID_SENSOR, 3);
                    values.put(Utilidades.ID_EDIFICIO, idEdi);
                    values.put(Utilidades.EDI_SENS_VALOR, "0");
                    db.insert(Utilidades.TABLA_EDIFICIO_SENSOR,null,values);
                    values.clear();
                }
                if (temp == true){
                    values.put(Utilidades.ID_SENSOR, 4);
                    values.put(Utilidades.ID_EDIFICIO, idEdi);
                    values.put(Utilidades.EDI_SENS_VALOR, "0");
                    db.insert(Utilidades.TABLA_EDIFICIO_SENSOR,null,values);
                    values.clear();
                }

                db.close();

                editor = pref.edit();
                editor.putInt("idEdi",idEdi);
                editor.commit();

                /*Toast.makeText(getApplicationContext(),"Se habilito el edificio con exito",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);*/
                if(dir == true) {
                    Toast.makeText(getApplicationContext(), "Se habilito el edificio con exito", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Error en la direccion",Toast.LENGTH_SHORT).show();
                    Intent error = new Intent(getApplicationContext(),EdificioActivity.class);
                    startActivity(error);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edificio);

        pref = getSharedPreferences("MisPreferencias",MODE_PRIVATE);

        direccion = (EditText) findViewById(R.id.direccion);

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

        localizame = (Button) findViewById(R.id.edificio_localizame);
        localizame.setOnClickListener(new View.OnClickListener() {
            int permissionCheck = ContextCompat.checkSelfPermission(EdificioActivity.this,Manifest.permission.ACCESS_FINE_LOCATION);
            @Override
            public void onClick(View v) {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(EdificioActivity.this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);
                    LocationListener locationListener = new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            longitud = location.getLongitude();
                            latitud = location.getLatitude();
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    };
                }
            }
        });
        btn_edificio_guardar = (Button) findViewById(R.id.btn_edificio_guardar);
        btn_edificio_guardar.setOnClickListener(guardar);
    }

}
