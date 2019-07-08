package com.example.tpdomotica;

import java.io.Serializable;

public class Sensor implements Serializable {
    private int ID;
    private String TIPO;
    private int UMBRAL;
    private int VALOR_ACTUAL;

    public int getVALOR_ACTUAL() {
        return VALOR_ACTUAL;
    }

    public void setVALOR_ACTUAL(int VALOR_ACTUAL) {
        this.VALOR_ACTUAL = VALOR_ACTUAL;
    }

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
