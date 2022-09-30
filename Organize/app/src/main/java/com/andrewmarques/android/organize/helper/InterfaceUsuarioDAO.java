package com.andrewmarques.android.organize.helper;

import com.andrewmarques.android.organize.model.Usuario;

import java.util.List;

/*
    Criado por: Andrew Marques Silva
    Github: https://github.com/AndrewMarques2018
    Linkedin: https://www.linkedin.com/in/andrewmarques2018
    Instagram: https://www.instagram.com/andrewmarquessilva
 */

public interface InterfaceUsuarioDAO {

    public boolean salvar ( Usuario usuario);

    public boolean atualizar ( Usuario usuario);

    public boolean deletar ( Usuario usuario);

    public List<Usuario> listar ();
}
