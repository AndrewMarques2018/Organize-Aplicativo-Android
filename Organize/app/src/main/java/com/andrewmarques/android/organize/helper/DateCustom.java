package com.andrewmarques.android.organize.helper;

import java.text.SimpleDateFormat;
import java.util.logging.SimpleFormatter;

public class DateCustom {

    public static String dataAtual (){
        long date = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataString = simpleDateFormat.format(date);
        return dataString;
    }

}
