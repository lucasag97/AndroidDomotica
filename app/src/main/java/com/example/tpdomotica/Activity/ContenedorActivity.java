package com.example.tpdomotica.Activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.tpdomotica.Entidades.Sensor;
import com.example.tpdomotica.Fragment.DetalleEdificioFragment;
import com.example.tpdomotica.Entidades.Edificio;
import com.example.tpdomotica.Fragment.DetalleSensorFragment;
import com.example.tpdomotica.Fragment.EdificioFragment;
import com.example.tpdomotica.Interface.IComunicaFragment;
import com.example.tpdomotica.R;
import com.example.tpdomotica.Fragment.SensorFragment;

public class ContenedorActivity extends AppCompatActivity implements
        EdificioFragment.OnFragmentInteractionListener, SensorFragment.OnFragmentInteractionListener,
        DetalleSensorFragment.OnFragmentInteractionListener,DetalleEdificioFragment.OnFragmentInteractionListener, IComunicaFragment {
    EdificioFragment listaEdificios;
    SensorFragment listaSensor;
    DetalleEdificioFragment detalleEdificio;
    DetalleSensorFragment detalleSensor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contenedor);

        listaEdificios = new EdificioFragment();
        listaSensor = new SensorFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.contenedorFragment,listaEdificios).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    @Override
    public void enviarEdificio(Edificio edificio){
        detalleEdificio = new DetalleEdificioFragment();
        Bundle bundleEnvio = new Bundle();
        bundleEnvio.putSerializable("objeto",edificio);
        detalleEdificio.setArguments(bundleEnvio);

        //cargamos el fragment en el activity
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedorFragment,detalleEdificio).addToBackStack(null).commit();
    }
    @Override
    public void enviarEdificioAsensor(Edificio edificio){
        listaSensor = new SensorFragment();
        Bundle bundleEnvio = new Bundle();
        bundleEnvio.putSerializable("objeto",edificio);
        listaSensor.setArguments(bundleEnvio);

        getSupportFragmentManager().beginTransaction().replace(R.id.contenedorFragment,listaSensor).addToBackStack(null).commit();
    }
    @Override
    public void enviarSensor(Sensor sensor){
        detalleSensor = new DetalleSensorFragment();
        Bundle bundleEnvio = new Bundle();
        bundleEnvio.putSerializable("sensor",sensor);
        detalleSensor.setArguments(bundleEnvio);

        getSupportFragmentManager().beginTransaction().replace(R.id.contenedorFragment,detalleSensor).addToBackStack(null).commit();
    }
}
