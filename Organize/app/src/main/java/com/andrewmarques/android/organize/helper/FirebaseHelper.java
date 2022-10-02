package com.andrewmarques.android.organize.helper;

import android.util.Log;

import androidx.annotation.NonNull;

import com.andrewmarques.android.organize.model.Movimentacao;
import com.andrewmarques.android.organize.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/*
    Criado por: Andrew Marques Silva
    Github: https://github.com/AndrewMarques2018
    Linkedin: https://www.linkedin.com/in/andrewmarques2018
    Instagram: https://www.instagram.com/andrewmarquessilva
 */

public class FirebaseHelper {

    private static FirebaseAuth auth = getAuth();
    private static DatabaseReference databaseReference = getDatabaseReference();
    private static DatabaseReference movimentacaoRef;
    private static DatabaseReference usuarioRef;
    public static ValueEventListener movimentacaoValueEventListener;
    public static ValueEventListener usuarioValueEventListener;

    public static boolean isCurrentUser (){

        if (auth.getCurrentUser() != null){
            return true;
        }
        return false;
    }

    public static void deletar (Movimentacao movimentacao) {

        movimentacaoRef = getMovimentacaoReference(DateCustom.getMesAno(movimentacao.getData()));
        movimentacaoRef.child(movimentacao.getIdMovimentacao()).removeValue();
    }

    public static DatabaseReference getMovimentacaoReference (String mesAnoSelecionado){

        String emailUser = auth.getCurrentUser().getEmail();
        String idUser = Base64Custom.codificarBase64(emailUser);
        return databaseReference.child("movimentacao").child(idUser).child(mesAnoSelecionado);

    }

    public static DatabaseReference getMovimentacaoReference (){

        String emailUser = auth.getCurrentUser().getEmail();
        String idUser = Base64Custom.codificarBase64(emailUser);
        return databaseReference.child("movimentacao").child(idUser);

    }

    public static DatabaseReference getUsuarioReference (){

        String emailUser = auth.getCurrentUser().getEmail();
        String idUser = Base64Custom.codificarBase64(emailUser);
        usuarioRef = databaseReference.child("usuarios").child(idUser);
        return usuarioRef;
    }

    public static void signOut () {

        if (auth.getCurrentUser() != null){
            auth.signOut();
        }
    }

    public static void atualizarUsuario ( Usuario usuario) {

       DatabaseReference usuarioReference = getUsuarioReference();
       usuarioReference.setValue(usuario);

    }

    public static void salvarMovimentacao ( Movimentacao movimentacao) {

        DatabaseReference movimentacaoReference = getMovimentacaoReference(DateCustom.getMesAno(movimentacao.getData()));
        movimentacaoReference
                .child(movimentacao.getIdMovimentacao())
                .setValue(movimentacao);
    }

    public static DatabaseReference getDatabaseReference(){

        if(databaseReference == null){
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }

        return databaseReference;
    }

    public static FirebaseAuth getAuth () {

        if(auth == null){
            auth = FirebaseAuth.getInstance();
        }

        return auth;
    }

    public static void stop() {
        try {
            usuarioRef.removeEventListener(usuarioValueEventListener);
            movimentacaoRef.removeEventListener(movimentacaoValueEventListener);
        } catch (Exception ignored) {

        }

    }

    public static void removeMovimentacaoEventListener() {
        try {
            movimentacaoRef.removeEventListener(movimentacaoValueEventListener);
        }catch (Exception ignored){

        }

    }

}
