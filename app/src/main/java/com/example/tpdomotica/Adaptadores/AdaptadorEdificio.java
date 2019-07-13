package com.example.tpdomotica.Adaptadores;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tpdomotica.Entidades.Edificio;
import com.example.tpdomotica.R;

import java.util.ArrayList;

public class AdaptadorEdificio extends
        RecyclerView.Adapter<AdaptadorEdificio.EdificioViewHolder> implements View.OnClickListener,View.OnLongClickListener{

    ArrayList<Edificio> ListaEdificio;
    private View.OnClickListener listener;
    private View.OnLongClickListener listener1;
    private OnItemClickListener listenerM;
    private OnItemLongClickListener listenerN;
    private Context context;


    public AdaptadorEdificio(ArrayList<Edificio> listaEdificio){
        this.ListaEdificio = listaEdificio;
    }
    public void setContext(Context context){
        this.context = context;
    }
    public Context getContext(){
        return context;
    }
    @Override
    public EdificioViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item,null,false);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return new EdificioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EdificioViewHolder edificioViewHolder, int i) {
        final Edificio edificio = ListaEdificio.get(i);
        edificioViewHolder.Nombre.setText(edificio.getNOMBRE());
        edificioViewHolder.Informacion.setText(ListaEdificio.get(i).getDIRECCION());
    }

    @Override
    public int getItemCount() {
        return ListaEdificio.size();
    }

    public void setOnclickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    public void setOnLongClickListener(View.OnLongClickListener listener1){
        this.listener1 = listener1;
    }

     @Override
    public void onClick(View view) {
        if(listener != null){
            listener.onClick(view);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if(listener1 != null){
            listener1.onLongClick(v);
        }
        return true;
    }

    public class EdificioViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener,View.OnClickListener{
        TextView Nombre,Informacion;

        public EdificioViewHolder(View itemView){
            super(itemView);
            Nombre = (TextView) itemView.findViewById(R.id.idNombre);
            Informacion = (TextView) itemView.findViewById(R.id.idInfo);
        }

        @Override
        public void onClick(View v) {
            listenerM.onItemClicked(v,getPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            listenerN.onItemLongClicked(v,getPosition());
            return true;
        }
    }
    public interface OnItemClickListener{
        public void onItemClicked(View v,int position);
    }
    public interface  OnItemLongClickListener{
        public boolean onItemLongClicked(View v,int position);
    }
}
