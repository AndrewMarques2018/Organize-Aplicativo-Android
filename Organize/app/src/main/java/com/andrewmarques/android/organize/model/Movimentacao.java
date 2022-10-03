package com.andrewmarques.android.organize.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
    Criado por: Andrew Marques Silva
    Github: https://github.com/AndrewMarques2018
    Linkedin: https://www.linkedin.com/in/andrewmarques2018
    Instagram: https://www.instagram.com/andrewmarquessilva
 */

public class Movimentacao implements Serializable, Comparable<Movimentacao>{

    private String idMovimentacao;
    private String data;
    private String categoria;
    private String descricao;
    private String tipo;
    private Float valor;
    private String fk_usuario;

    private String dataModificação;

    public Movimentacao() {
    }

    public String getDataModificação() {
        return dataModificação;
    }

    public void setDataModificação() {
        Date dataAtual = new Date();
        this.dataModificação = new SimpleDateFormat("dd/MM/yyyy").format(dataAtual);
    }

    public String getFk_usuario() {
        return fk_usuario;
    }

    public void setFk_usuario(String fk_usuario) {
        this.fk_usuario = fk_usuario;
    }

    public String getIdMovimentacao() {
        return idMovimentacao;
    }

    public void setIdMovimentacao(String idMovimentacao) {
        this.idMovimentacao = idMovimentacao;
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

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "Movimentacao{" +
                "idMovimentacao='" + idMovimentacao + '\'' +
                ", data='" + data + '\'' +
                ", categoria='" + categoria + '\'' +
                ", descricao='" + descricao + '\'' +
                ", tipo='" + tipo + '\'' +
                ", valor=" + valor +
                ", fk_usuario='" + fk_usuario + '\'' +
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
