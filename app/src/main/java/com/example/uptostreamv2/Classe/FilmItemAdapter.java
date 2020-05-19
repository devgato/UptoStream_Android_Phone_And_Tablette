/**
 *  Created by devgato on 19/05/2020.
 */
package com.example.uptostreamv2.Classe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import com.example.uptostreamv2.Activity.MainActivity;
import com.example.uptostreamv2.Activity.StreamingActivity;
import com.example.uptostreamv2.R;

public class FilmItemAdapter extends BaseAdapter {

    //fields
    private Context context;
    private List<FilmItem> filmItemList;
    private LayoutInflater inflater;

    //constructor
    public FilmItemAdapter(Context context, List<FilmItem> filmItemList) {
        this.context = context;
        this.filmItemList = filmItemList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return filmItemList.size();
    }

    @Override
    public FilmItem getItem(int position) {
        return filmItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.content_main, null);

        //Get information about item
        FilmItem currentItem = getItem(position);
        final String itemCode = currentItem.getCode();
        final String itemName = currentItem.getName();

        String nomImage = "";
        if (itemCode == "") {
            nomImage = "folder";
        } else {
            nomImage = "movie";
        }

        //Creation de l'imageview
        ImageView itemIconView = convertView.findViewById(R.id.item_icon);
        int redId = context.getResources().getIdentifier(nomImage, "mipmap", context.getPackageName());
        itemIconView.setImageResource(redId);

        //Creation du textview
        TextView itemNameView = convertView.findViewById(R.id.item_name);
        itemNameView.setText(itemName);

        //Integration dans dans le linearlayout
        LinearLayout item_layout = convertView.findViewById(R.id.item_layout);

        //onClick sur un un linear layout
        item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Si c'est un fichier videos
                if (itemCode != "") {

                    Intent intent = new Intent(context.getApplicationContext(), StreamingActivity.class);
                    Bundle param = new Bundle();

                    param.putString("code_film", itemCode); //Your id
                    intent.putExtras(param);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                //Si c'est un dossier
                else {
                    MainActivity monactiviteprincipal = MainActivity.getInstance();
                    String urlTemp = monactiviteprincipal.getUrltemp();

                    //Gestion cas racine
                    if (!urlTemp.substring(urlTemp.length() - 1).equalsIgnoreCase("/")) {
                        urlTemp = urlTemp + "/" + itemName;
                    } else {
                        urlTemp = urlTemp + itemName;
                    }

                    monactiviteprincipal.setUrltemp(urlTemp);
                    monactiviteprincipal.apiRest.getFileUtpobox(urlTemp);
                }
            }
        });


        return convertView;
    }

}
