package com.example.tpdomotica;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    IComunicaFragmentSensores interfaceComunicaFragmentSensores;
    Activity activity;

    ArrayList<Sensor> sensores = null;
    TextView textDescripcion;
    TextView iluminacion,movimiento,humo,temperatura;
    Button Sensores;

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
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_detalle_edificio, container, false);

        textDescripcion = (TextView) vista.findViewById(R.id.EdificioDirreccion);
        temperatura = (TextView) vista.findViewById(R.id.estado_temperatura);
        movimiento = (TextView) vista.findViewById(R.id.estado_movimiento);
        humo = (TextView) vista.findViewById(R.id.estado_humo);
        iluminacion = (TextView) vista.findViewById(R.id.estado_iluminacion);


        Sensores = (Button) vista.findViewById(R.id.verSensores);
        Sensores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle objecto = getArguments();
                Edificio edificio = (Edificio) objecto.getSerializable("objeto");
                interfaceComunicaFragment.enviarEdificioAsensor(edificio);
                /*Intent intent = new Intent(getActivity(),ContenedorSensoresActivity.class);
                startActivity(intent);*/
            }
        });




        Bundle objetoEdificio = getArguments();
        Edificio edificio = null;
        if (objetoEdificio != null){
            edificio = (Edificio) objetoEdificio.getSerializable("objeto");
            textDescripcion.setText(edificio.getDIRECCION());
            ArrayList<Sensor> sen = consultarSensores(edificio);
            for (Sensor x : sen){
                if(x.getTIPO().equals("temperatura")){
                    temperatura.setText("ON");
                    temperatura.setTextColor(Color.parseColor("#00FF00"));
                }
                if(x.getTIPO().equals("gas")){
                    humo.setText("ON");
                    humo.setTextColor(Color.parseColor("#00FF00"));
                }
                if(x.getTIPO().equals("movimiento")){
                    movimiento.setText("ON");
                    movimiento.setTextColor(Color.parseColor("#00FF00"));
                }
                if(x.getTIPO().equals("iluminacion")){
                    iluminacion.setText("ON");
                    iluminacion.setTextColor(Color.parseColor("#00FF00"));
                }

            }
        }
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
        Cursor cursor = db_actual.rawQuery("SELECT S._id, S.tipo, S.umbral FROM sensor S INNER JOIN edificio_sensor ES ON S._id = ES.id_sensor WHERE ES.id_edificio = "+edificio.getID(), null);
        int cont = cursor.getCount();
        if (cursor.moveToFirst()) {
            for (int i = 0; i<=cont-1;i++){
                Sensor sensor = new Sensor();
                sensor.setID(cursor.getInt(0));
                sensor.setTIPO(cursor.getString(1));
                sensor.setUMBRAL(cursor.getInt(2));

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
