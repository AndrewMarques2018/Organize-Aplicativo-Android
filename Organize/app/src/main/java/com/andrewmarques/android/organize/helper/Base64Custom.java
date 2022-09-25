package com.andrewmarques.android.organize.helper;

import android.util.Base64;

import java.nio.charset.StandardCharsets;

public class Base64Custom {

    public static String codificarBase64 (String txt){
        return Base64.encodeToString(txt.getBytes(), Base64.DEFAULT)
                .replaceAll("\\n|\\r",""); // remover espacos
    }

    public static String decodificarBase64 (String txtCodificado){
        return new String(Base64.decode(txtCodificado, Base64.DEFAULT));
    }
}
