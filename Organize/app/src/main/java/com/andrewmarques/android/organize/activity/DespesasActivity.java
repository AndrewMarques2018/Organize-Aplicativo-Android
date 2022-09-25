package com.andrewmarques.android.organize.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.andrewmarques.android.organize.R;
import com.andrewmarques.android.organize.helper.DateCustom;
import com.google.android.material.textfield.TextInputEditText;

public class DespesasActivity extends AppCompatActivity {

    private TextInputEditText campoData, campoDescricao, campoCategoria;
    private EditText campoValor;

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
}