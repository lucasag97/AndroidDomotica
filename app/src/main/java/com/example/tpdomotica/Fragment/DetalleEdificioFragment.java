package com.example.tpdomotica.Fragment;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tpdomotica.Activity.LoginActivity;
import com.example.tpdomotica.Adaptadores.AdaptadorSensor;
import com.example.tpdomotica.BaseDatos.ConexionSQLite;
import com.example.tpdomotica.Entidades.Edificio;
import com.example.tpdomotica.Entidades.Sensor;
import com.example.tpdomotica.Entidades.Servicio;
import com.example.tpdomotica.Interface.IComunicaFragment;
import com.example.tpdomotica.R;
import com.example.tpdomotica.Utilidades.Utilidades;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetalleEdificioFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetalleEdificioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetalleEdificioFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ConexionSQLite db;
    IComunicaFragment interfaceComunicaFragment;
    Activity activity;
    SharedPreferences pref;
    ArrayList<Sensor> sensores = null;
    TextView textDescripcion;
    RecyclerView recyclerSensores;

    public DetalleEdificioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetalleEdificioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetalleEdificioFragment newInstance(String param1, String param2) {
        DetalleEdificioFragment fragment = new DetalleEdificioFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new ConexionSQLite(this.getActivity(),"db_domotica",null,1);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        pref =  this.getActivity().getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_detalle_edificio, container, false);

        Toolbar mToolbar = vista.findViewById(R.id.toolbar1);
        mToolbar.setTitle(getString(R.string.detalle_edi));
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
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
                        editor.apply();
                        getActivity().stopService(new Intent(getActivity(), Servicio.class));
                        NotificationManager notificationManager = (NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.cancelAll();
                        startActivity(cerrar_sesion);
                        return true;
                }
                return false;
            }
        });

        textDescripcion = (TextView) vista.findViewById(R.id.EdificioDirreccion);
        recyclerSensores = (RecyclerView) vista.findViewById(R.id.recyclerIDsensoresActuales);

        Bundle objetoEdificio = getArguments();
        Edificio edificio = null;
        edificio = (Edificio) objetoEdificio.getSerializable("objeto");

        textDescripcion.setText(edificio.getDIRECCION());
        final ArrayList<Sensor>sensores = consultarSensores(edificio);

        recyclerSensores.setLayoutManager(new LinearLayoutManager(getContext()));
        AdaptadorSensor adapter = new AdaptadorSensor(sensores);

        recyclerSensores.setAdapter(adapter);
        adapter.setOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaceComunicaFragment.enviarSensor(sensores.get(recyclerSensores.getChildAdapterPosition(v)));
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
    public ArrayList<Sensor> consultarSensores(Edificio edificio){
        SQLiteDatabase db_actual = db.getReadableDatabase();
        ArrayList<Sensor> sensores = new ArrayList<>();
        //SELECT E._id, S.tipo, valor FROM edificio_sensor ES INNER JOIN edificio E ON ES.id_edificio = E._id INNER JOIN sensor S ON ES.id_sensor = S._id WHERE ES.valor > S.umbral AND E._id = 1
        Cursor cursor = db_actual.rawQuery("SELECT S._id, S.tipo, S.umbral,ES.valor FROM sensor S INNER JOIN edificio_sensor ES ON S._id = ES.id_sensor WHERE ES.id_edificio = "+edificio.getID(), null);
        int cont = cursor.getCount();
        if (cursor.moveToFirst()) {
            for (int i = 0; i<=cont-1;i++){
                Sensor sensor = new Sensor();
                sensor.setID(cursor.getInt(0));
                sensor.setTIPO(cursor.getString(1));
                sensor.setUMBRAL(cursor.getInt(2));
                sensor.setVALOR_ACTUAL(cursor.getInt(3));
                sensor.setID_EDI(edificio.getID());

                sensores.add(sensor);
                cursor.moveToNext();
            }
        }else{
            Toast.makeText(getActivity(),"error",Toast.LENGTH_SHORT).show();
        }
        return sensores;
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
