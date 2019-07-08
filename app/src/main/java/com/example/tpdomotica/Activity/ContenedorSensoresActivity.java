package com.example.tpdomotica.Activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.tpdomotica.Fragment.DetalleEdificioFragment;
import com.example.tpdomotica.Fragment.DetalleSensorFragment;
import com.example.tpdomotica.Interface.IComunicaFragmentSensores;
import com.example.tpdomotica.R;
import com.example.tpdomotica.Entidades.Sensor;
import com.example.tpdomotica.Fragment.SensorFragment;

public class ContenedorSensoresActivity extends AppCompatActivity implements
        IComunicaFragmentSensores, SensorFragment.OnFragmentInteractionListener, DetalleEdificioFragment.OnFragmentInteractionListener {

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
