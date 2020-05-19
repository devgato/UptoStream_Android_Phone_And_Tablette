/**
 *  Created by devgato on 19/05/2020.
 */
package com.example.uptostreamv2.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.uptostreamv2.Classe.Preference;
import com.example.uptostreamv2.R;

public class PreferenceActivity extends AppCompatActivity {

    Switch autoPlay = null;
    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.param_view);

        //Afficher une icone dans la navbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar menu = getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);
        menu.setIcon(R.drawable.logo);

        //Cacher le titre
        menu.setDisplayShowTitleEnabled(false);

        //Instanciation Preference
        mPreferences = getApplicationContext().getSharedPreferences("prefUptobox", MODE_PRIVATE);

        //Chargement AutoPlay
        autoPlay = (Switch) findViewById(R.id.auto_play);
        autoPlay.setChecked(mPreferences.getBoolean("autoplay", false));

        //Save
        Button btnSave = findViewById(R.id.btn_param_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(mPreferences);

            }

        });

        //Annuler
        Button btnCancel = findViewById(R.id.btn_param_back);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }

        });
    }


    //Fonction pour sauvegarder les preferences
    public boolean save(SharedPreferences mPreferences) {

        Preference.saveAutoclik(mPreferences, autoPlay.isChecked());

        finish();
        Toast.makeText(MainActivity.getInstance(), "Preferences Save", Toast.LENGTH_SHORT).show();
        return true;

    }
}
