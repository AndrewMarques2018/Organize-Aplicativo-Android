package com.andrewmarques.android.organize.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.andrewmarques.android.organize.R;
import com.andrewmarques.android.organize.config.ConfigFirebase;
import com.andrewmarques.android.organize.helper.Base64Custom;
import com.andrewmarques.android.organize.helper.DateCustom;
import com.andrewmarques.android.organize.model.Movimentacao;
import com.andrewmarques.android.organize.model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class DespesasActivity extends AppCompatActivity {

    private TextInputEditText campoData, campoDescricao, campoCategoria;
    private EditText campoValor;
    private Movimentacao movimentacao;
    private String keyMovimentacaoRecuperada;
    private DatabaseReference firebase = ConfigFirebase.getDatabaseReference();
    private FirebaseAuth auth = ConfigFirebase.getAuth();
    private Double despesaTotal;
    private Double despesaAtualizada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        campoData = findViewById(R.id.editDespesasData);
        campoCategoria = findViewById(R.id.editDespesasCategoria);
        campoDescricao = findViewById(R.id.editDespesasDescricao);
        campoValor = findViewById(R.id.editDespesasValor);

        // preencher data padrão, data atual
        campoData.setText(DateCustom.dataAtual());

        recuperarMovimentacao();
        recuperarDespesaTotal();
    }

    public void recuperarMovimentacao (){
        try {
            movimentacao = (Movimentacao) getIntent().getSerializableExtra("movimentacaoSelecionada");
            if (movimentacao != null){
                campoData.setText(movimentacao.getData());
                campoCategoria.setText(movimentacao.getCategoria());
                campoDescricao.setText(movimentacao.getDescricao());

                campoValor.setText(new DecimalFormat( "0.00" ).format(movimentacao.getValor()).replace(',', '.'));
                keyMovimentacaoRecuperada = movimentacao.getKey();
            }

        }catch (Exception ignored){
            // nao é uma edição!
        }
    }

    public void salvarDespesa (View view){

        if (movimentacao != null){
            if(validarCamposDespesas()){

                Double valorAtual = movimentacao.getValor();
                Double valorRecuperado = Double.parseDouble(campoValor.getText().toString());

                movimentacao.setValor( valorRecuperado );
                movimentacao.setCategoria(campoCategoria.getText().toString());
                movimentacao.setDescricao(campoDescricao.getText().toString());
                movimentacao.setData(campoData.getText().toString());

                despesaAtualizada = despesaTotal + valorRecuperado - valorAtual;
                atualizarDespesas(despesaAtualizada);

                movimentacao.atualizar(keyMovimentacaoRecuperada);
                finish();
            }
        }else
        if(validarCamposDespesas()){

            Double valorRecuperado = Double.parseDouble(campoValor.getText().toString());

            movimentacao = new Movimentacao();
            movimentacao.setValor( valorRecuperado );
            movimentacao.setCategoria(campoCategoria.getText().toString());
            movimentacao.setDescricao(campoDescricao.getText().toString());
            movimentacao.setData(campoData.getText().toString());
            movimentacao.setTipo("d");

            despesaAtualizada = despesaTotal + valorRecuperado;
            atualizarDespesas(despesaAtualizada);

            movimentacao.salvar();
            finish();
        }

    }

    public boolean validarCamposDespesas (){

        String txtValor = campoValor.getText().toString();
        String txtCategoria = campoCategoria.getText().toString();
        String txtDescricao = campoDescricao.getText().toString();
        String txtData = campoData.getText().toString();

        // validando se os campos estão vazios
        {
            if (txtValor.isEmpty()) {
                Toast.makeText(DespesasActivity.this,
                        "Valor não foi preenchido!",
                        Toast.LENGTH_SHORT).show();
                return false;
            }else

            if (txtData.isEmpty()) {
                Toast.makeText(DespesasActivity.this,
                        "Data não foi preenchido!",
                        Toast.LENGTH_SHORT).show();
                return false;
            }else

            if (txtCategoria.isEmpty()) {
                Toast.makeText(DespesasActivity.this,
                        "Categoria não foi preenchido!",
                        Toast.LENGTH_SHORT).show();
                return false;
            }else

            if (txtDescricao.isEmpty()) {
                campoDescricao.setText("Sem Descrição");
                return false;
            }
        }

        return true;
    }

    public void recuperarDespesaTotal (){

        String emailUser = auth.getCurrentUser().getEmail();
        String idUser = Base64Custom.codificarBase64(emailUser);
        DatabaseReference userRef = firebase.child("usuarios").child(idUser);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                despesaTotal = user.getDespesaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void atualizarDespesas (Double despesa){

        String emailUser = auth.getCurrentUser().getEmail();
        String idUser = Base64Custom.codificarBase64(emailUser);
        DatabaseReference userRef = firebase.child("usuarios").child(idUser);
        userRef.child("despesaTotal").setValue(despesa);
    }

}