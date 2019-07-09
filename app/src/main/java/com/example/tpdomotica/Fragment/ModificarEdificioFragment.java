package com.example.tpdomotica.Fragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.tpdomotica.BaseDatos.ConexionSQLite;
import com.example.tpdomotica.Entidades.Edificio;
import com.example.tpdomotica.Entidades.Sensor;
import com.example.tpdomotica.Interface.IComunicaFragment;
import com.example.tpdomotica.R;
import com.example.tpdomotica.Utilidades.Utilidades;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ModificarEdificioFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ModificarEdificioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModificarEdificioFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    CheckBox iluminacion,gases,movimiento,temperatura;
    boolean ilu,gas,movi,temp,dir;
    IComunicaFragment interfaceComunicaFragment;
    ConexionSQLite db;
    Button guardar;


    public ModificarEdificioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ModificarEdificioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ModificarEdificioFragment newInstance(String param1, String param2) {
        ModificarEdificioFragment fragment = new ModificarEdificioFragment();
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
        db  = new ConexionSQLite(getActivity(),"db_domotica",null,1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_modificar_edificio, container, false);

        guardar = (Button) vista.findViewById(R.id.guardar);

        iluminacion = (CheckBox) vista.findViewById(R.id.edificio_iluminacion);
        iluminacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = ((CheckBox)v).isChecked();
                if(isChecked) {
                    ilu = true;
                }else{
                    ilu = false;
                }
            }
        });

        gases = (CheckBox) vista.findViewById(R.id.edificio_gases);
        gases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = ((CheckBox)v).isChecked();
                if(isChecked){
                    gas = true;
                }else{
                    gas = false;
                }
            }
        });

        movimiento = (CheckBox) vista.findViewById(R.id.edificio_movimiento);
        movimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = ((CheckBox)v).isChecked();
                if(isChecked){
                    movi=true;
                }else {
                    movi = false;
                }
            }
        });

        temperatura = (CheckBox)  vista.findViewById(R.id.edificio_temperatura);
        temperatura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = ((CheckBox)v).isChecked();
                if(isChecked){
                    temp = true;
                }else{
                    temp = false;
                }
            }
        });

        Edificio edificio = null;
        Bundle objecto = getArguments();
        edificio = (Edificio) objecto.getSerializable("edificio");

        ArrayList<Sensor> sensores = new ArrayList<>();

        final SQLiteDatabase db_actual = db.getWritableDatabase();

        Cursor cursor = db_actual.rawQuery("SELECT ES.id_sensor FROM edificio_sensor ES INNER JOIN edificio E ON E._id = ES.id_edificio WHERE ES.id_edificio = "+edificio.getID(),null);

        int cont = cursor.getCount();
        if (cursor.moveToFirst()) {
            for (int i = 0; i<=cont-1;i++){
                if(cursor.getInt(0) == 1){
                    iluminacion.setChecked(true);
                }
                if(cursor.getInt(0) == 2){
                    gases.setChecked(true);
                }
                if(cursor.getInt(0) == 3){
                    movimiento.setChecked(true);
                }
                if (cursor.getInt(0) == 4){
                    temperatura.setChecked(true);
                }
                cursor.moveToNext();
            }
        }else{
            Toast.makeText(getActivity(),"error",Toast.LENGTH_SHORT).show();
        }
        final ContentValues values = new ContentValues();

        final Edificio finalEdificio = edificio;
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ilu == true){
                    if(verificarCampoExistente(Utilidades.TABLA_EDIFICIO_SENSOR,Utilidades.TABLA_SENSOR,finalEdificio.getID(),1) == false){
                        values.put(Utilidades.ID_SENSOR, 1);
                        values.put(Utilidades.ID_EDIFICIO, finalEdificio.getID());
                        values.put(Utilidades.EDI_SENS_VALOR, "0");
                        db_actual.insert(Utilidades.TABLA_EDIFICIO_SENSOR,null,values);
                        values.clear();
                    }
                }else{
                    if(verificarCampoExistente(Utilidades.TABLA_EDIFICIO_SENSOR,Utilidades.TABLA_SENSOR,finalEdificio.getID(),1) == false){

                    }else {
                        String Query = "DELETE FROM edificio_sensor WHERE (id_sensor = " + 1 + ") AND (id_edificio = " + finalEdificio.getID() + ")";
                        db_actual.execSQL(Query);
                    }
                }
                if(gas == true){
                    if(verificarCampoExistente(Utilidades.TABLA_EDIFICIO_SENSOR,Utilidades.TABLA_SENSOR,finalEdificio.getID(),2) == false){
                        values.put(Utilidades.ID_SENSOR,2);
                        values.put(Utilidades.ID_EDIFICIO,finalEdificio.getID());
                        values.put(Utilidades.EDI_SENS_VALOR,"0");
                        db_actual.insert(Utilidades.TABLA_EDIFICIO_SENSOR,null,values);
                        values.clear();
                    }
                }else{
                    if(verificarCampoExistente(Utilidades.TABLA_EDIFICIO_SENSOR,Utilidades.TABLA_SENSOR,finalEdificio.getID(),2) == false){

                    }else {
                        String Query = "DELETE FROM edificio_sensor WHERE (id_sensor = " + 2 + ") AND (id_edificio = " + finalEdificio.getID() + ")";
                        db_actual.execSQL(Query);
                    }
                }
                if(movi == true){
                    if(verificarCampoExistente(Utilidades.TABLA_EDIFICIO_SENSOR,Utilidades.TABLA_SENSOR,finalEdificio.getID(),3) == false){
                        values.put(Utilidades.ID_SENSOR,3);
                        values.put(Utilidades.ID_EDIFICIO,finalEdificio.getID());
                        values.put(Utilidades.EDI_SENS_VALOR,"0");
                        db_actual.insert(Utilidades.TABLA_EDIFICIO_SENSOR,null,values);
                        values.clear();
                    }
                }else{
                    if(verificarCampoExistente(Utilidades.TABLA_EDIFICIO_SENSOR,Utilidades.TABLA_SENSOR,finalEdificio.getID(),3) == false){

                    }else {
                        String Query = "DELETE FROM edificio_sensor WHERE (id_sensor = " + 3 + ") AND (id_edificio = " + finalEdificio.getID() + ")";
                        db_actual.execSQL(Query);
                    }
                }
                if(temp == true){
                    if(verificarCampoExistente(Utilidades.TABLA_EDIFICIO_SENSOR,Utilidades.TABLA_SENSOR,finalEdificio.getID(),4) == false){
                        values.put(Utilidades.ID_SENSOR,4);
                        values.put(Utilidades.ID_EDIFICIO,finalEdificio.getID());
                        values.put(Utilidades.EDI_SENS_VALOR,"0");
                        db_actual.insert(Utilidades.TABLA_EDIFICIO_SENSOR,null,values);
                        values.clear();
                    }
                }else{
                    if(verificarCampoExistente(Utilidades.TABLA_EDIFICIO_SENSOR,Utilidades.TABLA_SENSOR,finalEdificio.getID(),4) == false){

                    }else {
                        String Query = "DELETE FROM edificio_sensor WHERE (id_sensor = " + 4 + ") AND (id_edificio = " + finalEdificio.getID() + ")";
                        db_actual.execSQL(Query);
                    }
                }
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    public boolean verificarCampoExistente(String campo1,String campo2,int id_edificio, int id_sensor){
        ConexionSQLite db = new ConexionSQLite(getActivity(),"db_domotica",null,1);
        SQLiteDatabase db_actual = db.getReadableDatabase();
        String Query = "SELECT ES.id_sensor FROM "+campo1+" ES INNER JOIN "+campo2+" S ON S._id = ES.id_sensor WHERE (ES.id_sensor = "+id_sensor+") AND (ES.id_edificio = "+id_edificio+")";
        Cursor cursor = db_actual.rawQuery(Query,null);
        if(cursor.getCount() <= 0){
            cursor.close();
            db_actual.close();
            return false;
        }
        return true;
    }
    public boolean verificarCampoEliminar(String campo1,String campo2,int id_edificio,int id_sensor){
        ConexionSQLite db = new ConexionSQLite(getActivity(),"db_domotica",null,1);
        SQLiteDatabase db_actual = db.getReadableDatabase();
        String Query = "SElECT ES.id_sensor FROM "+campo1+" ES INNER JOIN "+campo2+" S ON S._id = ES.id_sensor WHERE (ES.id_sensor = "+id_sensor+") AND (ES.id_edificio = "+id_edificio+")";
        Cursor cursor = db_actual.rawQuery(Query,null);

        return true;
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
