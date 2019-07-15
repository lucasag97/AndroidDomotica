package com.example.tpdomotica.Utilidades;

import java.util.ArrayList;

public class Utilidades {

    public static ArrayList<Integer> edis = new ArrayList();
    public static int item = 0;

    //Tabla Usuario
    public static final String TABLA_USUARIO="usuarios";
    public static final String USER_ID="_id";
    public static final String USER_NOMBRE = "nombre";
    public static final String USER_APELLIDO = "apellido";
    public static final String USER_DNI = "dni";
    public static final String USER_USERNAME = "username";
    public static final String USER_PASSWORD = "password";
    public static final String USER_ROL = "rol";
    public static final String CREAR_TABLA_USER = "CREATE TABLE "+ TABLA_USUARIO +" ("+USER_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+USER_NOMBRE+" TEXT, "+USER_APELLIDO+" TEXT, "+USER_DNI+" VARCHAR, "+USER_USERNAME+" VARCHAR, "+USER_PASSWORD+" VARCHAR, "+USER_ROL+" VARCHAR)";

    //Tabla edificio
    public static final String TABLA_EDIFICIO="edificio";
    public static final String EDI_ID="_id";
    public static final String EDI_NOMBRE="nombre";
    public static final String EDI_DIRECCION = "direccion";
    public static final String EDI_DIRECCION_LAT="direccion_lat";
    public static final String EDI_DIRECCION_LONG="direccion_long";
    public static final String EDI_ESTADO = "estado";
    public static final String EDI_ID_USUARIO="id_usuario";
    public static final String CREAR_TABLA_EDIFICIO= "CREATE TABLE "+ TABLA_EDIFICIO+" ("+EDI_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+EDI_NOMBRE+" VARCHAR, "+EDI_DIRECCION+" VARCHAR, "+EDI_DIRECCION_LAT+" TEXT, "+EDI_DIRECCION_LONG+" TEXT, "+EDI_ESTADO+" INTEGER, "+EDI_ID_USUARIO+" INTEGER NOT NULL, FOREIGN KEY("+EDI_ID_USUARIO+") REFERENCES "+TABLA_USUARIO+"("+USER_ID+"))";

    //tabla sensor
    public static final String TABLA_SENSOR="sensor";
    public static final String SENSOR_ID="_id";
    public static final String SENSOR_TIPO="tipo";
    public static final String SENSOR_UMBRAL =  "umbral";
    public static final String CREAR_TABLA_SENSOR = "CREATE TABLE "+TABLA_SENSOR+" ("+SENSOR_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+SENSOR_TIPO+ " VARCHAR, "+SENSOR_UMBRAL+" VARCHAR)";

    //tabla edificio-sensor
    public static final String TABLA_EDIFICIO_SENSOR = "edificio_sensor";
    public static final String ID_SENSOR = "id_sensor";
    public static final String ID_EDIFICIO = "id_edificio";
    public static final String EDI_SENS_VALOR = "valor";
    public static final String EDI_SENS_TIMESTAMP = "momento";
    public static final String CREAR_TABLA_EDIFICIO_SENSOR = "CREATE TABLE "+TABLA_EDIFICIO_SENSOR+" ("+ID_SENSOR+" INTEGER, "+ID_EDIFICIO+" INTEGER, "+EDI_SENS_VALOR+" INTEGER, "+EDI_SENS_TIMESTAMP+" DEFAULT CURRENT_TIMESTAMP, PRIMARY KEY ("+ID_EDIFICIO+", "+ID_SENSOR+"), FOREIGN KEY ("+ID_EDIFICIO+") REFERENCES "+TABLA_EDIFICIO+"("+EDI_ID+"), FOREIGN KEY ("+ID_SENSOR+") REFERENCES "+TABLA_SENSOR+"("+SENSOR_ID+"))";

    //Tabla historico_sensor
    public static final String TABLA_HISTORICO = "historico_sensor";
    public static final String HISTORICO_ID = "_id";
    public static final String ID_SENSOR_H = "id_sensor";
    public final static String ID_EDIFICIO_H = "id_edificio";
    public static final String HISTORICO_VALOR = "valor";
    public static final String HISTORICO_TIMESTAMP = "momento";
    public static final String CREAR_TABLA_HISTORICO = "CREATE TABLE "+TABLA_HISTORICO+" ("+HISTORICO_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+ID_SENSOR_H+" INTEGER, "+ID_EDIFICIO_H+" INTEGER, "+HISTORICO_VALOR+" INTEGER, "+HISTORICO_TIMESTAMP+" DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY ("+ID_EDIFICIO_H+") REFERENCES "+TABLA_EDIFICIO+"("+EDI_ID+"), FOREIGN KEY ("+ID_SENSOR_H+") REFERENCES "+TABLA_SENSOR+"("+SENSOR_ID+"))";

}
