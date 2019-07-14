package com.example.tpdomotica.Interface;

import com.example.tpdomotica.Entidades.Edificio;
import com.example.tpdomotica.Entidades.Sensor;

public interface IComunicaFragment {

    public void enviarEdificio(Edificio edificio);
    public void enviarSensor(Sensor sensor);
    public void modificarEdificio(Edificio edificio);
    public void recargarEdificio();
    public void irApendientes();
}
