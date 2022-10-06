package com.andrewmarques.android.organize.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.andrewmarques.android.organize.R;
import com.andrewmarques.android.organize.helper.DateCustom;
import com.andrewmarques.android.organize.helper.FirebaseHelper;
import com.andrewmarques.android.organize.helper.MovimentacaoDAO;
import com.andrewmarques.android.organize.helper.MySharedPreferencs;
import com.andrewmarques.android.organize.model.Movimentacao;
import com.andrewmarques.android.organize.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.UUID;

/*
    Criado por: Andrew Marques Silva
    Github: https://github.com/AndrewMarques2018
    Linkedin: https://www.linkedin.com/in/andrewmarques2018
    Instagram: https://www.instagram.com/andrewmarquessilva
 */

public class DespesasActivity extends AppCompatActivity {

    private MySharedPreferencs mySharedPreferencs;
    private MovimentacaoDAO movimentacaoDAO;
    private Movimentacao movimentacao;

    private TextInputEditText campoData, campoDescricao, campoCategoria;
    private EditText campoValor;
    private Float despesaTotal;
    private Float despesaAtualizada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        campoData = findViewById(R.id.editDespesasData);
        campoCategoria = findViewById(R.id.editDespesasCategoria);
        campoDescricao = findViewById(R.id.editDespesasDescricao);
        campoValor = findViewById(R.id.editDespesasValor);

        mySharedPreferencs = new MySharedPreferencs(getApplicationContext());
        movimentacaoDAO = new MovimentacaoDAO(getApplicationContext());

        // preencher data padrão, data atual
        campoData.setText(DateCustom.mesAnoToDate((String) getIntent().getSerializableExtra("mesAno")));

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
            }

        }catch (Exception ignored){
            // nao é uma edição!
        }
    }

    public void salvarDespesa (View view){

        if (movimentacao != null){
            if(validarCamposDespesas()){

                movimentacaoDAO.deletar(movimentacao);

                Float valorAtual = movimentacao.getValor();
                Float valorRecuperado = Float.parseFloat(campoValor.getText().toString());

                movimentacao.setValor( valorRecuperado );
                movimentacao.setCategoria(campoCategoria.getText().toString());
                movimentacao.setDescricao(campoDescricao.getText().toString());
                movimentacao.setData(campoData.getText().toString());

                despesaAtualizada = despesaTotal + valorRecuperado - valorAtual;
                atualizarDespesas(despesaAtualizada);

                if (movimentacaoDAO.atualizar(movimentacao)){
                    FirebaseHelper.salvarMovimentacao(movimentacao);
                }

                finish();
            }
        }else
        if(validarCamposDespesas()){

            Float valorRecuperado = Float.parseFloat(campoValor.getText().toString());

            movimentacao = new Movimentacao();
            movimentacao.setIdMovimentacao(UUID.randomUUID().toString().replace("-",""));
            movimentacao.setFk_usuario(mySharedPreferencs.getUsuarioAtual().getIdUser());
            movimentacao.setValor( valorRecuperado );
            movimentacao.setCategoria(campoCategoria.getText().toString());
            movimentacao.setDescricao(campoDescricao.getText().toString());
            movimentacao.setData(campoData.getText().toString());
            movimentacao.setTipo("d");

            despesaAtualizada = despesaTotal + valorRecuperado;

            if (movimentacaoDAO.salvar(movimentacao)){
                atualizarDespesas(despesaAtualizada);
                FirebaseHelper.salvarMovimentacao(movimentacao);
                movimentacaoDAO.putCallBack(movimentacao, "ATU");
            }
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

        Usuario usuario = mySharedPreferencs.getUsuarioAtual();
        despesaTotal = usuario.getDespesaTotal();
    }

    public void atualizarDespesas (Float despesaTotal){

        Usuario usuario = mySharedPreferencs.getUsuarioAtual();
        usuario.setDespesaTotal(despesaTotal);
        usuario.setDataModificação();

        if (mySharedPreferencs.salvarUsuarioAtual(usuario)){
            FirebaseHelper.atualizarUsuario(usuario);
        }
    }

}