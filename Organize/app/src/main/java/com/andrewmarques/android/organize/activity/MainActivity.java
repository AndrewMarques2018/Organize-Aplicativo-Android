package com.andrewmarques.android.organize.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.andrewmarques.android.organize.R;
import com.andrewmarques.android.organize.helper.FirebaseHelper;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

/*
    Criado por: Andrew Marques Silva
    Github: https://github.com/AndrewMarques2018
    Linkedin: https://www.linkedin.com/in/andrewmarques2018
    Instagram: https://www.instagram.com/andrewmarquessilva
 */

public class MainActivity extends IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        setButtonBackVisible(false);
        setButtonNextVisible(false);

        addSlide( new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_1)
                .build()
        );

        addSlide( new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_2)
                .build()
        );

        addSlide( new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_3)
                .build()
        );

        addSlide( new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_4)
                .build()
        );

        addSlide( new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_cadastro)
                .canGoForward(false) // impede o usuario de passar o slide
                .build()
        );

    }

    @Override
    protected void onStart() {
        super.onStart();
        verificarUsuarioLogado();
    }

    public void btEntrar (View view) {
        startActivity( new Intent( this, LoginActivity.class) );
    }

    public void btCadastrar (View view) {
        startActivity( new Intent( this, CadastroActivity.class) );
    }

    public void verificarUsuarioLogado (){

        if (FirebaseHelper.isCurrentUser()){
            abrirTelaPrincipal();
        }
    }

    public void abrirTelaPrincipal (){
        startActivity(new Intent(this, PrincipalActivity.class));
    }


}