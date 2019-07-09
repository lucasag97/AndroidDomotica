package com.example.tpdomotica.Activity;

import android.app.ActionBar;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.tpdomotica.Entidades.Sensor;
import com.example.tpdomotica.Fragment.DetalleEdificioFragment;
import com.example.tpdomotica.Entidades.Edificio;
import com.example.tpdomotica.Fragment.DetalleSensorFragment;
import com.example.tpdomotica.Fragment.EdificioFragment;
import com.example.tpdomotica.Fragment.ModificarEdificioFragment;
import com.example.tpdomotica.Interface.IComunicaFragment;
import com.example.tpdomotica.R;

public class ContenedorActivity extends AppCompatActivity implements
        EdificioFragment.OnFragmentInteractionListener,
        DetalleSensorFragment.OnFragmentInteractionListener,DetalleEdificioFragment.OnFragmentInteractionListener, ModificarEdificioFragment.OnFragmentInteractionListener, IComunicaFragment {
    EdificioFragment listaEdificios;
    DetalleEdificioFragment detalleEdificio;
    DetalleSensorFragment detalleSensor;
    ModificarEdificioFragment modificarEdificio;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contenedor);

        listaEdificios = new EdificioFragment();



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
    public void enviarSensor(Sensor sensor){
        detalleSensor = new DetalleSensorFragment();
        Bundle bundleEnvio = new Bundle();
        bundleEnvio.putSerializable("sensor",sensor);
        detalleSensor.setArguments(bundleEnvio);

        getSupportFragmentManager().beginTransaction().replace(R.id.contenedorFragment,detalleSensor).addToBackStack(null).commit();
    }
    @Override
    public void modificarEdificio(Edificio edificio){
        modificarEdificio = new ModificarEdificioFragment();
        Bundle bundleEnvio = new Bundle();
        bundleEnvio.putSerializable("edificio",edificio);
        modificarEdificio.setArguments(bundleEnvio);

        getSupportFragmentManager().beginTransaction().replace(R.id.contenedorFragment,modificarEdificio).addToBackStack(null).commit();
    }
    @Override
    public void recargarEdificio(){
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedorFragment,listaEdificios).commit();
    }
}
