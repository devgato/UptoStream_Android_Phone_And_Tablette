/**
 *  Created by devgato on 19/05/2020.
 */
package com.example.uptostreamv2.Classe;

import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.uptostreamv2.Activity.MainActivity;

public class ApiRest {

    private Context context;

    // constructeur
    public ApiRest(Context context) {

        this.context = context;
        AndroidNetworking.initialize(context.getApplicationContext());

    }

    public void getFileUtpobox(String request) {

        AndroidNetworking.get(request)
                .build().
                getAsJSONObject(new JSONObjectRequestListener() {

                    JSONArray tabFiles = new JSONArray();
                    JSONArray tabFolders = new JSONArray();

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Récupération de la liste des folders
                            tabFolders = response.getJSONObject("data").getJSONArray("folders");

                            //Récupération de la liste des fichiers
                            tabFiles = response.getJSONObject("data").getJSONArray("files");

                            //Affichage de l'arborescence des fichiers et des dossiers dans la vue
                            MainActivity.getInstance().ShowViewListFilesandFolders(tabFolders, tabFiles);

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }

    public static String ConstructURLindex(String token) {
        return "https://uptobox.com/api/user/files?token=" + token + "&limit=50&path=//";
    }

}
