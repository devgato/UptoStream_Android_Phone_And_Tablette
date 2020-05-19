/**
 *  Created by devgato on 19/05/2020.
 */
package com.example.uptostreamv2.Classe;

public class FilmItem {

    //fields
    private String name;
    private String code;

    // constructeur
    public FilmItem(String name,  String code){
        this.name = name;
        this.code = code;

    }

    // methods

    public String getName() {return name; }

    public String getCode() {return code; }

}
