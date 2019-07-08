package com.example.tpdomotica;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorSensor extends
        RecyclerView.Adapter<AdaptadorSensor.SensorViewHolder> implements View.OnClickListener {
    ArrayList<Sensor> ListaSensores;
    private View.OnClickListener listener;

    public AdaptadorSensor(ArrayList<Sensor> listaSensores){
        this.ListaSensores = listaSensores;
    }

    @Override
    public SensorViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_sensor,null,false);
        view.setOnClickListener(this);
        return new AdaptadorSensor.SensorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SensorViewHolder sensorViewHolder, int i) {
        sensorViewHolder.Nombre.setText("Sensor "+ListaSensores.get(i).getTIPO());

        switch (ListaSensores.get(i).getTIPO()){
            case "iluminacion":
                sensorViewHolder.Informacion.setText("Valor Actual: "+ListaSensores.get(i).getVALOR_ACTUAL());
                sensorViewHolder.icono.setImageResource(R.mipmap.ic_iluminacion_round);
                break;
            case "gas":
                sensorViewHolder.Informacion.setText("Valor Actual: "+ListaSensores.get(i).getVALOR_ACTUAL());
                sensorViewHolder.icono.setImageResource(R.mipmap.ic_humo_round);
                break;
            case "movimiento":
                sensorViewHolder.Informacion.setText("Valor Actual: "+ListaSensores.get(i).getVALOR_ACTUAL());
                sensorViewHolder.icono.setImageResource(R.mipmap.ic_movimiento_round);
                break;
            case "temperatura":
                sensorViewHolder.Informacion.setText("Valor Actual: "+ListaSensores.get(i).getVALOR_ACTUAL()+"°C");
                sensorViewHolder.icono.setImageResource(R.mipmap.ic_temperatura_round);
        }

    }

    @Override
    public int getItemCount() {
        return ListaSensores.size();
    }
    public void setOnclickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if(listener != null){
            listener.onClick(v);
        }
    }

    public class SensorViewHolder extends RecyclerView.ViewHolder {
        TextView Nombre,Informacion;
        ImageView icono;
        public SensorViewHolder(View itemView){
            super(itemView);
            Nombre = (TextView) itemView.findViewById(R.id.idNombre);
            Informacion = (TextView) itemView.findViewById(R.id.idInfo);
            icono = (ImageView) itemView.findViewById(R.id.idImagen);
        }
    }
}
