package com.example.tpdomotica.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.Toast;

import com.example.tpdomotica.Activity.ContenedorActivity;
import com.example.tpdomotica.Activity.LoginActivity;
import com.example.tpdomotica.Adaptadores.AdaptadorEdificio;
import com.example.tpdomotica.BaseDatos.ConexionSQLite;
import com.example.tpdomotica.Entidades.Edificio;
import com.example.tpdomotica.Interface.IComunicaFragment;
import com.example.tpdomotica.R;
import com.example.tpdomotica.Utilidades.Utilidades;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EdificiosPendientesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EdificiosPendientesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EdificiosPendientesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    RecyclerView recyclerPendientes;
    ConexionSQLite db;
    SharedPreferences pref;
    IComunicaFragment interfaceComunicaFragment;
    private Activity activity;

    public EdificiosPendientesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EdificiosPendientesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EdificiosPendientesFragment newInstance(String param1, String param2) {
        EdificiosPendientesFragment fragment = new EdificiosPendientesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        db = new ConexionSQLite(getContext(),"db_domotica",null,1);
        pref =  this.getActivity().getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View vista = inflater.inflate(R.layout.fragment_edificios_pendientes, container, false);

        Toolbar mToolbar = vista.findViewById(R.id.toolbar);
        mToolbar.setTitle(getString(R.string.edi_pendientes));
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        mToolbar.inflateMenu(R.menu.menu_refresh);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.configuracion:
                        return true;
                    case R.id.cerrar_sesion:
                        Intent cerrar_sesion = new Intent(getContext(), LoginActivity.class);
                        cerrar_sesion.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean("logged", false);
                        editor.remove("id");
                        Utilidades.edis.clear();
                        editor.commit();
                        //stopService(new Intent(getActivity(),Servicio.class));
                        //stopService(new Intent(getActivity(),Servicio.class));
                        startActivity(cerrar_sesion);
                        return true;
                }
                return false;
            }
        });

        recyclerPendientes = (RecyclerView) vista.findViewById(R.id.edi_pendientes);

        final ArrayList<Edificio> listaEdificio = new ArrayList<>();

        consultarListaEdificio(listaEdificio);

        AdaptadorEdificio adapter = new AdaptadorEdificio(listaEdificio);
        recyclerPendientes.setAdapter(adapter);
        recyclerPendientes.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter.setOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final View view = v;
                final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                PopupMenu popupMenu = new PopupMenu(getContext(),v);
                MenuInflater menuInflater = popupMenu.getMenuInflater();
                menuInflater.inflate(R.menu.menu_eliminacion,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.Eliminar:
                                dialog.setTitle(getResources().getString(R.string.eliminar));
                                dialog.setMessage(getResources().getString(R.string.sure));
                                dialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        SQLiteDatabase db_actual = db.getWritableDatabase();
                                        int cont = Utilidades.edis.size();
                                        int borrar = 1000;
                                        for (int i=0; i<cont; i++){
                                            if(Utilidades.edis.get(i) == listaEdificio.get(recyclerPendientes.getChildAdapterPosition(v)).getID()){
                                                borrar = i;
                                            }
                                        }

                                        if (borrar != 1000) {
                                            Utilidades.edis.remove(borrar);
                                        }
                                        String Query = "DELETE FROM edificio WHERE _id = " + listaEdificio.get(recyclerPendientes.getChildAdapterPosition(view)).getID();
                                        db_actual.execSQL(Query);

                                        String Query1 = "DELETE FROM edificio_sensor WHERE id_edificio = " + listaEdificio.get(recyclerPendientes.getChildAdapterPosition(view)).getID();
                                        db_actual.execSQL(Query1);

                                        db_actual.close();
                                        dialog.dismiss();

                                        Intent intent = new Intent(getActivity(), ContenedorActivity.class);
                                        getActivity().finish();
                                        startActivity(intent);
                                    }
                                });
                                dialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();
                                return true;
                        }
                        return true;
                    }
                });
                popupMenu.show();



            }
        });

        return vista;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

                if(edificio.getESTADO() == 0) {
                    listaEdificio.add(edificio);
                }
                cursor.moveToNext();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof Activity){
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

    /*@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu,v,menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_item_list,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.eliminarEdificio:
                interfaceComunicaFragment.irApendientes();
                return true;
        }
        return super.onContextItemSelected(item);
    }*/
}
