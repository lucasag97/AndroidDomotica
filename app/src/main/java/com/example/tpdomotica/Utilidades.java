package com.example.tpdomotica;

public class Utilidades {

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
    public static final String ID_EDIFICIO="id_edificio";
    public static final String DIRECCION_LAT="direccion_lat";
    public static final String DIRECCION_LONG="direccion_long";
    public static final String ID_PERSONA="id_persona";
    public static final String CREAR_TABLA_EDIFICIO= "CREATE TABLE"+ TABLA_EDIFICIO+" ("+ID_EDIFICIO+" INTEGER PRIMARY KEY  AUTOINCREMENT, "+DIRECCION_LAT+" TEXT, "+DIRECCION_LONG+" TEXT, "+ID_PERSONA+" INTEGER NOT NULL, FOREIGN KEY("+ID_PERSONA+") REFERENCES "+TABLA_USUARIO+"("+USER_ID+")";

    //tabla sensor
    public static final String TABLA_SENSOR="sensor";
    public static final String ID_SENSOR="id_sensor";
    public static final String TIPO="tipo";
    public static final String CREAR_TABLA_SENSOR = "CREATE TABLE "+TABLA_SENSOR+" ("+ID_SENSOR+" INTEGER PRIMARY KEY AUTOINCREMENT, "+TIPO+ " VARCHAR)";


}
