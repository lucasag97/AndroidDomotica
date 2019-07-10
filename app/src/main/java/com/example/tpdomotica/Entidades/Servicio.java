package com.example.tpdomotica.Entidades;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import com.example.tpdomotica.BaseDatos.ConexionSQLite;
import com.example.tpdomotica.R;
import com.example.tpdomotica.Utilidades.Utilidades;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Servicio extends Service {

    final int ID_TEMP = 100;
    final int ID_GAS = 200;
    final int ID_MOV = 300;
    private SharedPreferences pref;
    private String idUser;
    private String CHANNEL_ID;
    Timer timer1;
    Timer timer2;
    private TimerTask doAsynchronousTask1;
    private TimerTask doAsynchronousTask2;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        pref = getSharedPreferences("MisPreferencias",MODE_PRIVATE);
        idUser = pref.getString("id", "999");
        final Handler handler = new Handler();
        timer1 = new Timer();
        timer2 = new Timer();
        doAsynchronousTask1 = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        //SELECT E._id, S.tipo, valor FROM edificio_sensor ES INNER JOIN edificio E ON ES.id_edificio = E._id INNER JOIN sensor S ON ES.id_sensor = S._id WHERE ES.valor > S.umbral AND E._id = 1
                        // Toast.makeText(getApplicationContext(), "Se muestra esto cada 15 segundos", Toast.LENGTH_LONG).show();
                        ConexionSQLite conn = new ConexionSQLite(getApplicationContext(), "db_domotica", null, 1);
                        SQLiteDatabase db = conn.getReadableDatabase();
                        Cursor cursor = db.rawQuery("SELECT E."+Utilidades.EDI_ID+", S."+Utilidades.SENSOR_TIPO+", E."+Utilidades.EDI_NOMBRE+","+Utilidades.EDI_SENS_VALOR+" FROM "+Utilidades.TABLA_EDIFICIO_SENSOR+" ES INNER JOIN "+Utilidades.TABLA_EDIFICIO+" E ON ES."+Utilidades.ID_EDIFICIO+" = E."+Utilidades.EDI_ID+" INNER JOIN "+Utilidades.TABLA_SENSOR+" S ON ES."+Utilidades.ID_SENSOR+" = S."+Utilidades.SENSOR_ID+" WHERE ES."+Utilidades.EDI_SENS_VALOR+" >= S."+Utilidades.SENSOR_UMBRAL+" AND E."+Utilidades.EDI_ID_USUARIO+" = "+idUser+" AND E."+Utilidades.EDI_ESTADO+" = 1", null);
                        boolean c = cursor.moveToFirst();
                        int cont = cursor.getCount();
                        if (c) {
                            for (int i = 0; i <= cont-1; i++) {
                                int valor = cursor.getInt(3);
                                String tipo = cursor.getString(1);
                                String nombre = cursor.getString(2);
                                int edi = cursor.getInt(0);
                                String mensaje = "";
                                int idM = 0;
                                switch (tipo) {
                                    case "temperatura":
                                        mensaje = "¡Se superó el umbral de " + tipo + " en el edificio " + nombre + "! ¡El valor es " + valor + "°C!";
                                        idM = ID_TEMP+edi;
                                        break;
                                    case "gas":
                                        mensaje = "¡Se ha detectado gas a través del sensor en el edificio " + nombre + "!";
                                        idM = ID_GAS+edi;
                                        break;
                                    case "movimiento":
                                        mensaje = "¡Se ha detectado movimiento a través del sensor en el edificio " + nombre + "!";
                                        idM = ID_MOV+edi;
                                        break;
                                }

                                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                                CHANNEL_ID = "channel1";

                                Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                        .setSmallIcon(R.mipmap.ic_launcher)
                                        .setContentTitle("Alerta en "+ nombre)
                                        .setContentText(mensaje)
                                        //.setLargeIcon(largeIcon)
                                        .setStyle(new NotificationCompat.BigTextStyle()
                                                .bigText(mensaje)
                                                .setBigContentTitle("Alerta en "+ nombre)
                                                .setSummaryText("Servicio en background"))
                                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                        .setColor(Color.BLUE)
                                        //.setContentIntent(contentIntent)
                                        .setAutoCancel(true)
                                        .setOnlyAlertOnce(true)
                                        //.addAction(R.mipmap.ic_launcher, "Toast", actionIntent)
                                        .build();

                                notificationManager.notify(idM, notification);

                                cursor.moveToNext();
                            }
                        }
                    }
                });
            }
        };
        doAsynchronousTask2 = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        //Toast.makeText(getApplicationContext(), "Se muestra esto cada 20 segundos", Toast.LENGTH_LONG).show();
                        ConexionSQLite conn = new ConexionSQLite(getApplicationContext(), "db_domotica", null, 1);
                        SQLiteDatabase db = conn.getWritableDatabase();

                        ArrayList<Integer> sens = new ArrayList<>();
                        Cursor sensores = db.rawQuery("SELECT S."+Utilidades.SENSOR_ID+" FROM "+Utilidades.TABLA_EDIFICIO_SENSOR+" ES INNER JOIN "+Utilidades.TABLA_SENSOR+" S ON ES."+Utilidades.ID_SENSOR+" = S."+Utilidades.SENSOR_ID+" INNER JOIN "+Utilidades.TABLA_EDIFICIO+" E ON ES."+Utilidades.ID_EDIFICIO+" = E."+Utilidades.EDI_ID+" WHERE E."+Utilidades.EDI_ID_USUARIO+" = "+idUser+" AND E."+Utilidades.EDI_ESTADO+" = 1",null);
                        if (sensores.moveToFirst()) {
                            for (int i = 0; i<sensores.getCount(); i++){
                                sens.add(sensores.getInt(0));
                                sensores.moveToNext();
                            }
                        }

                        int cant = Utilidades.edis.size();

                        for(int i = 0; i < cant; i++) {
                            Random r = new Random();
                            //Para temperatura
                            int temp_v = r.nextInt((60 - 5) + 1) + 5;

                            ContentValues cont = new ContentValues();
                            cont.put(Utilidades.EDI_SENS_VALOR, temp_v);
                            db.update(Utilidades.TABLA_EDIFICIO_SENSOR, cont, "id_sensor=4 and id_edificio="+Utilidades.edis.get(i), null);
                            cont.clear();

                            Cursor temp = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_HISTORICO + " H INNER JOIN "+Utilidades.TABLA_EDIFICIO+" E ON H."+Utilidades.ID_EDIFICIO_H+" = E."+Utilidades.EDI_ID+" WHERE " + Utilidades.ID_EDIFICIO_H + " = " + Utilidades.edis.get(i) + " AND " + Utilidades.ID_SENSOR_H + " = 4 AND E."+Utilidades.EDI_ESTADO+" = 1 ORDER BY " + Utilidades.HISTORICO_TIMESTAMP + " DESC", null);

                            if (sens.contains(4)) {
                                if (temp.getCount() < 5) {
                                    cont.put(Utilidades.ID_SENSOR_H, 4);
                                    cont.put(Utilidades.ID_EDIFICIO_H, Utilidades.edis.get(i));
                                    cont.put(Utilidades.HISTORICO_VALOR, temp_v);
                                    db.insert(Utilidades.TABLA_HISTORICO, null, cont);
                                    cont.clear();
                                } else {
                                    temp.moveToLast();
                                    String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                                    cont.put(Utilidades.HISTORICO_VALOR, temp_v);
                                    cont.put(Utilidades.HISTORICO_TIMESTAMP, timeStamp);
                                    db.update(Utilidades.TABLA_HISTORICO, cont, "_id=" + temp.getString(0), null);
                                    cont.clear();
                                }
                            }


                            //Para movimiento y gases
                            int val_mov = (int) Math.round(Math.random());
                            cont.put(Utilidades.EDI_SENS_VALOR, val_mov);
                            db.update(Utilidades.TABLA_EDIFICIO_SENSOR, cont, "id_sensor=3", null);
                            cont.clear();

                            Cursor mov = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_HISTORICO + " WHERE " + Utilidades.ID_EDIFICIO_H + " = " + Utilidades.edis.get(i) + " AND " + Utilidades.ID_SENSOR_H + " = 3 ORDER BY " + Utilidades.HISTORICO_TIMESTAMP + " DESC", null);

                            if (sens.contains(3)) {
                                if (mov.getCount() < 5) {
                                    cont.put(Utilidades.ID_SENSOR_H, 3);
                                    cont.put(Utilidades.ID_EDIFICIO_H, Utilidades.edis.get(i));
                                    cont.put(Utilidades.HISTORICO_VALOR, val_mov);
                                    db.insert(Utilidades.TABLA_HISTORICO, null, cont);
                                    cont.clear();
                                } else {
                                    mov.moveToLast();
                                    String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                                    cont.put(Utilidades.HISTORICO_VALOR, val_mov);
                                    cont.put(Utilidades.HISTORICO_TIMESTAMP, timeStamp);
                                    db.update(Utilidades.TABLA_HISTORICO, cont, "_id=" + mov.getString(0), null);
                                    cont.clear();
                                }
                            }

                            int val_gas = (int) Math.round(Math.random());
                            cont.put(Utilidades.EDI_SENS_VALOR, val_gas);
                            db.update(Utilidades.TABLA_EDIFICIO_SENSOR, cont, "id_sensor=2", null);
                            cont.clear();

                            Cursor gas = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_HISTORICO + " WHERE " + Utilidades.ID_EDIFICIO_H + " = " + Utilidades.edis.get(i) + " AND " + Utilidades.ID_SENSOR_H + " = 2 ORDER BY " + Utilidades.HISTORICO_TIMESTAMP + " DESC", null);

                            if (sens.contains(2)) {
                                if (gas.getCount() < 5) {
                                    cont.put(Utilidades.ID_SENSOR_H, 2);
                                    cont.put(Utilidades.ID_EDIFICIO_H, Utilidades.edis.get(i));
                                    cont.put(Utilidades.HISTORICO_VALOR, val_gas);
                                    db.insert(Utilidades.TABLA_HISTORICO, null, cont);
                                    cont.clear();
                                } else {
                                    gas.moveToLast();
                                    String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                                    cont.put(Utilidades.HISTORICO_VALOR, val_gas);
                                    cont.put(Utilidades.HISTORICO_TIMESTAMP, timeStamp);
                                    db.update(Utilidades.TABLA_HISTORICO, cont, "_id=" + temp.getString(0), null);
                                    cont.clear();
                                }
                            }
                        }

                        db.close();
                    }
                });
            }
        };
        timer1.schedule(doAsynchronousTask1, 0, 19000);
        timer2.schedule(doAsynchronousTask2,0, 18000);

        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        timer1.cancel();
        timer2.cancel();
        Toast.makeText(this, "Service destroyed by user.", Toast.LENGTH_LONG).show();
    }
}