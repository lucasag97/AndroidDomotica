package com.example.tpdomotica;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Servicio extends Service {

    final int ID_TEMP = 100;
    final int ID_GAS = 200;
    final int ID_MOV = 300;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final Handler handler = new Handler();
        Timer timer1 = new Timer();
        Timer timer2 = new Timer();
        TimerTask doAsynchronousTask1 = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        //SELECT E._id, S.tipo, valor FROM edificio_sensor ES INNER JOIN edificio E ON ES.id_edificio = E._id INNER JOIN sensor S ON ES.id_sensor = S._id WHERE ES.valor > S.umbral
                        //Toast.makeText(getApplicationContext(), "Se muestra esto cada 15 segundos", Toast.LENGTH_LONG).show();
                        ConexionSQLite conn = new ConexionSQLite(getApplicationContext(), "db_domotica", null, 1);
                        SQLiteDatabase db = conn.getReadableDatabase();
                        Cursor cursor = db.rawQuery("SELECT E."+Utilidades.EDI_ID+", S."+Utilidades.SENSOR_TIPO+", "+Utilidades.EDI_SENS_VALOR+" FROM "+Utilidades.TABLA_EDIFICIO_SENSOR+" ES INNER JOIN "+Utilidades.TABLA_EDIFICIO+" E ON ES."+Utilidades.ID_EDIFICIO+" = E."+Utilidades.EDI_ID+" INNER JOIN "+Utilidades.TABLA_SENSOR+" S ON ES."+Utilidades.ID_SENSOR+" = S."+Utilidades.SENSOR_ID+" WHERE ES."+Utilidades.EDI_SENS_VALOR+" >= S."+Utilidades.SENSOR_UMBRAL, null);
                        boolean c = cursor.moveToFirst();
                        int cont = cursor.getCount();
                        if (c) {
                            for (int i = 0; i <= cont-1; i++) {
                                int valor = cursor.getInt(2);
                                String tipo = cursor.getString(1);
                                int edi = cursor.getInt(0);
                                String mensaje = "";
                                int idM = 0;
                                switch (tipo) {
                                    case "temperatura":
                                        mensaje = "¡Se superó el umbral de " + tipo + " en el edificio " + edi + ". El valor es " + valor + "°C!";
                                        idM = ID_TEMP;
                                        break;
                                    case "gas":
                                        mensaje = "¡Se ha detectado gas a través del sensor en el edificio " + edi + "!";
                                        idM = ID_GAS;
                                        break;
                                    case "movimiento":
                                        mensaje = "¡Se ha detectado movimiento a través del sensor en el edificio " + edi + "!";
                                        idM = ID_MOV;
                                        break;
                                }
                                NotificationManager nm = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
                                Notification.Builder noti = new Notification.Builder(getApplicationContext());
                                noti.setContentTitle("ALERTA");
                                noti.setContentText(mensaje);
                                noti.setWhen(System.currentTimeMillis());
                                noti.setSmallIcon(R.mipmap.ic_launcher);
                                nm.notify(idM, noti.build());
                                cursor.moveToNext();
                            }
                        }
                    }
                });
            }
        };
        TimerTask doAsynchronousTask2 = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        //Toast.makeText(getApplicationContext(), "Se muestra esto cada 20 segundos", Toast.LENGTH_LONG).show();
                        ConexionSQLite conn = new ConexionSQLite(getApplicationContext(), "db_domotica", null, 1);
                        SQLiteDatabase db = conn.getWritableDatabase();
                        Random r = new Random();
                        //Para temperatura
                        int temp_v = r.nextInt(61);

                        ContentValues cont = new ContentValues();
                        cont.put(Utilidades.EDI_SENS_VALOR, temp_v);
                        db.update(Utilidades.TABLA_EDIFICIO_SENSOR, cont, "id_sensor=4", null);
                        cont.clear();

                        //Para movimiento y gases
                        int val_mov = (int)Math.round(Math.random());
                        cont.put(Utilidades.EDI_SENS_VALOR, val_mov);
                        db.update(Utilidades.TABLA_EDIFICIO_SENSOR, cont, "id_sensor=3", null);
                        cont.clear();

                        int val_gas = (int)Math.round(Math.random());
                        cont.put(Utilidades.EDI_SENS_VALOR, val_gas);
                        db.update(Utilidades.TABLA_EDIFICIO_SENSOR, cont, "id_sensor=2", null);
                        cont.clear();

                        db.close();
                    }
                });
            }
        };
        timer1.schedule(doAsynchronousTask1, 0, 15000);
        timer2.schedule(doAsynchronousTask2,0, 20000);

        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service destroyed by user.", Toast.LENGTH_LONG).show();
    }
}