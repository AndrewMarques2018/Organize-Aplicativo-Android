package com.andrewmarques.android.organize.helper;

import android.util.Base64;

/*
    Criado por: Andrew Marques Silva
    Github: https://github.com/AndrewMarques2018
    Linkedin: https://www.linkedin.com/in/andrewmarques2018
    Instagram: https://www.instagram.com/andrewmarquessilva
 */

public class Base64Custom {

    public static String codificarBase64 (String txt){
        return Base64.encodeToString(txt.getBytes(), Base64.DEFAULT)
                .replaceAll("\\n|\\r",""); // remover espacos
    }

    public static String decodificarBase64 (String txtCodificado){
        return new String(Base64.decode(txtCodificado, Base64.DEFAULT));
    }
}
