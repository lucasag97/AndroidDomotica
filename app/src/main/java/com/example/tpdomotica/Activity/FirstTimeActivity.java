package com.example.tpdomotica.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tpdomotica.R;

import java.util.Locale;

public class FirstTimeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_first_time);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.welcome));

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
                Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(login);
            }
        });
    }

    private void showDialog() {
        final String[] listItems  = {getResources().getString(R.string.español), getResources().getString(R.string.ingles), getResources().getString(R.string.frances), getResources().getString(R.string.italiano)};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(FirstTimeActivity.this);
        mBuilder.setTitle(getResources().getString(R.string.idioma));
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    setLocale("es");
                    recreate();
                }
                if (which == 1){
                    setLocale("en");
                    recreate();
                }
                if (which == 2){
                    setLocale("fr");
                    recreate();
                }
                if (which == 3){
                    setLocale("it");
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
        SharedPreferences pref = getSharedPreferences("MisPreferencias",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("lang", lang);
        editor.apply();
    }

    public void loadLocale(){
        SharedPreferences pref = getSharedPreferences("MisPreferencias",MODE_PRIVATE);
        String language = pref.getString("lang", "");
        setLocale(language);
    }
}
