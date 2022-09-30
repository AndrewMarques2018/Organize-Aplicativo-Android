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
    public static String NOME_TABELA_USUARIOS = "USUARIO";

    public DBhelper(Context context) {
        super(context, NOME_DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sqlUsuarios =
                " CREATE TABLE IF NOT EXISTS " + NOME_TABELA_USUARIOS  +
                " ( idUsuario VARCHAR (100) NOT NULL PRIMARY KEY, " +
                " nome VARCHAR (100) NOT NULL, " +
                " email VARCHAR (100) NOT NULL, " +
                " senha VARCHAR (50) NOT NULL, " +
                " receitaTotal DECIMAL (12,2) NOT NULL, " +
                " despesaTotal DECIMAL (12,2) NOT NULL " +
                " ); ";

        String sqlMovimentacoes =
                " CREATE TABLE IF NOT EXISTS " + NOME_TABELA_MOVIMENTACOES  +
                " ( idMovimentacao VARCHAR (100) NOT NULL PRIMARY KEY, " +
                " fk_idUsuario VARCHAR (100) NOT NULL, "+
                " mesAno CHAR (6) NOT NULL, " +
                " tipo CHAR (1) NOT NULL, " +
                " dataMovimentacao DATE NOT NULL, " +
                " valor DECIMAL (12,2) NOT NULL, " +
                " categoria VARCHAR (100) NOT NULL, " +
                " descricao TEXT NOT NULL " +
                " ); ";

        String sqlAssociacaoUsuarioMovimentacao =
                " ALTER TABLE " + NOME_TABELA_MOVIMENTACOES +
                " ADD CONSTRAINT fk_usuario_movimentacao " +
                " FOREIGN KEY ( fk_idUsuario ) " +
                " REFERENCES " + NOME_TABELA_USUARIOS + " ( idUsuario ) ";


        try {
            db.execSQL(sqlMovimentacoes);
        }catch (Exception e){
            Log.i("INFO_DB", "Erro ao criar a tabela de movimentacoes: " + e.getMessage());
        }

        try {
            db.execSQL(sqlUsuarios);
        }catch (Exception e){
            Log.i("INFO_DB", "Erro ao criar a tabela de usuarios: " + e.getMessage());
        }

        try {
            db.execSQL(sqlAssociacaoUsuarioMovimentacao);
        }catch (Exception e){
            Log.i("INFO_DB", "Erro ao criar associacao entre usuarios e tabelas: " + e.getMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
