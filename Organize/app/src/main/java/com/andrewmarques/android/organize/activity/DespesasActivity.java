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
import com.andrewmarques.android.organize.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/*
    Criado por: Andrew Marques Silva
    Github: https://github.com/AndrewMarques2018
    Linkedin: https://www.linkedin.com/in/andrewmarques2018
    Instagram: https://www.instagram.com/andrewmarquessilva
 */

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

                movimentacao.deletar(keyMovimentacaoRecuperada);

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

    public boolean validarCamposDespesas () {

        String txtValor = campoValor.getText().toString();
        String txtCategoria = campoCategoria.getText().toString();
        String txtDescricao = campoDescricao.getText().toString();
        String txtData = campoData.getText().toString();

        // validando Valor digitado
        try {
            Double valorIsDouble = Double.parseDouble(txtValor);
        } catch (NullPointerException e) {
            Toast.makeText(DespesasActivity.this,
                    "Valor não foi preenchido!",
                    Toast.LENGTH_SHORT).show();
            return false;
        } catch (NumberFormatException e) {
            Toast.makeText(DespesasActivity.this,
                    "Formato incorreto: ex: 150.00",
                    Toast.LENGTH_SHORT).show();
            return false;
        } catch (Exception e) {
            Toast.makeText(DespesasActivity.this,
                    "Erro ao resgatar numero: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        // validando data digitada
        String dateFormat = "dd/MM/uuuu";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter
                .ofPattern(dateFormat)
                .withResolverStyle(ResolverStyle.STRICT);
        try {
            LocalDate date = LocalDate.parse(txtData, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            Toast.makeText(DespesasActivity.this,
                    "Data invalida",
                    Toast.LENGTH_SHORT).show();
            return false;
        }catch (Exception e) {
            Toast.makeText(DespesasActivity.this,
                    "Data: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
            throw e;
        }

        // validando categoria
        try {
            if (txtCategoria.isEmpty()){
                throw new NullPointerException();
            }
        }catch (NullPointerException e){
            Toast.makeText(DespesasActivity.this,
                    "Categoria não foi preenchido!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        if (txtDescricao.isEmpty()) {
            campoDescricao.setText("Sem Descrição");
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
                Usuario usuario = snapshot.getValue(Usuario.class);

                despesaTotal = usuario.getDespesaTotal();
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