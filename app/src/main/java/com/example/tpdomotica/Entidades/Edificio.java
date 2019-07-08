package com.example.tpdomotica.Entidades;

import java.io.Serializable;

public class Edificio implements Serializable {

    private int ID;
    private String DIRECCION;
    private String DIRRECION_LAT;
    private String DIRECCION_LONG;
    private int ESTADO;
    private int ID_USUARIO;

    public Edificio() {

    }


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDIRRECION_LAT() {
        return DIRRECION_LAT;
    }

    public void setDIRRECION_LAT(String DIRRECION_LAT) {
        this.DIRRECION_LAT = DIRRECION_LAT;
    }

    public String getDIRECCION_LONG() {
        return DIRECCION_LONG;
    }

    public void setDIRECCION_LONG(String DIRECCION_LONG) {
        this.DIRECCION_LONG = DIRECCION_LONG;
    }

    public int getID_USUARIO() {
        return ID_USUARIO;
    }

    public String getDIRECCION() {
        return DIRECCION;
    }

    public void setDIRECCION(String DIRECCION) {
        this.DIRECCION = DIRECCION;
    }

    public int getESTADO() {
        return ESTADO;
    }

    public void setESTADO(int ESTADO) {
        this.ESTADO = ESTADO;
    }

    public void setID_USUARIO(int ID_USUARIO) {
        this.ID_USUARIO = ID_USUARIO;
    }
}
