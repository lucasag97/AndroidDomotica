package com.example.tpdomotica.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tpdomotica.Adaptadores.AdaptadorSensor;
import com.example.tpdomotica.BaseDatos.ConexionSQLite;
import com.example.tpdomotica.Entidades.Edificio;
import com.example.tpdomotica.Entidades.Sensor;
import com.example.tpdomotica.Interface.IComunicaFragment;
import com.example.tpdomotica.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SensorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SensorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SensorFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ArrayList<Sensor> listaSensores;
    RecyclerView recyclerSensores;
    Activity activity;
    IComunicaFragment interfaceComunicaFragment;
    SharedPreferences pref;
    ConexionSQLite db;

    public SensorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SensorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SensorFragment newInstance(String param1, String param2) {
        SensorFragment fragment = new SensorFragment();
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
        db = new ConexionSQLite(getActivity(), "db_domotica", null, 1);
        pref =  this.getActivity().getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);
        ArrayList<Sensor> listaSensores;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_sensor, container, false);

        listaSensores = new ArrayList<Sensor>();
        recyclerSensores = vista.findViewById(R.id.recyclerIDsensor);
        recyclerSensores.setLayoutManager(new LinearLayoutManager(getContext()));

        consultarListaSensores(listaSensores);
        AdaptadorSensor adapter = new AdaptadorSensor(listaSensores);
        recyclerSensores.setAdapter(adapter);
        adapter.setOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Seleccionaste: " + listaSensores.get(recyclerSensores.getChildAdapterPosition(v)),Toast.LENGTH_LONG).show();
                interfaceComunicaFragment.enviarSensor(listaSensores.get(recyclerSensores.getChildAdapterPosition(v)));
            }
        });
        return vista;
    }
    private void consultarListaSensores(ArrayList<Sensor> listaSensores) {
        SQLiteDatabase db_actual = db.getReadableDatabase();
        Bundle objetoEdificio = getArguments();
        Edificio edificio = (Edificio) objetoEdificio.getSerializable("objeto");
        //select * from edificio where id_usuario = ....
        Cursor cursor = db_actual.rawQuery("SELECT S._id, S.tipo, S.umbral, ES.valor, ES.id_edificio FROM sensor S INNER JOIN edificio_sensor ES ON S._id = ES.id_sensor WHERE ES.id_edificio = "+edificio.getID(), null);

        int cont = cursor.getCount();
        if (cursor.moveToFirst()) {
            for (int i = 0; i<=cont-1;i++){
                Sensor sensor = new Sensor();
                sensor.setID(cursor.getInt(0));
                sensor.setTIPO(cursor.getString(1));
                sensor.setUMBRAL(cursor.getInt(2));
                sensor.setVALOR_ACTUAL(cursor.getInt(3));
                sensor.setID_EDI(cursor.getInt(4));

                listaSensores.add(sensor);
                cursor.moveToNext();
            }
        }else{
            Toast.makeText(getActivity(),"error",Toast.LENGTH_SHORT).show();
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
        if (context instanceof Activity){
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
}
