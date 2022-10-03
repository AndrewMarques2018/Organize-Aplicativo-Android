package com.andrewmarques.android.organize.helper;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.andrewmarques.android.organize.model.Usuario;

public class MySharedPreferencs {

    private Context context;
    private Usuario usuarioAtual;
    private static final String USER_ATUAL_PREFERENCES = "USER_ATUAL_PREFERENCES";

    private SharedPreferences.OnSharedPreferenceChangeListener callback = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        }
    };

    public MySharedPreferencs(Context context) {
        this.context = context;

        usuarioAtual = new Usuario();

        SharedPreferences sp = context.getSharedPreferences(USER_ATUAL_PREFERENCES, MODE_PRIVATE);
        usuarioAtual.setIdUser(sp.getString("id_usuario_atual", null));
        usuarioAtual.setNome(sp.getString("nome_usuario_atual", null));
        usuarioAtual.setEmail(sp.getString("email_usuario_atual", null));
        usuarioAtual.setSenha(sp.getString("senha_usuario_atual", null));
        usuarioAtual.setReceitaTotal(sp.getFloat("receitaTotal_usuario_atual", 0f));
        usuarioAtual.setDespesaTotal(sp.getFloat("despesaTotal_usuario_atual", 0f));
        sp.getString("dataModificacao_usuario_atual", "");

        sp.registerOnSharedPreferenceChangeListener(callback);
    }

    public void finalizar () {
        SharedPreferences sp = context.getSharedPreferences(USER_ATUAL_PREFERENCES, MODE_PRIVATE);
        sp.unregisterOnSharedPreferenceChangeListener(callback);
    }

    public boolean salvarUsuarioAtual (Usuario usuario) {
        try {
            SharedPreferences sp = context.getSharedPreferences(USER_ATUAL_PREFERENCES, MODE_PRIVATE);

            SharedPreferences.Editor editor = sp.edit();
            editor.putString("id_usuario_atual", usuario.getIdUser());
            editor.putString("nome_usuario_atual", usuario.getNome());
            editor.putString("email_usuario_atual", usuario.getEmail());
            editor.putString("senha_usuario_atual", usuario.getSenha());
            editor.putFloat("receitaTotal_usuario_atual", usuario.getReceitaTotal());
            editor.putFloat("despesaTotal_usuario_atual", usuario.getDespesaTotal());
            editor.putString("dataModificacao_usuario_atual", usuario.getDataModificação());

            editor.commit();

        } catch (Exception e){
            Log.e("MySharedPreferences", "Erro ao salvar usuario: " + e.getMessage());
            return false;
        }

        return true;
    }

    public Usuario getUsuarioAtual (){

        return this.usuarioAtual;
    }

}
