package com.andrewmarques.android.organize.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.andrewmarques.android.organize.model.Movimentacao;

import java.util.ArrayList;
import java.util.List;

/*
    Criado por: Andrew Marques Silva
    Github: https://github.com/AndrewMarques2018
    Linkedin: https://www.linkedin.com/in/andrewmarques2018
    Instagram: https://www.instagram.com/andrewmarquessilva
 */

public class MovimentacaoDAO implements InterfaceMovimentacaoDAO {

    private SQLiteDatabase escreve;
    private SQLiteDatabase le;

    public MovimentacaoDAO(Context context) {

        DBhelper db = new DBhelper( context );
        escreve = db.getWritableDatabase();
        le = db.getReadableDatabase();

    }

    @Override
    public boolean salvar(Movimentacao movimentacao) {

        ContentValues cv = new ContentValues();
        cv.put("idMovimentacao" , movimentacao.getIdMovimentacao());
        cv.put("fk_idUsuario" , movimentacao.getFk_usuario());
        cv.put("mesAno" , DateCustom.getMesAno(movimentacao.getData()));
        cv.put("tipo" , movimentacao.getTipo());
        cv.put("dataMovimentacao" , DateCustom.getDateSQL(movimentacao.getData()));
        cv.put("valor" , movimentacao.getValor());
        cv.put("categoria" , movimentacao.getCategoria());
        cv.put("descricao" , movimentacao.getDescricao());

        try {
            escreve.insert(DBhelper.NOME_TABELA_MOVIMENTACOES, null, cv);
        }catch (Exception e){
            Log.e( "INFO", "Erro ao salvar movimentação: " + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean atualizar(Movimentacao movimentacao) {

        ContentValues cv = new ContentValues();
        cv.put("fk_idUsuario" , movimentacao.getFk_usuario());
        cv.put("mesAno" , DateCustom.getMesAno(movimentacao.getData()));
        cv.put("tipo" , movimentacao.getTipo());
        cv.put("dataMovimentacao" , DateCustom.getDateSQL(movimentacao.getData()));
        cv.put("valor" , movimentacao.getValor());
        cv.put("categoria" , movimentacao.getCategoria());
        cv.put("descricao" , movimentacao.getDescricao());

        String sqlQuery = "idMovimentacao = ?";
        String[] args = {movimentacao.getIdMovimentacao()};

        try {
            escreve.update(DBhelper.NOME_TABELA_MOVIMENTACOES, cv, sqlQuery, args);
        }catch (Exception e){
            Log.e( "INFO", "Erro ao atualizar movimentação: " + e.getMessage());
            return false;
        }

        return true;
    }

    public void put (Movimentacao movimentacao) {

        if (!exists(movimentacao.getIdMovimentacao())){
            salvar(movimentacao);
        }else{
            atualizar(movimentacao);
        }

    }

    public boolean exists (String id){

        String sql = " SELECT * FROM " + DBhelper.NOME_TABELA_MOVIMENTACOES +
                " WHERE idMovimentacao = ? " +
                " ; " ;
        String[] args = {id};

        Cursor c = le.rawQuery(sql, args);
        if (c.moveToNext()){
            if (!c.getString(c.getColumnIndexOrThrow("idMovimentacao")).isEmpty()){
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean deletar(Movimentacao movimentacao) {

        String sqlQuery = "idMovimentacao = ?";
        String[] args = {movimentacao.getIdMovimentacao()};

        try {
            escreve.delete(DBhelper.NOME_TABELA_MOVIMENTACOES, sqlQuery, args);
        }catch (Exception e){
            Log.e( "INFO", "Erro ao deletar movimentação: " + e.getMessage());
            return false;
        }

        return true;
    }

    public boolean limpar () {

        String sqlQuery = "";
        String[] args = {};

        try {
            escreve.delete(DBhelper.NOME_TABELA_MOVIMENTACOES, sqlQuery, args);
        }catch (Exception e){
            Log.e( "INFO", "limpar movimentações: " + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public List<Movimentacao> listar() {

        List<Movimentacao> movimentacoes = new ArrayList<>();

        String sql = " SELECT * FROM " + DBhelper.NOME_TABELA_MOVIMENTACOES +
                " ; " ;

        Cursor c = le.rawQuery(sql, null);

        while (c.moveToNext() ) {

            Movimentacao movimentacao = new Movimentacao();

            try {

                movimentacao.setData( DateCustom.dateSQLParseData(c.getString( c.getColumnIndexOrThrow("dataMovimentacao") ) ));
                movimentacao.setIdMovimentacao( c.getString( c.getColumnIndexOrThrow("idMovimentacao") ) );
                movimentacao.setFk_usuario( c.getString( c.getColumnIndexOrThrow("fk_idUsuario") ) );
                movimentacao.setTipo( c.getString( c.getColumnIndexOrThrow("tipo") ) );
                movimentacao.setValor( c.getFloat( c.getColumnIndexOrThrow("valor")));
                movimentacao.setCategoria( c.getString( c.getColumnIndexOrThrow("categoria") ) );
                movimentacao.setDescricao( c.getString( c.getColumnIndexOrThrow("descricao") ) );

            }catch (Exception e){
                throw e;
            }

            movimentacoes.add(movimentacao);

        }

        return movimentacoes;
    }

    public List<Movimentacao> listar( String mesAno) {

        List<Movimentacao> movimentacoes = new ArrayList<>();

        String sql = " SELECT * FROM " + DBhelper.NOME_TABELA_MOVIMENTACOES +
                " WHERE mesAno = ? " +
                " ; " ;
        String[] args = {mesAno};
        Cursor c = le.rawQuery(sql, args);

        while (c.moveToNext() ) {

            Movimentacao movimentacao = new Movimentacao();

            try {

                movimentacao.setData( DateCustom.dateSQLParseData(c.getString( c.getColumnIndexOrThrow("dataMovimentacao") ) ));
                movimentacao.setIdMovimentacao( c.getString( c.getColumnIndexOrThrow("idMovimentacao") ) );
                movimentacao.setFk_usuario( c.getString( c.getColumnIndexOrThrow("fk_idUsuario") ) );
                movimentacao.setTipo( c.getString( c.getColumnIndexOrThrow("tipo") ) );
                movimentacao.setValor( c.getFloat( c.getColumnIndexOrThrow("valor")));
                movimentacao.setCategoria( c.getString( c.getColumnIndexOrThrow("categoria") ) );
                movimentacao.setDescricao( c.getString( c.getColumnIndexOrThrow("descricao") ) );

            }catch (Exception e){
                throw e;
            }

            movimentacoes.add(movimentacao);

        }

        return movimentacoes;
    }
}
