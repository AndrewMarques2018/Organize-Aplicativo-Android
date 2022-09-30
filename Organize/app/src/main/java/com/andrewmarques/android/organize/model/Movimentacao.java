package com.andrewmarques.android.organize.model;

import com.andrewmarques.android.organize.config.ConfigFirebase;
import com.andrewmarques.android.organize.helper.Base64Custom;
import com.andrewmarques.android.organize.helper.DateCustom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

/*
    Criado por: Andrew Marques Silva
    Github: https://github.com/AndrewMarques2018
    Linkedin: https://www.linkedin.com/in/andrewmarques2018
    Instagram: https://www.instagram.com/andrewmarquessilva
 */

public class Movimentacao implements Serializable, Comparable<Movimentacao>{

    private String data;
    private String categoria;
    private String descricao;
    private String tipo;
    private Double valor;
    private String key;
    private String fk_usuario;

    public Movimentacao() {
    }

    public void deletar (String key){

        FirebaseAuth auth = ConfigFirebase.getAuth();
        String idUser = Base64Custom.codificarBase64( auth.getCurrentUser().getEmail() );

        DatabaseReference firebase = ConfigFirebase.getDatabaseReference();
        firebase.child("movimentacao")
                .child(idUser)
                .child(DateCustom.getMesAno(data))
                .child(key)
                .removeValue();

    }

    public void atualizar (String key){

        FirebaseAuth auth = ConfigFirebase.getAuth();
        String idUser = Base64Custom.codificarBase64( auth.getCurrentUser().getEmail() );

        DatabaseReference firebase = ConfigFirebase.getDatabaseReference();
        firebase.child("movimentacao")
                .child(idUser)
                .child(DateCustom.getMesAno(data))
                .child(key)
                .setValue(this);

    }

    public void salvar (){

        FirebaseAuth auth = ConfigFirebase.getAuth();
        String idUser = Base64Custom.codificarBase64( auth.getCurrentUser().getEmail() );

        DatabaseReference firebase = ConfigFirebase.getDatabaseReference();
        firebase.child("movimentacao")
                .child(idUser)
                .child(DateCustom.getMesAno(data))
                .push()
                .setValue(this);

    }

    public String getFk_usuario() {
        return fk_usuario;
    }

    public void setFk_usuario(String fk_usuario) {
        this.fk_usuario = fk_usuario;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "Movimentacao{" +
                "data='" + data + '\'' +
                ", valor=" + valor +
                ", key='" + key + '\'' +
                '}';
    }

    @Override
    public int compareTo(Movimentacao o) {

        if (this.data.equals(o.getData())){
            return this.categoria.compareTo(o.getCategoria());
        }

        return this.data.compareTo(o.getData());

    }
}
