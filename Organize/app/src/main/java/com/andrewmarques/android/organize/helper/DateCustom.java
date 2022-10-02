package com.andrewmarques.android.organize.helper;

import java.text.SimpleDateFormat;

/*
    Criado por: Andrew Marques Silva
    Github: https://github.com/AndrewMarques2018
    Linkedin: https://www.linkedin.com/in/andrewmarques2018
    Instagram: https://www.instagram.com/andrewmarquessilva
 */

public class DateCustom {

    public static String dataAtual (){
        long date = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataString = simpleDateFormat.format(date);
        return dataString;
    }

    public static String getDia(String data){

        String retornoData[] = data.split("/");
        String dia = retornoData[0];
        String mes = retornoData[1];
        String ano = retornoData[2];

        return dia;
    }

    public static String getMesAno(String data){

        String retornoData[] = data.split("/");
        String dia = retornoData[0];
        String mes = retornoData[1];
        String ano = retornoData[2];

        return mes + ano;
    }

    public static String dateSQLParseData (String data) {
        String retornoData[] = data.split("-");
        String ano = retornoData[0];
        String mes = retornoData[1];
        String dia = retornoData[2];

        return dia + "/" + mes + "/" + ano;
    }

    public static String getDateSQL (String data) {

        String retornoData[] = data.split("/");
        String dia = retornoData[0];
        String mes = retornoData[1];
        String ano = retornoData[2];

        return ano + "-" + mes + "-" + dia;

    }

    public static String parseMillisOfMesAno (Long dataMillis){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMyyyy");
        String dataString = simpleDateFormat.format(dataMillis);

        return dataString;
    }

}
