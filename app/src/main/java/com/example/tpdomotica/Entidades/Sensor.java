package com.example.tpdomotica.Entidades;

import java.io.Serializable;

public class Sensor implements Serializable {
    private int ID;
    private String TIPO;
    private int UMBRAL;
    private int VALOR_ACTUAL;
    private int ID_EDI;

    public int getID_EDI() {
        return ID_EDI;
    }

    public void setID_EDI(int ID_EDI) {
        this.ID_EDI = ID_EDI;
    }

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
