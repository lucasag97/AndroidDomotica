package com.example.tpdomotica.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.example.tpdomotica.R;

import java.util.Locale;

public class SettingsActivity extends PreferenceActivity {

    static ListPreference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.toolbar, (ViewGroup)findViewById(android.R.id.content));
        Toolbar mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mToolbar.setTitle(getString(R.string.settings));
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ContenedorActivity.class);
                finish();
                startActivity(intent);
            }
        });

        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {

        SharedPreferences pref;
        SharedPreferences.Editor editor;

        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            pref = getActivity().getSharedPreferences("MisPreferencias",MODE_PRIVATE);
            editor = pref.edit();

            preference = (ListPreference)findPreference("language");
            final ListPreference list = (ListPreference) preference;

            list.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                public boolean onPreferenceChange(Preference preference, Object newValue) {

                    int index = list.findIndexOfValue(newValue.toString());

                    if (index == 0) {
                        setLocale("es");
                        editor.putString("lang", "es");
                        editor.apply();
                        getActivity().recreate();
                    }
                    if (index == 1) {
                        setLocale("en");
                        editor.putString("lang", "en");
                        editor.apply();
                        getActivity().recreate();
                    }
                    if (index == 2) {
                        setLocale("fr");
                        editor.putString("lang", "fr");
                        editor.apply();
                        getActivity().recreate();
                    }
                    if (index == 3) {
                        setLocale("it");
                        editor.putString("lang", "it");
                        editor.apply();
                        getActivity().recreate();
                    }
                    return true;
                }
            });

        }
        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            int horizontalMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
            int verticalMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
            int topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) getResources().getDimension(R.dimen.activity_vertical_margin) + 30, getResources().getDisplayMetrics());
            view.setPadding(horizontalMargin, topMargin, horizontalMargin, verticalMargin);
        }

        private void setLocale(String lang) {
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
            editor.putString("lang", lang);
            editor.apply();
        }

        public void loadLocale(){
            String language = pref.getString("lang", "");
            setLocale(language);
        }

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ContenedorActivity.class);
        finish();
        startActivity(intent);
    }

}
