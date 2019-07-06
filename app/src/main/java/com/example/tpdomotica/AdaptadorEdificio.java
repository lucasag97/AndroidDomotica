package com.example.tpdomotica;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorEdificio extends
        RecyclerView.Adapter<AdaptadorEdificio.EdificioViewHolder> implements View.OnClickListener {

    ArrayList<Edificio> ListaEdificio;
    private View.OnClickListener listener;

    public AdaptadorEdificio(ArrayList<Edificio> listaEdificio){
        this.ListaEdificio = listaEdificio;
    }
    @Override
    public EdificioViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item,null,false);
        view.setOnClickListener(this);
        return new EdificioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EdificioViewHolder edificioViewHolder, int i) {
        int cont = i+1;
        edificioViewHolder.Nombre.setText("Edificio "+cont);
        edificioViewHolder.Informacion.setText(ListaEdificio.get(i).getDIRECCION());
        cont++;
    }

    @Override
    public int getItemCount() {
        return ListaEdificio.size();
    }

    public void setOnclickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener != null){
            listener.onClick(view);
        }
    }

    public class EdificioViewHolder extends RecyclerView.ViewHolder {
        TextView Nombre,Informacion;
        public EdificioViewHolder(View itemView){
            super(itemView);
            Nombre = (TextView) itemView.findViewById(R.id.idNombre);
            Informacion = (TextView) itemView.findViewById(R.id.idInfo);
        }
    }
}
