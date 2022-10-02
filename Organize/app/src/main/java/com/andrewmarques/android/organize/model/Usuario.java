package com.andrewmarques.android.organize.model;

/*
    Criado por: Andrew Marques Silva
    Github: https://github.com/AndrewMarques2018
    Linkedin: https://www.linkedin.com/in/andrewmarques2018
    Instagram: https://www.instagram.com/andrewmarquessilva
 */

public class Usuario {

    private String idUser;
    private String nome;
    private String email;
    private String senha;
    private Float receitaTotal = 0.00f;
    private Float despesaTotal = 0.00f;

    public Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public Usuario() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public Float getReceitaTotal() {
        return receitaTotal;
    }

    public void setReceitaTotal(Float receitaTotal) {
        this.receitaTotal = receitaTotal;
    }

    public Float getDespesaTotal() {
        return despesaTotal;
    }

    public void setDespesaTotal(Float despesaTotal) {
        this.despesaTotal = despesaTotal;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idUser='" + idUser + '\'' +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                ", receitaTotal=" + receitaTotal +
                ", despesaTotal=" + despesaTotal +
                '}';
    }
}
