package com.andrewmarques.android.organize.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

/*
    Criado por: Andrew Marques Silva
    Github: https://github.com/AndrewMarques2018
    Linkedin: https://www.linkedin.com/in/andrewmarques2018
    Instagram: https://www.instagram.com/andrewmarquessilva
 */

public class DBhelper extends SQLiteOpenHelper {

    public static int VERSION = 1;
    public static String NOME_DB = "DB_ORGANIZE";
    public static String NOME_TABELA_MOVIMENTACOES = "MOVIMENTACAO";

    public DBhelper(Context context) {
        super(context, NOME_DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sqlMovimentacoes =
                " CREATE TABLE IF NOT EXISTS " + NOME_TABELA_MOVIMENTACOES  +
                " ( idMovimentacao VARCHAR (100) NOT NULL PRIMARY KEY, " +
                " fk_idUsuario VARCHAR (100) NOT NULL, "+
                " mesAno CHAR (6) NOT NULL, " +
                " tipo CHAR (1) NOT NULL, " +
                " dataMovimentacao DATE NOT NULL, " +
                " valor FLOAT NOT NULL, " +
                " categoria VARCHAR (100) NOT NULL, " +
                " descricao TEXT NOT NULL " +
                " ); ";

        try {
            db.execSQL(sqlMovimentacoes);
        }catch (Exception e){
            Log.i("INFO_DB", "Erro ao criar a tabela de movimentacoes: " + e.getMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
