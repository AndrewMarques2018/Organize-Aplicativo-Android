package com.andrewmarques.android.organize.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.andrewmarques.android.organize.model.Movimentacao;
import com.andrewmarques.android.organize.model.Usuario;

import java.util.ArrayList;
import java.util.List;

/*
    Criado por: Andrew Marques Silva
    Github: https://github.com/AndrewMarques2018
    Linkedin: https://www.linkedin.com/in/andrewmarques2018
    Instagram: https://www.instagram.com/andrewmarquessilva
 */

public class UsuarioDAO implements InterfaceUsuarioDAO {

    private SQLiteDatabase escreve;
    private SQLiteDatabase le;

    public UsuarioDAO (Context context) {
        DBhelper db = new DBhelper( context );
        escreve = db.getWritableDatabase();
        le = db.getReadableDatabase();
    }

    @Override
    public boolean salvar(Usuario usuario) {

        ContentValues cv = new ContentValues();
        cv.put("idUsuario" , usuario.getIdUser());
        cv.put("nome" , usuario.getNome());
        cv.put("email" , usuario.getEmail());
        cv.put("senha" , usuario.getSenha());
        cv.put("receitaTotal" , usuario.getReceitaTotal());
        cv.put("despesaTotal" , usuario.getDespesaTotal());

        try {
            escreve.insert(DBhelper.NOME_TABELA_USUARIOS, null, cv);
        }catch (Exception e){
            Log.e( "INFO", "Erro ao salvar usuario: " + e.getMessage());
            return false;
        }


        return true;
    }

    @Override
    public boolean atualizar(Usuario usuario) {

        ContentValues cv = new ContentValues();
        cv.put("nome" , usuario.getNome());
        cv.put("email" , usuario.getEmail());
        cv.put("senha" , usuario.getSenha());
        cv.put("receitaTotal" , usuario.getReceitaTotal());
        cv.put("despesaTotal" , usuario.getDespesaTotal());

        String sqlQuery = "idUsuario = ?";
        String[] args = {usuario.getIdUser()};

        try {
            escreve.update(DBhelper.NOME_TABELA_USUARIOS, cv, sqlQuery, args);
        }catch (Exception e){
            Log.e( "INFO", "Erro ao atualizar usuario: " + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean deletar(Usuario usuario) {

        String sqlQuery = "idUsuario = ?";
        String[] args = {usuario.getIdUser()};

        try {
            escreve.delete(DBhelper.NOME_TABELA_USUARIOS, sqlQuery, args);
        }catch (Exception e){
            Log.e( "INFO", "Erro ao deletar usuario: " + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public List<Usuario> listar() {

        List<Usuario> usuarios = new ArrayList<>();

        String sql = " SELECT * FROM " + DBhelper.NOME_TABELA_USUARIOS +
                " ; " ;

        Cursor c = le.rawQuery(sql, null);

        while (c.moveToNext() ) {

            Usuario usuario = new Usuario();

            try {
                usuario.setIdUser( c.getString( c.getColumnIndexOrThrow("idUsuario") ) );
                usuario.setNome( c.getString( c.getColumnIndexOrThrow("nome") ) );
                usuario.setEmail( c.getString( c.getColumnIndexOrThrow("email") ) );
                usuario.setSenha( c.getString( c.getColumnIndexOrThrow("senha") ) );
                usuario.setReceitaTotal( c.getDouble( c.getColumnIndexOrThrow("receitaTotal") ) );
                usuario.setDespesaTotal( c.getDouble( c.getColumnIndexOrThrow("despesaTotal") ) );

            }catch (Exception e){
                throw e;
            }

            usuarios.add(usuario);

        }

        return usuarios;
    }
}
