package com.example.tpdomotica;

public class Edificio {
    private int ID;
    private String DIRRECION_LAT;
    private String DIRECCION_LONG;
    private int ID_USUARIO;

    public Edificio(int ID, String DIRRECION_LAT, String DIRECCION_LONG, int ID_USUARIO) {
        this.ID = ID;
        this.DIRRECION_LAT = DIRRECION_LAT;
        this.DIRECCION_LONG = DIRECCION_LONG;
        this.ID_USUARIO = ID_USUARIO;
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

    public void setID_USUARIO(int ID_USUARIO) {
        this.ID_USUARIO = ID_USUARIO;
    }
}
