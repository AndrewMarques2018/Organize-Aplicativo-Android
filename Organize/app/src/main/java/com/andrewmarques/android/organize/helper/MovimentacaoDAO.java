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
        cv.put("dataMovimentacao" , movimentacao.getData());
        cv.put("valor" , movimentacao.getValor());
        cv.put("categoria" , movimentacao.getCategoria());
        cv.put("descricao" , movimentacao.getDescricao());

        try {
            escreve.insert(DBhelper.NOME_TABELA_USUARIOS, "descricao", cv);
        }catch (Exception e){
            Log.e( "INFO", "Erro ao salvar usuario: " + e.getMessage());
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
        cv.put("dataMovimentacao" , movimentacao.getData());
        cv.put("valor" , movimentacao.getValor());
        cv.put("categoria" , movimentacao.getCategoria());
        cv.put("descricao" , movimentacao.getDescricao());

        String sqlQuery = "idUsuario = ?";
        String[] args = {movimentacao.getIdMovimentacao()};

        try {
            escreve.update(DBhelper.NOME_TABELA_USUARIOS, cv, sqlQuery, args);
        }catch (Exception e){
            Log.e( "INFO", "Erro ao salvar usuario: " + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean deletar(Movimentacao movimentacao) {

        String sqlQuery = "idUsuario = ?";
        String[] args = {movimentacao.getIdMovimentacao()};

        try {
            escreve.delete(DBhelper.NOME_TABELA_USUARIOS, sqlQuery, args);
        }catch (Exception e){
            Log.e( "INFO", "Erro ao salvar usuario: " + e.getMessage());
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
                movimentacao.setIdMovimentacao( c.getString( c.getColumnIndexOrThrow("idMovimentacao") ) );
                movimentacao.setFk_usuario( c.getString( c.getColumnIndexOrThrow("fk_idUsuario") ) );
                movimentacao.setData( c.getString( c.getColumnIndexOrThrow("idMovimentacao") ) );
                movimentacao.setTipo( c.getString( c.getColumnIndexOrThrow("idMovimentacao") ) );
                movimentacao.setValor( c.getDouble( c.getColumnIndexOrThrow("valor")));
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
