package com.example.tpdomotica.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tpdomotica.R;
import com.example.tpdomotica.Utilidades.Utilidades;

import java.util.ArrayList;
import java.util.Locale;

public class FirstTimeActivity extends AppCompatActivity {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = getSharedPreferences("MisPreferencias",MODE_PRIVATE);
        editor = pref.edit();
        editor.putBoolean("firstTime", false);
        editor.commit();
        loadLocale();
        setContentView(R.layout.activity_first_time);


        toolbar = findViewById(R.id.firstToolbar);
        toolbar.setTitle(R.string.welcome);


        Button changeLang = findViewById(R.id.language);
        changeLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        TextView con = findViewById(R.id.continuar);
        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(login);
            }
        });
    }

    private void showDialog() {
        final String[] listItems  = {getResources().getString(R.string.español), getResources().getString(R.string.ingles), getResources().getString(R.string.frances), getResources().getString(R.string.italiano)};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(FirstTimeActivity.this);
        mBuilder.setTitle(getResources().getString(R.string.idioma));
        mBuilder.setSingleChoiceItems(listItems, Utilidades.item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    Utilidades.item = 0;
                    setLocale("es");
                    editor.putString("lang", "es");
                    editor.apply();
                    recreate();
                }
                if (which == 1){
                    Utilidades.item = 1;
                    setLocale("en");
                    editor.putString("lang", "en");
                    editor.apply();
                    recreate();
                }
                if (which == 2){
                    Utilidades.item = 2;
                    setLocale("fr");
                    editor.putString("lang", "fr");
                    editor.apply();
                    recreate();
                }
                if (which == 3){
                    Utilidades.item = 3;
                    setLocale("it");
                    editor.putString("lang", "it");
                    editor.apply();
                    recreate();
                }

                dialog.dismiss();;
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        //SharedPreferences pref = getSharedPreferences("MisPreferencias",MODE_PRIVATE);
        //SharedPreferences.Editor editor = pref.edit();
        editor.putString("lang", lang);
        editor.apply();
    }

    public void loadLocale(){
        String language = pref.getString("lang", "");
        setLocale(language);
    }
}
