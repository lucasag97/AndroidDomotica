package com.example.tpdomotica.Fragment;

import android.app.Activity;
import android.content.Context;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tpdomotica.Adaptadores.AdaptadorHistorial;
import com.example.tpdomotica.BaseDatos.ConexionSQLite;
import com.example.tpdomotica.Entidades.Historico;
import com.example.tpdomotica.Entidades.Sensor;
import com.example.tpdomotica.Interface.IComunicaFragment;
import com.example.tpdomotica.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetalleSensorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetalleSensorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetalleSensorFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    Activity activity;
    IComunicaFragment interfaceComunicaFragment;
    TextView Titulo,DatosActuales;
    RecyclerView recyclerSensores;
    ConexionSQLite db;

    public DetalleSensorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetalleSensorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetalleSensorFragment newInstance(String param1, String param2) {
        DetalleSensorFragment fragment = new DetalleSensorFragment();
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
        db = new ConexionSQLite(getActivity(),"db_domotica",null,1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista =  inflater.inflate(R.layout.fragment_detalle_sensor,container,false);

        SQLiteDatabase db_actual = db.getReadableDatabase();
        Titulo = (TextView) vista.findViewById(R.id.tituloSensor);
        DatosActuales = (TextView) vista.findViewById(R.id.datosActualesSensor);
        recyclerSensores = (RecyclerView) vista.findViewById(R.id.recyclerIDSensorDetalle);

        ArrayList<Historico> historial = new ArrayList<>();
        Sensor sensor = null;
        Bundle objetoSensor = getArguments();
        sensor = (Sensor) objetoSensor.getSerializable("sensor");
        Titulo.setText(sensor.getTIPO());

        int valor = sensor.getVALOR_ACTUAL();
        int umbral = sensor.getUMBRAL();
        switch (sensor.getTIPO()){
            case "temperatura":
                DatosActuales.setText(getResources().getString(R.string.temp_act) + sensor.getVALOR_ACTUAL() + "°C");
                break;
            case "gas":
                if(valor < umbral) {
                    DatosActuales.setText(getResources().getString(R.string.gas_lib));
                }else {
                    DatosActuales.setText(getResources().getString(R.string.gas_det));
                }
                break;
            case "movimiento":
                if(valor < umbral){
                    DatosActuales.setText(getResources().getString(R.string.no_mov));
                }else{
                    DatosActuales.setText(getResources().getString(R.string.mov_si));
                }
                break;
            case "iluminacion":
                DatosActuales.setText(getResources().getString(R.string.ilu_bien));
        }
        //select ES.id_sensor,ES.valor,ES.momento from historico_sensor ES  INNER JOIN sensor S ON S._id = ES.id_sensor WHERE (ES.id_edificio = 1) AND (ES.id_sensor =  4) ORDER BY ES.momento DESC LIMIT 5
        Cursor cursor = db_actual.rawQuery("SELECT * FROM historico_sensor HS INNER JOIN sensor S ON S._id = HS.id_sensor WHERE (HS.id_sensor = "+sensor.getID()+" ) AND (HS.id_edificio = "+sensor.getID_EDI()+" ) ORDER BY HS.momento DESC LIMIT 5",null);
        int cont = cursor.getCount();
        if (cursor.moveToFirst()) {
            for (int i = 0; i<=cont-1;i++){
                Historico historico = new Historico();
                historico.setHISTORICO_ID(cursor.getInt(0));
                historico.setID_SENSOR_H(cursor.getString(1));
                historico.setID_EDIFICIO_H(cursor.getString(2));
                historico.setHISTORICO_VALOR(cursor.getString(3));
                historico.setHISTORICO_TIMESTAMP(cursor.getString(4));
                historico.setHISTORICO_UMBRAL(sensor.getUMBRAL());
                historico.setHISTORICO_TIPO(sensor.getTIPO());

                historial.add(historico);
                cursor.moveToNext();
            }
        }else{
            Toast.makeText(getActivity(),Integer.toString(sensor.getID()),Toast.LENGTH_SHORT).show();
        }

        recyclerSensores.setLayoutManager(new LinearLayoutManager(getContext()));
        AdaptadorHistorial adapter = new AdaptadorHistorial(historial);
        recyclerSensores.setAdapter(adapter);
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
