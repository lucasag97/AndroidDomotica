package com.example.tpdomotica.Entidades;

public class Historico  {
    private int HISTORICO_ID;
    private String ID_SENSOR_H;
    private String ID_EDIFICIO_H;
    private String HISTORICO_VALOR;
    private String HISTORICO_TIMESTAMP;
    private int HISTORICO_UMBRAL;
    private String HISTORICO_TIPO;


    public Historico(){

    }

    public String getHISTORICO_TIPO() {
        return HISTORICO_TIPO;
    }

    public void setHISTORICO_TIPO(String HISTORICO_TIPO) {
        this.HISTORICO_TIPO = HISTORICO_TIPO;
    }

    public int getHISTORICO_UMBRAL() {
        return HISTORICO_UMBRAL;
    }

    public void setHISTORICO_UMBRAL(int HISTORICO_UMBRAL) {
        this.HISTORICO_UMBRAL = HISTORICO_UMBRAL;
    }

    public int getHISTORICO_ID() {
        return HISTORICO_ID;
    }

    public void setHISTORICO_ID(int HISTORICO_ID) {
        this.HISTORICO_ID = HISTORICO_ID;
    }

    public String getID_SENSOR_H() {
        return ID_SENSOR_H;
    }

    public void setID_SENSOR_H(String ID_SENSOR_H) {
        this.ID_SENSOR_H = ID_SENSOR_H;
    }

    public String getID_EDIFICIO_H() {
        return ID_EDIFICIO_H;
    }

    public void setID_EDIFICIO_H(String ID_EDIFICIO_H) {
        this.ID_EDIFICIO_H = ID_EDIFICIO_H;
    }

    public String getHISTORICO_VALOR() {
        return HISTORICO_VALOR;
    }

    public void setHISTORICO_VALOR(String HISTORICO_VALOR) {
        this.HISTORICO_VALOR = HISTORICO_VALOR;
    }

    public String getHISTORICO_TIMESTAMP() {
        return HISTORICO_TIMESTAMP;
    }

    public void setHISTORICO_TIMESTAMP(String HISTORICO_TIMESTAMP) {
        this.HISTORICO_TIMESTAMP = HISTORICO_TIMESTAMP;
    }
}
