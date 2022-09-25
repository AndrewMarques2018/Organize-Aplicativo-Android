package com.andrewmarques.android.organize.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.andrewmarques.android.organize.R;
import com.andrewmarques.android.organize.helper.DateCustom;
import com.andrewmarques.android.organize.model.Movimentacao;
import com.google.android.material.textfield.TextInputEditText;

public class DespesasActivity extends AppCompatActivity {

    private TextInputEditText campoData, campoDescricao, campoCategoria;
    private EditText campoValor;
    private Movimentacao movimentacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        campoData = findViewById(R.id.editDespesaData);
        campoCategoria = findViewById(R.id.editDespesaCategoria);
        campoDescricao = findViewById(R.id.editDespesaDescricao);
        campoValor = findViewById(R.id.editDespesasValor);

        // preencher data padrão, data atual
        campoData.setText(DateCustom.dataAtual());
    }

    public void salvarDespesa (View view){
        movimentacao = new Movimentacao();
        movimentacao.setValor( Double.parseDouble(campoValor.getText().toString()));
        movimentacao.setCategoria(campoCategoria.getText().toString());
        movimentacao.setDescricao(campoDescricao.getText().toString());
        movimentacao.setData(campoData.getText().toString());
        movimentacao.setTipo("d");
        movimentacao.salvar();
    }

}