package com.example.tpdomotica;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ContenedorActivity extends AppCompatActivity implements EdificioFragment.OnFragmentInteractionListener,SensorFragment.OnFragmentInteractionListener{
    EdificioFragment listaEdificios;
    SensorFragment listaSensor;
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
}
