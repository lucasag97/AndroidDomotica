package com.example.tpdomotica;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Switch switchRecordar;
    private Button btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login3);

        bindUI();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                if(login(email,password)){
                    goToMain();
                }
            }
        });
    }

    private void bindUI(){
        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        switchRecordar = (Switch) findViewById(R.id.switchRecordar);
        btnLogin = (Button) findViewById(R.id.btnLogin);
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
}
