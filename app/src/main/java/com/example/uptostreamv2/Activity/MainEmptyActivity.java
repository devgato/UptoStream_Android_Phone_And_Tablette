/**
 *  Created by devgato on 19/05/2020.
 */
package com.example.uptostreamv2.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uptostreamv2.Classe.Preference;

public class MainEmptyActivity extends AppCompatActivity {

    private static MainEmptyActivity instance;
    private Intent activityIntent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        instance = this;

        String token = Preference.getPreference("token", this.getApplicationContext());
        Boolean connexionInternet = isNetworkAvailable();

        if (token != null && !token.isEmpty() && connexionInternet !=false) {

            activityIntent = new Intent(this, MainActivity.class);

        } else {

            activityIntent = new Intent(this, LoginActivity.class);

        }

        startActivity(activityIntent);

        finish();

    }

    public static MainEmptyActivity getInstance() {
        return instance;
    }

    //Fonction pour démarrer l'activity explorer depuis la classe loginfakeview.
    // Elle est appelé uniquement quand l'authentification a réussie.
    public void startActivityMain() {

        LoginActivity.getInstance().finish();
        activityIntent = new Intent(this, MainActivity.class);
        startActivity(activityIntent);

    }


//Fonction pour vérifier qu'il y a une connexion internet
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
