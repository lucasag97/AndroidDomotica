package com.example.tpdomotica.Adaptadores;

import android.content.Intent;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tpdomotica.Activity.ModificarEdificioActivity;
import com.example.tpdomotica.Entidades.Edificio;
import com.example.tpdomotica.Interface.IComunicaFragment;
import com.example.tpdomotica.R;

import java.util.ArrayList;

public class AdaptadorEdificio extends
        RecyclerView.Adapter<AdaptadorEdificio.EdificioViewHolder> implements View.OnClickListener {

    ArrayList<Edificio> ListaEdificio;
    private View.OnClickListener listener;
    IMyViewHolderClicks mListener;
    IMyViewHolderClicksImg Ilistener;
    IMyViewHolderClickEliminar Elistener;

    public AdaptadorEdificio(ArrayList<Edificio> listaEdificio){
        this.ListaEdificio = listaEdificio;
    }
    @Override
    public EdificioViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item,null,false);
        return new EdificioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EdificioViewHolder edificioViewHolder, int i) {
        final Edificio edificio = ListaEdificio.get(i);
        edificioViewHolder.Nombre.setText(edificio.getNOMBRE());
        edificioViewHolder.Informacion.setText(ListaEdificio.get(i).getDIRECCION());
        int state = edificio.getESTADO();
        if(state == 0){
            edificioViewHolder.estado.setText("Estado: Pendiente");
        }
        if(state == 1){
            edificioViewHolder.estado.setText("Estado: Aceptado");
        }
        if(state == 2){
            edificioViewHolder.estado.setText("Estado: Rechazado");
        }


    }

    @Override
    public int getItemCount() {
        return ListaEdificio.size();
    }

    public void setOnclickListener(View.OnClickListener listener){
        this.listener = listener;
    }
    public void addOnViewsListener(IMyViewHolderClicks listener){
        mListener = listener;
    }
    public void addOnImgListener(IMyViewHolderClicksImg ilistener){
        Ilistener = ilistener;
    }
    public void addOnDeleteListener(IMyViewHolderClickEliminar Elistener){
        this.Elistener =  Elistener;
    }

    @Override
    public void onClick(View view) {
        if(listener != null){
            listener.onClick(view);
        }
    }

    public class EdificioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView Nombre,Informacion,estado;
        Button modificar,eliminar,ubicar;
        ImageView img;
        public EdificioViewHolder(View itemView){
            super(itemView);
            Nombre = (TextView) itemView.findViewById(R.id.idNombre);
            Informacion = (TextView) itemView.findViewById(R.id.idInfo);
            estado = (TextView) itemView.findViewById(R.id.idEstado);
            img = (ImageView) itemView.findViewById(R.id.idImagen);
            img.setOnClickListener(this);
            modificar = (Button) itemView.findViewById(R.id.modificarEdificio);
            modificar.setOnClickListener(this);
            eliminar = (Button) itemView.findViewById(R.id.eliminarEdificio);
            eliminar.setOnClickListener(this);
            ubicar = (Button) itemView.findViewById(R.id.ubicacionEdificio);
            ubicar.setOnClickListener(this);
        }
        @Override
        public void onClick(View v){
            if(v.getId() == R.id.modificarEdificio){
                mListener.onButtonClick(v,getPosition());
            }
            if(v.getId() == R.id.idImagen){
                Ilistener.onImgClick(v,getPosition());
            }
            if(v.getId() == R.id.eliminarEdificio){
                Elistener.onEliminarClick(v,getPosition());
            }
        }
    }
    public interface IMyViewHolderClicks{
        void onButtonClick(View v,int position);
    }
    public interface IMyViewHolderClicksImg{
        void onImgClick(View v, int position);
    }
    public interface IMyViewHolderClickEliminar{
        void onEliminarClick(View v, int position);
    }
}
