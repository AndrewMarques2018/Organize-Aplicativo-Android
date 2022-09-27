package com.andrewmarques.android.organize.helper;

import java.text.SimpleDateFormat;

public class DateCustom {

    public static String dataAtual (){
        long date = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataString = simpleDateFormat.format(date);
        return dataString;
    }

    public static String mesAno (String data){

        String retornoData[] = data.split("/");
        String dia = retornoData[0];
        String mes = retornoData[1];
        String ano = retornoData[2];

        return mes + ano;
    }

    public static String parseMillisOfMesAno (Long dataMillis){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMyyyy");
        String dataString = simpleDateFormat.format(dataMillis);

        return dataString;
    }

}
