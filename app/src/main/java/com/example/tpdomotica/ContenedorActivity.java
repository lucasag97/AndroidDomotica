package com.example.tpdomotica;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ContenedorActivity extends AppCompatActivity implements
        EdificioFragment.OnFragmentInteractionListener,SensorFragment.OnFragmentInteractionListener, DetalleEdificioFragment.OnFragmentInteractionListener, IComunicaFragment{
    EdificioFragment listaEdificios;
    SensorFragment listaSensor;
    DetalleEdificioFragment detalleEdificio;
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
}
