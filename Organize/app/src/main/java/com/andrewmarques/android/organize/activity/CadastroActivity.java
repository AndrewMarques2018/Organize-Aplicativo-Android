package com.andrewmarques.android.organize.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.andrewmarques.android.organize.R;
import com.andrewmarques.android.organize.helper.Base64Custom;
import com.andrewmarques.android.organize.helper.FirebaseHelper;
import com.andrewmarques.android.organize.helper.MySharedPreferencs;
import com.andrewmarques.android.organize.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

/*
    Criado por: Andrew Marques Silva
    Github: https://github.com/AndrewMarques2018
    Linkedin: https://www.linkedin.com/in/andrewmarques2018
    Instagram: https://www.instagram.com/andrewmarquessilva
 */

public class CadastroActivity extends AppCompatActivity {

    private MySharedPreferencs mySharedPreferencs;

    private EditText campoNome, campoEmail, campoSenha;
    private Button btCadastrar;
    private ImageButton btMostrarSenha;
    private Usuario usuario;
    private boolean senhaVisivel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        campoNome = findViewById(R.id.txtCadastroNome);
        campoEmail = findViewById(R.id.txtCadastroEmail);
        campoSenha = findViewById(R.id.txtCadastroSenha);
        btCadastrar = findViewById(R.id.btCadastro);
        btMostrarSenha = findViewById(R.id.btVisualizarSenha);

        mySharedPreferencs = new MySharedPreferencs(getApplicationContext());

        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome, email, senha;
                nome = campoNome.getText().toString();
                senha = campoSenha.getText().toString();
                email = campoEmail.getText().toString();

                if(!nome.isEmpty() && !senha.isEmpty() && !email.isEmpty()){
                    usuario = new Usuario(nome, email, Base64Custom.codificarBase64(senha));
                    cadastrarUser();

                }else {
                    Toast.makeText(CadastroActivity.this,
                            "Preencha os campos",Toast.LENGTH_SHORT).show();
                }

            }
        });

        btMostrarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                senhaVisivel = !senhaVisivel;
                if (senhaVisivel){
                    campoSenha.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    btMostrarSenha.setImageResource(R.drawable.ic_baseline_visibility_24);

                }else {
                    campoSenha.setInputType(129);
                    btMostrarSenha.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                }
            }
        });
    }

    public void cadastrarUser (){

        FirebaseAuth auth = FirebaseHelper.getAuth();
        auth.createUserWithEmailAndPassword(usuario.getEmail(), Base64Custom.decodificarBase64(usuario.getSenha()))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String excecao = "";
                        if (task.isSuccessful()){
                            String idUser = Base64Custom.codificarBase64(usuario.getEmail());
                            usuario.setIdUser(idUser);
                            usuario.setDataModifica????o();

                            if (mySharedPreferencs.salvarUsuarioAtual(usuario)) {
                                FirebaseHelper.atualizarUsuario(usuario);
                            }

                            finish();
                        }else{
                            try {
                                throw task.getException();
                            }catch (FirebaseAuthWeakPasswordException e) {
                                excecao = "Digite uma senha mais forte";

                            }catch (FirebaseAuthInvalidCredentialsException e) {
                                excecao = "Por favor, digite um email v??lido";

                            }catch (FirebaseAuthUserCollisionException e) {
                                excecao = "Esta conta j?? foi cadastrada";

                            }catch (Exception e) {
                                excecao = "Erro ao cadastrar usuario: " + e.getMessage();
                                e.printStackTrace();
                            }

                            Toast.makeText(CadastroActivity.this,
                                    excecao, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}