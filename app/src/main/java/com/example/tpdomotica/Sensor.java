package com.example.tpdomotica;

public class Sensor {
    private int ID;
    private String TIPO;
    private int UMBRAL;

    public Sensor() {

    }
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTIPO() {
        return TIPO;
    }

    public void setTIPO(String TIPO) {
        this.TIPO = TIPO;
    }

    public int getUMBRAL() {
        return UMBRAL;
    }

    public void setUMBRAL(int UMBRAL) {
        this.UMBRAL = UMBRAL;
    }
}
