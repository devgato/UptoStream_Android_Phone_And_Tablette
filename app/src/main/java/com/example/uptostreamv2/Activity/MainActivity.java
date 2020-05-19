/**
 *  Created by devgato on 19/05/2020.
 */
package com.example.uptostreamv2.Activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.uptostreamv2.Classe.ApiRest;
import com.example.uptostreamv2.Classe.FilmItem;
import com.example.uptostreamv2.Classe.FilmItemAdapter;
import com.example.uptostreamv2.Classe.Preference;
import com.example.uptostreamv2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static MainActivity instance;
    public String urlIndex = "";
    public String urlTemp = "";
    public ApiRest apiRest = null;
    public ListView filmListView = null ;
    private FloatingActionButton btnBack = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = this;

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Afficher une icone dans la navbar
        ActionBar menu = getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);
        menu.setIcon(R.drawable.logo);

        //Cacher le titre
        menu.setDisplayShowTitleEnabled(false);

        //Initialisation de la liste
        filmListView = findViewById(R.id.film_List_View);

        //Initialisation du token de l'url pour lister les fichiers
        apiRest = new ApiRest(getApplicationContext());
        urlIndex = apiRest.ConstructURLindex(Preference.getPreference("token", getApplicationContext()));
        urlTemp = urlIndex;

        apiRest.getFileUtpobox(urlTemp);

        //Bouton retour
        btnBack = findViewById(R.id.fab);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                apiRest.getFileUtpobox(getUrlBack());

            }
        });

        //Clique sur element de la liste
        filmListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(filmListView.isFocused()) {

                    int i = filmListView.getSelectedItemPosition();
                    View viewItemList = ((ScrollView) ((LinearLayout) filmListView.getChildAt(i)).getChildAt(0)).getChildAt(0);
                    LinearLayout itemLayout = (LinearLayout)viewItemList;
                    itemLayout.callOnClick();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return true;
    }

    //Fonction pour le menu parametre
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent myIntent = new Intent(getApplicationContext(), PreferenceActivity.class);
            startActivityForResult(myIntent, 0);

            return true;
        }
        else if (id == R.id.action_refresh) {

            //ActualiseArborescence();
            urlTemp = urlIndex;
            apiRest.getFileUtpobox(urlTemp);
            return true;
        }
        else if (id == R.id.action_pageweb) {

            Intent myIntent = new Intent(getApplicationContext(), PageWebActivity.class);
            startActivityForResult(myIntent, 0);

            return true;
        }
        else if (id == R.id.action_logout) {

            Preference.clear(getApplicationContext());
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static MainActivity getInstance() {
        return instance;
    }

    //Fonction pour récupérer l'utl precedente pour le bouton retour
    public String getUrlBack() {

        if(!(this.urlTemp).equals(this.urlIndex)) {

            // Log.d("myTag", " this.urltemp "+this.urltemp);
            String urlback = this.urlTemp;

            //Condition pour le cas de la racine qui a 2 slash
            int posOfA = urlback.lastIndexOf('/');
            int posOfB = urlback.lastIndexOf("//");
            if(posOfA == posOfB+1)
            {
                urlback = urlback.substring(0, posOfA+1);
            }
            else{
                urlback = urlback.substring(0, posOfA);
            }

            this.urlTemp = urlback;
        }

        return this.urlTemp;
    }

    public String getUrltemp() {
        return this.urlTemp;
    }

    public void setUrltemp(String pUrlTemp) {
        this.urlTemp = pUrlTemp;
    }

    //Fonction pour afficher la liste des dossiers et des fichiers dans l'applicaiton
    public void ShowViewListFilesandFolders(JSONArray tabfolders, JSONArray tabFiles) {

        List<FilmItem> filmItemsList = new ArrayList<>();

        //Affichage de l'arborsence des dossiers
        if (tabfolders.length() > 0) {
            for (int i = 0; i < tabfolders.length(); i++) {

                JSONObject item = null;
                try {
                    item = tabfolders.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String foldername = null;
                try {
                    foldername = item.getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                filmItemsList.add(new FilmItem(foldername, ""));
            }
        }

        //Affichage de l'arborsence des fichiers
        if (tabFiles.length() > 0) {
            for (int i = 0; i < tabFiles.length(); i++) {

                JSONObject item = null;
                try {
                    item = tabFiles.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String filmname = null;
                try {
                    filmname = item.getString("file_name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String filmcode = null;
                try {
                    filmcode = item.getString("file_code");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Condition pour ne lister que les videos
                String transcoded = null;
                try {
                    transcoded = item.getString("transcoded");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(transcoded != "null") {
                    filmItemsList.add(new FilmItem(filmname, filmcode));
                }
            }
        }

        filmListView = findViewById(R.id.film_List_View);
        filmListView.setAdapter(new FilmItemAdapter(getApplicationContext(), filmItemsList));
    }
}
