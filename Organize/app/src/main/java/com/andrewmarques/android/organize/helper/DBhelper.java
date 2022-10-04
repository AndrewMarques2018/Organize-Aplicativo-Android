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

    public static int VERSION = 2;
    public static String NOME_DB = "DB_ORGANIZE";
    public static String NOME_TABELA_MOVIMENTACOES = "MOVIMENTACAO";
    public static String NOME_TABELA_CALLBACK = "MOVIMENTACAO_CALBACK";

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

        String sqlCallBack =
                " CREATE TABLE IF NOT EXISTS " + NOME_TABELA_CALLBACK  +
                " ( " +
                " status CHAR (3) NOT NULL, " + // DEL- Deletada , ATU - atualizada
                " idMovimentacao VARCHAR (100) NOT NULL PRIMARY KEY, " +
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

        try {
            db.execSQL(sqlCallBack);
        }catch (Exception e){
            Log.i("INFO_DB", "Erro ao criar a tabela de movimentacoes update: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}
