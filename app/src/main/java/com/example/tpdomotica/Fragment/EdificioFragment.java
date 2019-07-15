package com.example.tpdomotica.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tpdomotica.Activity.ContenedorActivity;
import com.example.tpdomotica.Activity.EdificioActivity;
import com.example.tpdomotica.Activity.LoginActivity;
import com.example.tpdomotica.Activity.MainActivity;
import com.example.tpdomotica.Adaptadores.AdaptadorEdificio;
import com.example.tpdomotica.BaseDatos.ConexionSQLite;
import com.example.tpdomotica.Entidades.Edificio;
import com.example.tpdomotica.Entidades.Servicio;
import com.example.tpdomotica.Interface.IComunicaFragment;
import com.example.tpdomotica.R;
import com.example.tpdomotica.Utilidades.Utilidades;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EdificioFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EdificioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EdificioFragment extends Fragment implements PopupMenu.OnMenuItemClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ArrayList<Edificio> listaEdificio;
    ArrayList<Edificio> listaEdificioHabilitados = new ArrayList<>();
    TextView vistaVacia,tituloDinamico;
    ImageView alerta;
    RecyclerView recyclerEdificio;
    ImageButton new_edi;
    Activity activity;
    IComunicaFragment interfaceComunicaFragment;
    SharedPreferences pref;
    ConexionSQLite db;
    boolean cond_edificios_pendiente = false;

    public EdificioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EdificioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EdificioFragment newInstance(String param1, String param2) {
        EdificioFragment fragment = new EdificioFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        db = new ConexionSQLite(getActivity(), "db_domotica", null, 1);
        pref =  this.getActivity().getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);
        ArrayList<Edificio> listaEdificio;
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        final View vista = inflater.inflate(R.layout.fragment_edificio, container, false);

        Toolbar mToolbar = vista.findViewById(R.id.toolbar);
        mToolbar.setTitle(getString(R.string.edificios));
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        mToolbar.inflateMenu(R.menu.menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){

                    case R.id.verPendientes:
                        if(cond_edificios_pendiente){
                            interfaceComunicaFragment.irApendientes();
                            return true;
                        }else{
                            AlertDialog.Builder alerta = new AlertDialog.Builder(getContext());
                            alerta.setMessage(getContext().getResources().getString(R.string.SinPendientes));
                            alerta.setNegativeButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            alerta.show();
                            return true;
                        }
                    case R.id.menu_edificio:
                        Intent intent = new Intent(getContext(),EdificioActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.menu_settings:
                        return true;
                    case R.id.menu_logout:
                        Intent cerrar_sesion = new Intent(getContext(),LoginActivity.class);
                        cerrar_sesion.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean("logged", false);
                        editor.remove("id");
                        Utilidades.edis.clear();
                        editor.apply();
                        getActivity().stopService(new Intent(getActivity(),Servicio.class));
                        NotificationManager notificationManager = (NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.cancelAll();
                        startActivity(cerrar_sesion);
                        return true;
                }
                return true;
            }
        });
        new_edi = vista.findViewById(R.id.new_edi);
        new_edi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EdificioActivity.class);
                getActivity().finish();
                startActivity(intent);
            }
        });

        listaEdificio = new ArrayList<Edificio>();

        vistaVacia = (TextView) vista.findViewById(R.id.sms_lista_vacia);
        alerta = (ImageView) vista.findViewById(R.id.idImagenAlerta);
        tituloDinamico = (TextView) vista.findViewById(R.id.tituloDinamico);

        recyclerEdificio = vista.findViewById(R.id.recyclerID);

        consultarListaEdificio(listaEdificio);
        int cont = listaEdificio.size();
        ArrayList<Edificio> edificios = new ArrayList<>();
        for (int i = 0;i< cont ; i++){
            if(listaEdificio.get(i).getESTADO() == 1){
              edificios.add(listaEdificio.get(i));
            }
            if(listaEdificio.get(i).getESTADO() == 0){
                cond_edificios_pendiente = true;
            }
        }
        if (edificios.size() > 0){
            listaEdificio = edificios;
        }

        if(edificios.size() > 0){
            mToolbar.setTitle(getString(R.string.edi_activos));

        }else{
            mToolbar.setTitle(getString(R.string.edi_pendientes));
            mToolbar.getMenu().findItem(R.id.verPendientes).setVisible(false);

        }

        final AdaptadorEdificio adapter = new AdaptadorEdificio(listaEdificio);
        recyclerEdificio.setAdapter(adapter);
        recyclerEdificio.setLayoutManager(new LinearLayoutManager(getContext()));
        if(recyclerEdificio.getAdapter() != null){
            if(recyclerEdificio.getAdapter().getItemCount() == 0){
                //quitarTitulo();
                noHayEdificios();
                mToolbar.setTitle(R.string.edificios);
            }
        }
        if(comprobacionDeAprobados(listaEdificio)){
            registerForContextMenu(vista);
        }

        adapter.setOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listaEdificio.get(recyclerEdificio.getChildAdapterPosition(v)).getESTADO() == 1) {
                    interfaceComunicaFragment.enviarEdificio(listaEdificio.get(recyclerEdificio.getChildAdapterPosition(v)));
                }
            }
        });
        adapter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final View view = v;
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                PopupMenu popup = new PopupMenu(getContext(),v);
                MenuInflater inflater1 = popup.getMenuInflater();
                inflater1.inflate(R.menu.menu_item_list,popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.modificarEdificio:
                                interfaceComunicaFragment.modificarEdificio(listaEdificio.get(recyclerEdificio.getChildAdapterPosition(view)));
                                return true;
                            case R.id.eliminarEdificio:
                                AlertDialog.Builder alert = builder;
                                alert.setTitle(getResources().getString(R.string.eliminar));
                                alert.setMessage(getResources().getString(R.string.sure));
                                alert.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                SQLiteDatabase db_actual = db.getWritableDatabase();
                                int cont = Utilidades.edis.size();
                                int borrar = 1000;
                                for (int i=0; i<cont; i++){
                                    if(Utilidades.edis.get(i) == listaEdificio.get(recyclerEdificio.getChildAdapterPosition(view)).getID()){
                                    borrar = i;
                                    }
                                }

                                if (borrar != 1000) {
                                Utilidades.edis.remove(borrar);
                                }
                                if (Utilidades.edis.size() == 0){
                                    getActivity().stopService(new Intent(getActivity(), Servicio.class));
                                }
                                String Query = "DELETE FROM edificio WHERE _id = " + listaEdificio.get(recyclerEdificio.getChildAdapterPosition(view)).getID();
                                db_actual.execSQL(Query);

                                String Query1 = "DELETE FROM edificio_sensor WHERE id_edificio = " + listaEdificio.get(recyclerEdificio.getChildAdapterPosition(view)).getID();
                                db_actual.execSQL(Query1);

                                db_actual.close();
                                dialog.dismiss();

                                Intent intent = new Intent(getActivity(), ContenedorActivity.class);
                                getActivity().finish();
                                startActivity(intent);
                                }
                            });
                            alert.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                 @Override
                                 public void onClick(DialogInterface dialog, int which) {
                                      dialog.dismiss();
                                      }
                                 });
                                 alert.show();
                                return true;
                        }
                        return true;
                    }
                });
                popup.show();
                return true;
            }
        });
        return vista;
    }
    private void noHayEdificios() {
        String mensaje = getResources().getString(R.string.no_asoc);
        vistaVacia.setText(mensaje);
        vistaVacia.setVisibility(View.VISIBLE);
        recyclerEdificio.setVisibility(View.GONE);
        alerta.setVisibility(View.VISIBLE);
    }
    private boolean comprobacionDeAprobados(ArrayList<Edificio> listaEdificios){
        boolean comprobacion = false;
        for (int i = 0; i <listaEdificios.size() ; i ++){
            if(listaEdificios.get(i).getESTADO() == 1){
                comprobacion = true;
            }
        }
        return comprobacion;
    }

    private void consultarListaEdificio(ArrayList<Edificio> listaEdificio) {
        SQLiteDatabase db_actual = db.getReadableDatabase();
        //select * from edificio where id_usuario = ....
        Cursor cursor = db_actual.rawQuery("SELECT * FROM "+ Utilidades.TABLA_EDIFICIO+" WHERE "+Utilidades.EDI_ID_USUARIO+" = "+pref.getString("id","error"), null);
        int cont = cursor.getCount();
        if (cursor.moveToFirst()) {
            for (int i = 0; i<=cont-1;i++){
                Edificio edificio = new Edificio();
                edificio.setID(cursor.getInt(0));
                edificio.setNOMBRE(cursor.getString(1));
                edificio.setDIRECCION(cursor.getString(2));
                edificio.setDIRRECION_LAT(cursor.getString(3));
                edificio.setDIRECCION_LONG(cursor.getString(4));
                edificio.setESTADO(cursor.getInt(5));
                edificio.setID_USUARIO(cursor.getInt(6));

                listaEdificio.add(edificio);
                cursor.moveToNext();
            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof  Activity){
            this.activity = (Activity) context;
            interfaceComunicaFragment = (IComunicaFragment) this.activity;
        }
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.modificarEdificio:
                Toast.makeText(getContext(),"modificar",Toast.LENGTH_LONG).show();
                return true;
        }
        return true;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
