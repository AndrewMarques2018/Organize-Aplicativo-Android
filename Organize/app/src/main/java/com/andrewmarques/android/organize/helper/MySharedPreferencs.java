package com.andrewmarques.android.organize.helper;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.andrewmarques.android.organize.model.Movimentacao;
import com.andrewmarques.android.organize.model.Usuario;

import java.util.List;

/*
    Criado por: Andrew Marques Silva
    Github: https://github.com/AndrewMarques2018
    Linkedin: https://www.linkedin.com/in/andrewmarques2018
    Instagram: https://www.instagram.com/andrewmarquessilva
 */

public class MySharedPreferencs {

    private final Context context;
    private static Usuario usuarioAtual = new Usuario();
    private static final String USER_ATUAL_PREFERENCES = "USER_ATUAL_PREF";

    private final SharedPreferences.OnSharedPreferenceChangeListener callback = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        }
    };

    public MySharedPreferencs(@NonNull Context context) {
        this.context = context;

        SharedPreferences spUser = context.getSharedPreferences(USER_ATUAL_PREFERENCES, MODE_PRIVATE);
        usuarioAtual.setIdUser(spUser.getString("id", null));
        usuarioAtual.setNome(spUser.getString("nome", null));
        usuarioAtual.setEmail(spUser.getString("email", null));
        usuarioAtual.setSenha(spUser.getString("senha", null));
        usuarioAtual.setReceitaTotal(spUser.getFloat("receitaTotal", 0f));
        usuarioAtual.setDespesaTotal(spUser.getFloat("despesaTotal", 0f));
        usuarioAtual.setDataModificação();
        spUser.getString("dataModificacao", "");

        spUser.registerOnSharedPreferenceChangeListener(callback);
    }

    public void finalizar () {
        SharedPreferences sp = context.getSharedPreferences(USER_ATUAL_PREFERENCES, MODE_PRIVATE);
        sp.unregisterOnSharedPreferenceChangeListener(callback);
    }

    public boolean salvarUsuarioAtual (Usuario usuario) {
        try {

            SharedPreferences sp = context.getSharedPreferences(USER_ATUAL_PREFERENCES, MODE_PRIVATE);

            SharedPreferences.Editor editor = sp.edit();
            editor.putString("id", usuario.getIdUser());
            editor.putString("nome", usuario.getNome());
            editor.putString("email", usuario.getEmail());
            editor.putString("senha", usuario.getSenha());
            editor.putFloat("receitaTotal", usuario.getReceitaTotal());
            editor.putFloat("despesaTotal", usuario.getDespesaTotal());
            editor.putString("dataModificacao", usuario.getDataModificação());

            editor.commit();

        } catch (Exception e){
            Log.e("MySharedPreferences", "Erro ao salvar usuario: " + e.getMessage());
            return false;
        }

        return true;
    }

    public Usuario getUsuarioAtual (){

        SharedPreferences spUser = context.getSharedPreferences(USER_ATUAL_PREFERENCES, MODE_PRIVATE);
        usuarioAtual.setIdUser(spUser.getString("id", null));
        usuarioAtual.setNome(spUser.getString("nome", null));
        usuarioAtual.setEmail(spUser.getString("email", null));
        usuarioAtual.setSenha(spUser.getString("senha", null));
        usuarioAtual.setReceitaTotal(spUser.getFloat("receitaTotal", 0f));
        usuarioAtual.setDespesaTotal(spUser.getFloat("despesaTotal", 0f));
        usuarioAtual.setDataModificação(spUser.getString("dataModificacao", ""));
        spUser.registerOnSharedPreferenceChangeListener(callback);

        return this.usuarioAtual;
    }

}
