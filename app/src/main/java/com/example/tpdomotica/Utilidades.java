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

    //Tabla x
    //
}
