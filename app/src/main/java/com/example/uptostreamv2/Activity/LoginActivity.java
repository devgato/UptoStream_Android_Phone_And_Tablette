/**
 *  Created by devgato on 19/05/2020.
 */

package com.example.uptostreamv2.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.uptostreamv2.Classe.LoginFakeView;
import com.example.uptostreamv2.R;

public class LoginActivity extends AppCompatActivity {

    public LoginFakeView fakeView = null;
    private SharedPreferences mPreferences;
    private static LoginActivity instance;
    private ImageView gifLoading = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_view);

        //Afficher une icone dans la navbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar menu = getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);
        menu.setIcon(R.drawable.logo);

        //Cacher le titre
        menu.setDisplayShowTitleEnabled(false);

        instance = this;

        //Instanciation Preference
        mPreferences = getApplicationContext().getSharedPreferences("prefUptobox", MODE_PRIVATE);

        gifLoading = findViewById(R.id.gif_loading);

        //Bouton Login
        Button btnLogin = findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText edit_login = (EditText) findViewById(R.id.edit_login);
                EditText edit_password = (EditText) findViewById(R.id.edit_password);

                String sLogin = edit_login.getText().toString();
                String sPassword = edit_password.getText().toString();

                if (!sLogin.equalsIgnoreCase("") && !sPassword.equalsIgnoreCase("")) {
                    gifLoading.setVisibility(View.VISIBLE);
                    fakeView = new LoginFakeView(mPreferences);
                    fakeView.getCookieLoginFakeView(sLogin, sPassword);
                } else {
                    Toast.makeText(instance, "Empty Credentials", Toast.LENGTH_LONG).show();
                }

            }

        });

    }

    public static LoginActivity getInstance() {
        return instance;
    }

    //Fonction appelée si la connexion a échouée
    public void connexionFailed() {
        Toast.makeText(instance, "Authentification Failed", Toast.LENGTH_LONG).show();
        gifLoading.setVisibility(View.INVISIBLE);
    }

}
