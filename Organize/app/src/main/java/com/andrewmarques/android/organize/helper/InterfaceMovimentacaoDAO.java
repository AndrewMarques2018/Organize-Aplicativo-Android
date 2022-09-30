package com.andrewmarques.android.organize.helper;

import com.andrewmarques.android.organize.model.Movimentacao;

import java.util.List;

/*
    Criado por: Andrew Marques Silva
    Github: https://github.com/AndrewMarques2018
    Linkedin: https://www.linkedin.com/in/andrewmarques2018
    Instagram: https://www.instagram.com/andrewmarquessilva
 */

public interface InterfaceMovimentacaoDAO {

    public boolean salvar ( Movimentacao movimentacao);

    public boolean atualizar ( Movimentacao movimentacao);

    public boolean deletar ( Movimentacao movimentacao);

    public List<Movimentacao> listar ();
}
