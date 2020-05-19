/**
 *  Created by devgato on 19/05/2020.
 */
package com.example.uptostreamv2.Classe;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileHtml {

    //Constuction du fichier HTML
    public static void writeStringAsFile(final String fileContents, String fileName, Context context) {
        try {
            FileWriter out = new FileWriter(new File(context.getCacheDir(), fileName));

            String stringTemp = "<body style='margin:0;padding:0;background-color:black'><iframe id=\"maframe\" width=\"100%\" height=\"100%\" src=\"https://uptostream.com/iframe/" + fileContents + "\" scrolling=\"no\" frameBorder=\"0\" allowfullscreen webkitallowfullscreen></iframe></body>";

            out.write(stringTemp);
            out.close();

        } catch (IOException e) {
            //Log.d("myTag", "Erreur de lecriture du fichier" + e);
        }
    }

    public static String readFileAsString(String fileName, Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        BufferedReader in = null;

        try {
            in = new BufferedReader(new FileReader(new File(context.getCacheDir(), fileName)));
            while ((line = in.readLine()) != null) stringBuilder.append(line);

        } catch (FileNotFoundException e) {
            Log.d("myTag", "le fichier na pas ete trouve" + e);
        } catch (IOException e) {
            Log.d("myTag", "impossible de lire le fichier" + e);
        }

        return stringBuilder.toString();
    }
}
