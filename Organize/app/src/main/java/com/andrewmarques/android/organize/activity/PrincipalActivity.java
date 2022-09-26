package com.andrewmarques.android.organize.activity;

import android.content.Intent;
import android.os.Bundle;

import com.andrewmarques.android.organize.R;
import com.andrewmarques.android.organize.adapter.AdapterMovimentacao;
import com.andrewmarques.android.organize.config.ConfigFirebase;
import com.andrewmarques.android.organize.databinding.ActivityPrincipalBinding;
import com.andrewmarques.android.organize.helper.Base64Custom;
import com.andrewmarques.android.organize.helper.DateCustom;
import com.andrewmarques.android.organize.model.Movimentacao;
import com.andrewmarques.android.organize.model.User;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class PrincipalActivity extends AppCompatActivity {

    private FirebaseAuth auth = ConfigFirebase.getAuth();
    private DatabaseReference firebase = ConfigFirebase.getDatabaseReference();
    private DatabaseReference userRef;
    private ValueEventListener valueEventListenerUser;

    private AppBarConfiguration appBarConfiguration;
    private ActivityPrincipalBinding binding;
    private CalendarView calendarView;
    private TextView txtSaudacao, txtSaldo;
    private Double despesaTotal = 0.00;
    private Double receitaTotal = 0.00;
    private Double resumoTotal = 0.00;

    private RecyclerView recyclerView;
    private AdapterMovimentacao adapterMovimentacao;
    private List<Movimentacao> movimentacoes = new ArrayList<>();
    private DatabaseReference movimentacaoRef;
    private ValueEventListener valueEventListenerMovimentacoes;
    private String mesAnoSelecionado;
    private TextView textViewTeste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPrincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        txtSaudacao = findViewById(R.id.txtSaudacao);
        txtSaldo = findViewById(R.id.txtSaldo);
        calendarView = findViewById(R.id.calendarView);
        textViewTeste = findViewById(R.id.textViewTest);

        configuracaoCalendarView();

        adapterMovimentacao = new AdapterMovimentacao(movimentacoes, this);

        recyclerView = findViewById(R.id.recycleMovimentos);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterMovimentacao);
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarResumo();
        recuperarMovimentacoes();
    }

    public void recuperarMovimentacoes (){

        String emailUser = auth.getCurrentUser().getEmail();
        String idUser = Base64Custom.codificarBase64(emailUser);
        movimentacaoRef = firebase.child("movimentacao").child(idUser).child(mesAnoSelecionado);

        valueEventListenerMovimentacoes = movimentacaoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                movimentacoes.clear();

                for (DataSnapshot dados: snapshot.getChildren()){
                    movimentacoes.add(dados.getValue(Movimentacao.class));
                }

                adapterMovimentacao.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void recuperarResumo (){

        String emailUser = auth.getCurrentUser().getEmail();
        String idUser = Base64Custom.codificarBase64(emailUser);
        userRef = firebase.child("usuarios").child(idUser);

        valueEventListenerUser = userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                assert user != null;
                despesaTotal = user.getDespesaTotal();
                receitaTotal = user.getReceitaTotal();
                resumoTotal = receitaTotal - despesaTotal;

                DecimalFormat decimalFormat = new DecimalFormat( "0.##" );
                txtSaldo.setText("R$" + decimalFormat.format(resumoTotal));
                txtSaudacao.setText("Olá " + user.getNome());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void configuracaoCalendarView (){
        calendarView.setPreviousButtonImage(getResources().getDrawable(R.drawable.ic_esquerda_preto));
        calendarView.setForwardButtonImage(getResources().getDrawable(R.drawable.ic_direita_preto));

        mesAnoSelecionado = DateCustom.parseMillisOfMesAno(calendarView.getCurrentPageDate().getTimeInMillis());

        calendarView.setOnForwardPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                mesAnoSelecionado = DateCustom.parseMillisOfMesAno(calendarView.getCurrentPageDate().getTimeInMillis());
                movimentacaoRef.removeEventListener(valueEventListenerMovimentacoes);
                recuperarMovimentacoes();
            }
        });

        calendarView.setOnPreviousPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                mesAnoSelecionado = DateCustom.parseMillisOfMesAno(calendarView.getCurrentPageDate().getTimeInMillis());
                movimentacaoRef.removeEventListener(valueEventListenerMovimentacoes);
                recuperarMovimentacoes();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuSair:
                auth.signOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addReceita (View view){
        startActivity(new Intent(this, ReceitasActivity.class));
    }

    public void addDespesa (View view){
        startActivity(new Intent( this, DespesasActivity.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
        userRef.removeEventListener( valueEventListenerUser );
        movimentacaoRef.removeEventListener(valueEventListenerMovimentacoes);
    }
}