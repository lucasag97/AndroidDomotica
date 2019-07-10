package com.example.tpdomotica.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tpdomotica.Activity.ContenedorActivity;
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
 * {@link EdificioFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EdificioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EdificioFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ArrayList<Edificio> listaEdificio;
    Button modificarEdificio,eliminarEdificio,ubicacionEdificio;
    TextView vistaVacia;
    ImageView img, alerta;
    RecyclerView recyclerEdificio;
    Activity activity;
    IComunicaFragment interfaceComunicaFragment;
    SharedPreferences pref;
    ConexionSQLite db;

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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        db = new ConexionSQLite(getActivity(), "db_domotica", null, 1);
        pref =  this.getActivity().getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);
        ArrayList<Edificio> listaEdificio;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View vista = inflater.inflate(R.layout.fragment_edificio, container, false);

        listaEdificio = new ArrayList<Edificio>();

        vistaVacia = (TextView) vista.findViewById(R.id.sms_lista_vacia);
        alerta = (ImageView) vista.findViewById(R.id.idImagenAlerta);

        modificarEdificio = (Button) vista.findViewById(R.id.modificarEdificio);
        eliminarEdificio = (Button) vista.findViewById(R.id.eliminarEdificio);
        ubicacionEdificio = (Button) vista.findViewById(R.id.ubicacionEdificio);
        img = (ImageView) vista.findViewById(R.id.idImagen);

        recyclerEdificio = vista.findViewById(R.id.recyclerID);

        consultarListaEdificio(listaEdificio);

        final AdaptadorEdificio adapter = new AdaptadorEdificio(listaEdificio);
        recyclerEdificio.setAdapter(adapter);
        recyclerEdificio.setLayoutManager(new LinearLayoutManager(getContext()));
        if(recyclerEdificio.getAdapter() != null){
            if(recyclerEdificio.getAdapter().getItemCount() == 0){
                noHayEdificios();
            }
        }
        adapter.addOnImgListener(new AdaptadorEdificio.IMyViewHolderClicksImg() {
                @Override
                public void onImgClick(View v, int position) { interfaceComunicaFragment.enviarEdificio(listaEdificio.get(position));
                }
            });

        adapter.addOnViewsListener(new AdaptadorEdificio.IMyViewHolderClicks() {
                @Override
                public void onButtonClick(View v, int position) {
                    interfaceComunicaFragment.modificarEdificio(listaEdificio.get(position));
                }
            });

        adapter.addOnDeleteListener(new AdaptadorEdificio.IMyViewHolderClickEliminar() {
            @Override
            public void onEliminarClick(View v, final int position) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle(getResources().getString(R.string.eliminar));
                alert.setMessage(getResources().getString(R.string.sure));
                alert.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SQLiteDatabase db_actual = db.getWritableDatabase();

                            int cont = Utilidades.edis.size();
                            int borrar = 0;
                            for (int i=0; i<cont; i++){
                                if(Utilidades.edis.get(i) == listaEdificio.get(position).getID()){
                                    borrar = i;
                                }
                            }

                            Utilidades.edis.remove(borrar);

                            String Query = "DELETE FROM edificio WHERE _id = "+listaEdificio.get(position).getID();
                            db_actual.execSQL(Query);

                            String Query1= "DELETE FROM edificio_sensor WHERE id_edificio = "+listaEdificio.get(position).getID();
                            db_actual.execSQL(Query1);

                            db_actual.close();
                            dialog.dismiss();

                            Intent intent = new Intent(getActivity(), ContenedorActivity.class);
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
                }
            });
        return vista;
    }

    private void noHayEdificios() {
        String mensaje = "No hay edificios asociados";
        vistaVacia.setText(mensaje);
        vistaVacia.setVisibility(View.VISIBLE);
        recyclerEdificio.setVisibility(View.GONE);
        alerta.setVisibility(View.VISIBLE);
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

    public void onCreateOptionMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu,menu);
    }
}
