package com.andrewmarques.android.organize.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.andrewmarques.android.organize.R;
import com.andrewmarques.android.organize.config.ConfigFirebase;
import com.andrewmarques.android.organize.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

/*
    Criado por: Andrew Marques Silva
    Github: https://github.com/AndrewMarques2018
    Linkedin: https://www.linkedin.com/in/andrewmarques2018
    Instagram: https://www.instagram.com/andrewmarquessilva
 */

public class LoginActivity extends AppCompatActivity {

    private EditText campoEmail, campoSenha;
    private Button btLogin;
    private FirebaseAuth auth;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        campoEmail = findViewById(R.id.txtLoginEmail);
        campoSenha = findViewById(R.id.txtLoginSenha);
        btLogin = findViewById(R.id.btLogin);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, senha;
                senha = campoSenha.getText().toString();
                email = campoEmail.getText().toString();

                if(!senha.isEmpty() && !email.isEmpty()){
                    usuario = new Usuario();
                    usuario.setEmail(email);
                    usuario.setSenha(senha);
                    loginUser();

                }else {
                    Toast.makeText(LoginActivity.this,
                            "Preencha os campos",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void loginUser () {

        auth = ConfigFirebase.getAuth();

        auth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            abrirTelaPrincipal();

                        } else {
                            String excecao = "";

                            try {
                                throw task.getException();
                            }catch (FirebaseAuthInvalidUserException e){
                                excecao = "Usuario não está cadastrado";
                                e.printStackTrace();
                            }catch (FirebaseAuthInvalidCredentialsException e){
                                excecao = "Email e senha não correspondem a um usário cadastrado";
                                e.printStackTrace();
                            }catch (Exception e){
                                excecao = "Erro ao fazer login: " + e.getMessage();
                                e.printStackTrace();
                            }

                            Toast.makeText(LoginActivity.this,
                                    excecao, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void abrirTelaPrincipal (){
        startActivity(new Intent(this, PrincipalActivity.class));
        finish();
    }
}