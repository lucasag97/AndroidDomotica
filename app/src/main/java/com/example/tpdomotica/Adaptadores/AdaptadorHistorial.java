package com.example.tpdomotica.Adaptadores;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.tpdomotica.Entidades.Historico;
import com.example.tpdomotica.R;

import java.util.ArrayList;

public class AdaptadorHistorial extends RecyclerView.Adapter<AdaptadorHistorial.HistorialViewHolder> implements View.OnClickListener {

    ArrayList<Historico> historial;
    private View.OnClickListener listener;

    public AdaptadorHistorial(ArrayList<Historico> historial){
        this.historial=historial;
    }

    @Override
    public HistorialViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_historico,null,false);
        view.setOnClickListener(this);
        return new AdaptadorHistorial.HistorialViewHolder(view);
    }

    @Override
    public void onBindViewHolder( AdaptadorHistorial.HistorialViewHolder historialViewHolder, int i) {
        int valor = Integer.parseInt(historial.get(i).getHISTORICO_VALOR());
        int umbral = historial.get(i).getHISTORICO_UMBRAL();
        String tipo = historial.get(i).getHISTORICO_TIPO();

        if(valor < umbral){
            historialViewHolder.img.setImageResource(R.mipmap.ic_afirmativo_foreground);
        }
        historialViewHolder.fecha.setText(historialViewHolder.valor.getContext().getResources().getString(R.string.suceso)+": "+historial.get(i).getHISTORICO_TIMESTAMP());
        switch(tipo){
            case "iluminacion":
                historialViewHolder.valor.setText(historialViewHolder.valor.getContext().getResources().getString(R.string.ilu_bien));
                break;
            case "gas":
                if (valor< umbral) {
                    historialViewHolder.valor.setText(historialViewHolder.valor.getContext().getResources().getString(R.string.gas_lib));
                }else {
                    historialViewHolder.valor.setText(historialViewHolder.valor.getContext().getResources().getString(R.string.gas_det));
                }
                break;
            case "movimiento":
                if (valor< umbral) {
                    historialViewHolder.valor.setText(historialViewHolder.valor.getContext().getResources().getString(R.string.no_mov));
                }else {
                    historialViewHolder.valor.setText(historialViewHolder.valor.getContext().getResources().getString(R.string.mov_si));
                }
                break;
            case "temperatura":
                if (valor< umbral) {
                    historialViewHolder.valor.setText(historialViewHolder.valor.getContext().getResources().getString(R.string.agradable)+" "+valor+"°C");
                }else {
                    historialViewHolder.valor.setText(historialViewHolder.valor.getContext().getResources().getString(R.string.alarmante)+" "+valor+"°C");
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return historial.size();
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

    public class HistorialViewHolder extends RecyclerView.ViewHolder {
        TextView fecha,valor;
        ImageView img;
        public HistorialViewHolder(View itemView){
            super(itemView);
            fecha = (TextView) itemView.findViewById(R.id.fecha);
            valor = (TextView) itemView.findViewById(R.id.valor);
            img = (ImageView) itemView.findViewById(R.id.idImagen);
        }
    }
}
