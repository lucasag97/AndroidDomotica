package com.example.tpdomotica.BaseDatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tpdomotica.Entidades.Edificio;
import com.example.tpdomotica.Utilidades.Utilidades;

public class ConexionSQLite extends SQLiteOpenHelper {

    public ConexionSQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(Utilidades.CREAR_TABLA_USER);
        db.execSQL(Utilidades.CREAR_TABLA_EDIFICIO);
        db.execSQL(Utilidades.CREAR_TABLA_SENSOR);
        db.execSQL(Utilidades.CREAR_TABLA_EDIFICIO_SENSOR);
        db.execSQL(Utilidades.CREAR_TABLA_HISTORICO);

        //Valores por defecto

        //User
        final String crear_user = "INSERT INTO "+ Utilidades.TABLA_USUARIO+" ("
                +Utilidades.USER_NOMBRE+", "
                +Utilidades.USER_APELLIDO+", "
                +Utilidades.USER_DNI+", "
                +Utilidades.USER_USERNAME+", "
                +Utilidades.USER_PASSWORD+", "
                +Utilidades.USER_ROL+") VALUES ('test_nombre', 'test_apellido', 123, 'test', 1234, 'user')";

        db.execSQL(crear_user);

        //Sensores
        final String iluminacion = "INSERT INTO "+ Utilidades.TABLA_SENSOR+" ("
                +Utilidades.SENSOR_TIPO+", "
                +Utilidades.SENSOR_UMBRAL+") VALUES ('iluminacion', 100)";

        db.execSQL(iluminacion);

        final String gas = "INSERT INTO "+ Utilidades.TABLA_SENSOR+" ("
                +Utilidades.SENSOR_TIPO+", "
                +Utilidades.SENSOR_UMBRAL+") VALUES ('gas', 1)";

        db.execSQL(gas);

        final String movimiento = "INSERT INTO "+ Utilidades.TABLA_SENSOR+" ("
                +Utilidades.SENSOR_TIPO+", "
                +Utilidades.SENSOR_UMBRAL+") VALUES ('movimiento', 1)";

        db.execSQL(movimiento);

        final String temperatura = "INSERT INTO "+ Utilidades.TABLA_SENSOR+" ("
                +Utilidades.SENSOR_TIPO+", "
                +Utilidades.SENSOR_UMBRAL+") VALUES ('temperatura', 50)";

        db.execSQL(temperatura);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public void editarEdificio(Edificio edificio){

    }

}
