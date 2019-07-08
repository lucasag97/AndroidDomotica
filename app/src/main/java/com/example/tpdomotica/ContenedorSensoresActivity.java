package com.example.tpdomotica;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ContenedorSensoresActivity extends AppCompatActivity implements
        IComunicaFragmentSensores, SensorFragment.OnFragmentInteractionListener, DetalleEdificioFragment.OnFragmentInteractionListener{

    SensorFragment listaSensor;
    DetalleSensorFragment detalleSensor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contenedor_sensores);
        listaSensor = new SensorFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.contenedorFragment,listaSensor).commit();

    }
    @Override
    public void enviarSensor(Sensor sensor){

    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
