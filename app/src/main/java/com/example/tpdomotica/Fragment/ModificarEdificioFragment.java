package com.example.tpdomotica.Fragment;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tpdomotica.Activity.ContenedorActivity;
import com.example.tpdomotica.Activity.LoginActivity;
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
    boolean ilu,gas,movi,temp;
    IComunicaFragment interfaceComunicaFragment;
    ConexionSQLite db;
    Button guardar;
    EditText dire, nombre;


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

        Toolbar mToolbar = vista.findViewById(R.id.toolbar);
        mToolbar.setTitle(getString(R.string.act_edi));
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
                        SharedPreferences pref = getActivity().getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);
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

        dire = vista.findViewById(R.id.direccion);

        nombre = vista.findViewById(R.id.edi_nombre);

        guardar = (Button) vista.findViewById(R.id.guardar);

        iluminacion = (CheckBox) vista.findViewById(R.id.edificio_iluminacion);
        iluminacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = ((CheckBox)v).isChecked();
                if(isChecked) {
                    ilu = true;
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

        Cursor c1 = db_actual.rawQuery("SELECT nombre, direccion FROM edificio WHERE _id = "+edificio.getID(), null);
        if (c1.moveToFirst()){
            nombre.setText(c1.getString(0));
            dire.setText(c1.getString(1));
        }

        Cursor cursor = db_actual.rawQuery("SELECT ES.id_sensor FROM edificio_sensor ES INNER JOIN edificio E ON E._id = ES.id_edificio WHERE ES.id_edificio = "+edificio.getID(),null);

        int cont = cursor.getCount();
        if (cursor.moveToFirst()) {
            for (int i = 0; i<=cont-1;i++){
                if(cursor.getInt(0) == 1){
                    iluminacion.setChecked(true);
                    ilu = true;
                }
                if(cursor.getInt(0) == 2){
                    gases.setChecked(true);
                    gas = true;
                }
                if(cursor.getInt(0) == 3){
                    movimiento.setChecked(true);
                    movi = true;
                }
                if (cursor.getInt(0) == 4){
                    temperatura.setChecked(true);
                    temp = true;
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
                boolean validado = false;
                ContentValues cont = new ContentValues();
                String dire_text =  dire.getText().toString();
                String nombre1 = nombre.getText().toString();
                if(!dire_text.equals(""))
                    cont.put(Utilidades.EDI_DIRECCION,dire.getText().toString());
                if (!nombre1.equals(""))
                    cont.put(Utilidades.EDI_NOMBRE, nombre1);
                if (!dire_text.equals("") && !nombre1.equals(""))
                    validado = true;
                if (!ilu && !gas && !movi && !temp)
                    validado = false;

                if (validado) {
                    db_actual.update(Utilidades.TABLA_EDIFICIO, cont, "_id="+finalEdificio.getID(), null);
                    cont.clear();

                    if (ilu == true) {
                        if (verificarCampoExistente(Utilidades.TABLA_EDIFICIO_SENSOR, Utilidades.TABLA_SENSOR, finalEdificio.getID(), 1) == false) {
                            values.put(Utilidades.ID_SENSOR, 1);
                            values.put(Utilidades.ID_EDIFICIO, finalEdificio.getID());
                            values.put(Utilidades.EDI_SENS_VALOR, "0");
                            db_actual.insert(Utilidades.TABLA_EDIFICIO_SENSOR, null, values);
                            values.clear();
                        }
                    } else {
                        ConexionSQLite db_consulta = new ConexionSQLite(getContext(), "db_domotica", null, 1);
                        SQLiteDatabase actual = db_consulta.getWritableDatabase();
                        String Query = "select * from edificio_sensor ES INNER JOIN sensor S ON S._id = ES.id_sensor WHERE (ES.id_edificio = 1) AND (ES.id_sensor = " + finalEdificio.getID() + ")";
                        Cursor cur = actual.rawQuery(Query, null);
                        if (cur.getCount() > 0) {
                            actual.execSQL("DELETE FROM edificio_sensor WHERE (id_sensor = 1) AND (id_edificio = " + finalEdificio.getID() + ")");
                        }

                    }

                    if (gas == true) {
                        if (verificarCampoExistente(Utilidades.TABLA_EDIFICIO_SENSOR, Utilidades.TABLA_SENSOR, finalEdificio.getID(), 2) == false) {
                            values.put(Utilidades.ID_SENSOR, 2);
                            values.put(Utilidades.ID_EDIFICIO, finalEdificio.getID());
                            values.put(Utilidades.EDI_SENS_VALOR, "0");
                            db_actual.insert(Utilidades.TABLA_EDIFICIO_SENSOR, null, values);
                            values.clear();
                        }
                    } else {
                        String Query = "DELETE FROM " + Utilidades.TABLA_EDIFICIO_SENSOR + " WHERE (" + Utilidades.ID_SENSOR + " = " + 2 + ") AND (" + Utilidades.ID_EDIFICIO + " = " + finalEdificio.getID() + ")";
                        db_actual.execSQL(Query);
                    }
                    if (movi == true) {
                        if (verificarCampoExistente(Utilidades.TABLA_EDIFICIO_SENSOR, Utilidades.TABLA_SENSOR, finalEdificio.getID(), 3) == false) {
                            values.put(Utilidades.ID_SENSOR, 3);
                            values.put(Utilidades.ID_EDIFICIO, finalEdificio.getID());
                            values.put(Utilidades.EDI_SENS_VALOR, "0");
                            db_actual.insert(Utilidades.TABLA_EDIFICIO_SENSOR, null, values);
                            values.clear();
                        }
                    } else {
                        String Query = "DELETE FROM " + Utilidades.TABLA_EDIFICIO_SENSOR + " WHERE (" + Utilidades.ID_SENSOR + " = " + 3 + ") AND (" + Utilidades.ID_EDIFICIO + " = " + finalEdificio.getID() + ")";
                        db_actual.execSQL(Query);
                    }

                    if (temp == true) {
                        if (verificarCampoExistente(Utilidades.TABLA_EDIFICIO_SENSOR, Utilidades.TABLA_SENSOR, finalEdificio.getID(), 4) == false) {
                            values.put(Utilidades.ID_SENSOR, 4);
                            values.put(Utilidades.ID_EDIFICIO, finalEdificio.getID());
                            values.put(Utilidades.EDI_SENS_VALOR, "0");
                            db_actual.insert(Utilidades.TABLA_EDIFICIO_SENSOR, null, values);
                            values.clear();
                        }
                    } else {
                        String Query = "DELETE FROM " + Utilidades.TABLA_EDIFICIO_SENSOR + " WHERE (" + Utilidades.ID_SENSOR + " = " + 4 + ") AND (" + Utilidades.ID_EDIFICIO + " = " + finalEdificio.getID() + ")";
                        db_actual.execSQL(Query);
                    }
                    Intent intent = new Intent(getActivity(), ContenedorActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
                else{
                    if (dire_text.equals("")) {
                        dire.setError(getResources().getString(R.string.no_empty));
                    }
                    if (nombre1.equals("")){
                        nombre.setError((getResources().getString(R.string.no_empty)));
                    }
                    if (!ilu && !gas && !movi && !temp){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(getResources().getString(R.string.please_sens))
                                .setCancelable(true)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
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
