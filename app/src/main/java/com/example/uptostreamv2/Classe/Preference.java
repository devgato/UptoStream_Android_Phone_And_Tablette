/**
 *  Created by devgato on 19/05/2020.
 */
package com.example.uptostreamv2.Classe;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;

import com.example.uptostreamv2.Activity.MainActivity;
import com.example.uptostreamv2.Activity.MainEmptyActivity;
import com.example.uptostreamv2.R;

import static android.content.Context.MODE_PRIVATE;

public class Preference {

    //private SharedPreferences mPreferences;

    //Fonction pour sauvegarder les preferences
    static public boolean saveAutoclik(SharedPreferences mPreferences, Boolean autoplay) {

        SharedPreferences.Editor editor = mPreferences.edit();

        editor.putBoolean("autoplay", autoplay);

        // Save the changes in SharedPreferences
        editor.apply(); // commit changes

        return true;
    }

    //Fonction pour sauvegarder les preferences
    static public boolean save(SharedPreferences mPreferences, String token,String cookie_cfduid,String cookie_xfss,String cookie_video) {

        SharedPreferences.Editor editor = mPreferences.edit();

        //Insertion des valeurs dans l'editeur de preference
        editor.putBoolean("autoplay", true);
        editor.putString("token", token);  // Saving string
        editor.putString("cookie_cfduid", cookie_cfduid);  // Saving string
        editor.putString("cookie_xfss", cookie_xfss);  // Saving string
        editor.putString("cookie_video", cookie_video);  // Saving string

        // Save the changes in SharedPreferences
        editor.apply(); // commit changes

        return true;
    }

    //Fonction pour sauvegarder les preferences
    static public String getPreference(String key, Context context) {
        SharedPreferences mPreferences = context.getSharedPreferences("prefUptobox", MODE_PRIVATE);
        return mPreferences.getString(key, null);
    }

    //Fonction pour sauvegarder les preferences
    static public Boolean getPreferenceBool(String key, Context context) {
        SharedPreferences mPreferences = context.getSharedPreferences("prefUptobox", MODE_PRIVATE);
        return mPreferences.getBoolean(key, false);
    }

    //Fonction pour sauvegarder les preferences
    static public boolean setInit() {

        SharedPreferences mPreferences =  MainActivity.getInstance().getApplicationContext().getSharedPreferences("prefUptobox", MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();

        //Insertion des valeurs dans l'editeur de preference
        editor.putString("token", "");  // Saving string
        editor.putString("cookie_cfduid", "");  // Saving string
        editor.putString("cookie_xfss", "");  // Saving string
        editor.putString("cookie_video", "");  // Saving string

        // Save the changes in SharedPreferences
        editor.apply(); // commit changes

        return true;
    }

    //Fonction pour effacer toutes les preferences
    static public boolean clear(Context context) {

        SharedPreferences settings = context.getSharedPreferences("prefUptobox", Context.MODE_PRIVATE);
        settings.edit().clear().commit();

        return true;
    }
}
