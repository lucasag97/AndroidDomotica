package com.example.tpdomotica;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences prefs;

    private EditText editTextEmail; //estos dos atributos tienen  que ser EditText para que el usuario pueda ingresar los datos
    private EditText editTextPassword;
    private Switch switchRecordar;
    private Button btnLogin;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login3);
        bindUI();
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        setCredentialsIfExist();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                if(login(email,password)){
                    goToMain();
                    saveOnPreferences(email,password);
                }
            }
        });
    }

    private void bindUI(){
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        switchRecordar = (Switch) findViewById(R.id.switchRecordar);
        btnLogin = (Button) findViewById(R.id.btnLogin);
    }

    public void setCredentialsIfExist(){
        String email = getUserMailPrefs();
        String password = getUserPasswordPrefs();
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty((password))){
            editTextEmail.setText(email);
            editTextPassword.setText(password);
        }
    }

    private boolean login(String email, String password){
        if (!isValidEmail(email)){
            Toast.makeText(this, "email no valido, intenta de nuevo", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(!isValidPassword(password)){
            Toast.makeText(this,"El password no es valido, ingresa mas caracteres", Toast.LENGTH_LONG).show();
            return false;
        }else{
            return true;
        }
    }
    private void saveOnPreferences(String email,String password){
        if(switchRecordar.isChecked()){
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("email",email);
            editor.putString("pass",password);
            editor.commit();
            editor.apply();
        }
    }
    private  boolean isValidEmail(String email){
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private boolean isValidPassword(String password){
        return password.length() > 4;
    }

    private void goToMain(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private String getUserMailPrefs(){
        return prefs.getString("email","");
    }
    private String getUserPasswordPrefs(){
        return prefs.getString("pass","");
    }


}
