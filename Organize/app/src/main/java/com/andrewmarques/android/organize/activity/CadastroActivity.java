package com.andrewmarques.android.organize.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.andrewmarques.android.organize.R;
import com.andrewmarques.android.organize.config.ConfigFirebase;
import com.andrewmarques.android.organize.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CadastroActivity extends AppCompatActivity {

    private EditText campoNome, campoEmail, campoSenha;
    private Button btCadastrar;
    private FirebaseAuth auth;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        campoNome = findViewById(R.id.txtCadastroNome);
        campoEmail = findViewById(R.id.txtCadastroEmail);
        campoSenha = findViewById(R.id.txtCadastroSenha);
        btCadastrar = findViewById(R.id.btCadastro);

        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome, email, senha;
                nome = campoNome.getText().toString();
                senha = campoSenha.getText().toString();
                email = campoEmail.getText().toString();

                if(!nome.isEmpty() && !senha.isEmpty() && !email.isEmpty()){
                    user = new User(nome, email, senha);
                    cadastrarUser();
                }else {
                    Toast.makeText(CadastroActivity.this,
                            "Preencha os campos",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void cadastrarUser (){
        auth = ConfigFirebase.getAuth();

        auth.createUserWithEmailAndPassword(user.getEmail(), user.getSenha())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(CadastroActivity.this,
                            "Sucesso ao cadastrar usuario", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(CadastroActivity.this,
                            "Erro ao cadastrar usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}