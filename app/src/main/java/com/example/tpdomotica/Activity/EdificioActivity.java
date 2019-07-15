package com.example.tpdomotica.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import com.example.tpdomotica.BaseDatos.ConexionSQLite;
import com.example.tpdomotica.Entidades.Servicio;
import com.example.tpdomotica.R;
import com.example.tpdomotica.Utilidades.Utilidades;

public class EdificioActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {
    CheckBox iluminacion,gases,movimiento,temperatura;
    TextView estado_text;
    EditText direccion, nombre;
    String Direccion_user;
    boolean ilu,gas,movi,temp, validado;
    Double longitud,latitud;
    Button btn_edificio_guardar, btn_estado;
    Switch localizame;
    SharedPreferences pref;
    int estado_num;
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
                Direccion_user =  direccion.getText().toString();
                String nombre1 = nombre.getText().toString();
                if(!Direccion_user.equals(""))
                    values.put(Utilidades.EDI_DIRECCION,direccion.getText().toString());
                if (!nombre1.equals(""))
                    values.put(Utilidades.EDI_NOMBRE, nombre1);
                if (!Direccion_user.equals("") && !nombre1.equals(""))
                    validado = true;
                if (!ilu && !gas && !movi && !temp)
                    validado = false;
                values.put(Utilidades.EDI_DIRECCION_LAT, latitud);
                values.put(Utilidades.EDI_DIRECCION_LONG, longitud);
                values.put(Utilidades.EDI_ESTADO, estado_num); //Estado edificio pendiente = 0, aprobado = 1, rechazado = 2
                values.put(Utilidades.EDI_ID_USUARIO, pref.getString("id",""));

                if(validado) {
                    db.insert(Utilidades.TABLA_EDIFICIO, null, values);
                    values.clear();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.pen_toast), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    finish();
                    startActivity(intent);

                    SQLiteDatabase read = conn.getReadableDatabase();
                    Cursor cursor = read.rawQuery("SELECT * FROM "+Utilidades.TABLA_EDIFICIO+" ORDER BY "+Utilidades.EDI_ID+" DESC LIMIT 1", null);
                    cursor.moveToFirst();
                    int idEdi = cursor.getInt(0);

                    if(ilu){
                        values.put(Utilidades.ID_SENSOR, 1);
                        values.put(Utilidades.ID_EDIFICIO, idEdi);
                        values.put(Utilidades.EDI_SENS_VALOR, "0");
                        db.insert(Utilidades.TABLA_EDIFICIO_SENSOR,null,values);
                        values.clear();
                    }
                    if (gas){
                        values.put(Utilidades.ID_SENSOR, 2);
                        values.put(Utilidades.ID_EDIFICIO, idEdi);
                        values.put(Utilidades.EDI_SENS_VALOR, "0");
                        db.insert(Utilidades.TABLA_EDIFICIO_SENSOR,null,values);
                        values.clear();
                    }
                    if (movi){
                        values.put(Utilidades.ID_SENSOR, 3);
                        values.put(Utilidades.ID_EDIFICIO, idEdi);
                        values.put(Utilidades.EDI_SENS_VALOR, "0");
                        db.insert(Utilidades.TABLA_EDIFICIO_SENSOR,null,values);
                        values.clear();
                    }
                    if (temp){
                        values.put(Utilidades.ID_SENSOR, 4);
                        values.put(Utilidades.ID_EDIFICIO, idEdi);
                        values.put(Utilidades.EDI_SENS_VALOR, "0");
                        db.insert(Utilidades.TABLA_EDIFICIO_SENSOR,null,values);
                        values.clear();
                    }

                    if (estado_num == 1) {
                        Utilidades.edis.add(idEdi);
                    }
                    if (Utilidades.edis.size() == 1){
                        startService(new Intent(getApplicationContext(), Servicio.class));
                    }
                }else {
                    if (Direccion_user.equals("")) {
                        direccion.setError(getResources().getString(R.string.no_empty));
                    }
                    if (nombre1.equals("")){
                        nombre.setError((getResources().getString(R.string.no_empty)));
                    }
                    if (!ilu && !gas && !movi && !temp){
                        AlertDialog.Builder builder = new AlertDialog.Builder(EdificioActivity.this);
                        builder.setMessage(getResources().getString(R.string.please_sens))
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
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edificio);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(getString(R.string.crear_edi));
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        pref = getSharedPreferences("MisPreferencias",MODE_PRIVATE);

        direccion = (EditText) findViewById(R.id.direccion);

        nombre = findViewById(R.id.edi_nombre);

        iluminacion = (CheckBox) findViewById(R.id.edificio_iluminacion);
        iluminacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = ((CheckBox)v).isChecked();
                if(isChecked) {
                    ilu = true;
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
                }
            }
        });

        localizame = (Switch) findViewById(R.id.edificio_localizame);
        localizame.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int permissionCheck = ContextCompat.checkSelfPermission(EdificioActivity.this,Manifest.permission.ACCESS_FINE_LOCATION);

                if (isChecked) {
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                        ActivityCompat.requestPermissions(EdificioActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
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
            }
        });
        btn_edificio_guardar = (Button) findViewById(R.id.btn_edificio_guardar);
        btn_edificio_guardar.setOnClickListener(guardar);

        btn_estado = findViewById(R.id.btn_estado);

        estado_num = 0;

        estado_text = findViewById(R.id.estadotexto);

        btn_estado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(estado_text.getText().toString().equals("El estado es: Pendiente")) {
                    estado_text.setText("El estado es: Activo");
                    estado_num = 1;
                }
                else{
                    estado_text.setText("El estado es: Pendiente");
                    estado_num = 0;
                }
            }
        });
    }

}
