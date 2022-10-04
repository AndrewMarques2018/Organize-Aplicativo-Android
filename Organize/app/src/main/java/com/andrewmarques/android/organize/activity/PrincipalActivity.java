package com.andrewmarques.android.organize.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Bundle;

import com.andrewmarques.android.organize.R;
import com.andrewmarques.android.organize.adapter.AdapterMovimentacao;
import com.andrewmarques.android.organize.databinding.ActivityPrincipalBinding;
import com.andrewmarques.android.organize.helper.DateCustom;
import com.andrewmarques.android.organize.helper.FirebaseHelper;
import com.andrewmarques.android.organize.helper.MovimentacaoDAO;
import com.andrewmarques.android.organize.helper.MySharedPreferencs;
import com.andrewmarques.android.organize.helper.RecyclerItemClickListener;
import com.andrewmarques.android.organize.model.Movimentacao;
import com.andrewmarques.android.organize.model.Usuario;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
    Criado por: Andrew Marques Silva
    Github: https://github.com/AndrewMarques2018
    Linkedin: https://www.linkedin.com/in/andrewmarques2018
    Instagram: https://www.instagram.com/andrewmarquessilva
 */

public class PrincipalActivity extends AppCompatActivity {

    private MySharedPreferencs mySharedPreferencs;
    private MovimentacaoDAO movimentacaoDAO;

    ConnectivityManager connectivityManager;
    NetworkRequest networkRequest;
    ConnectivityManager.NetworkCallback networkCallback;
    private boolean isOnline;

    private ActivityPrincipalBinding binding;
    private CalendarView calendarView;
    private TextView txtSaudacao, txtSaldo;
    private Float despesaTotal = 0.00f;
    private Float receitaTotal = 0.00f;
    private TextView txtValorSaldoMensal;

    private RecyclerView recyclerView;
    private AdapterMovimentacao adapterMovimentacao;
    private List<Movimentacao> movimentacoes = new ArrayList<>();
    private Movimentacao movimentacao;
    private String mesAnoSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPrincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        txtSaudacao = findViewById(R.id.txtSaudacao);
        txtSaldo = findViewById(R.id.txtSaldoGeral);
        calendarView = findViewById(R.id.calendarView);
        txtValorSaldoMensal = findViewById(R.id.txtValorSaldoMensal);
        recyclerView = findViewById(R.id.recycleMovimentos);

        movimentacaoDAO = new MovimentacaoDAO(getApplicationContext());
        mySharedPreferencs = new MySharedPreferencs(getApplicationContext());

        addOnItemClickListener();
        swipe();

        configuracaoCalendarView();

        adapterMovimentacao = new AdapterMovimentacao(movimentacoes, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterMovimentacao);

        addNetworkCallback();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (isOnline){
            atualizarUsuario();
            verificarMovimentacoesCallBacks();
            resgatarMovimentacoesFirebase();
        }

        recuperarResumo();
        atualizarView();
    }

    public void addNetworkCallback() {
        connectivityManager = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        networkRequest = new NetworkRequest.Builder().build();
        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onLost(@NonNull Network network) {
                isOnline = false;
                super.onLost(network);
            }

            @Override
            public void onAvailable(@NonNull Network network) {
                isOnline = true;
                super.onAvailable(network);
            }
        };

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
    }

    public void verificarMovimentacoesCallBacks() {

        List<Movimentacao> movimentacaosCalbacks = movimentacaoDAO.listarCallBack();
        for (Movimentacao callback : movimentacaosCalbacks) {
            if (callback.getStatus().equals("DEL")){
                FirebaseHelper.deletar(callback);
                movimentacaoDAO.removeByCallBack(callback);
            }
            if (callback.getStatus().equals("ATU")){
                FirebaseHelper.salvarMovimentacao(callback);
                callback.setStatus("nul");
                movimentacaoDAO.removeByCallBack(callback);

            }
        }
    }

    public void atualizarUsuario () {

        Log.i("atualizar usuario", "atualizar usuario: ");
        FirebaseHelper.usuarioValueEventListener = FirebaseHelper.getUsuarioReference()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Usuario usuarioFirebase = snapshot.getValue(Usuario.class);
                        Usuario usuarioLocal = mySharedPreferencs.getUsuarioAtual();

                        if (usuarioLocal.getIdUser() != null || usuarioLocal.getNome() == null){
                            mySharedPreferencs.salvarUsuarioAtual(usuarioFirebase);
                        }

                        assert usuarioFirebase != null;
                        if (usuarioFirebase.compareTo(usuarioLocal) > 0){
                            mySharedPreferencs.salvarUsuarioAtual(usuarioFirebase);
                        }else
                        if (usuarioFirebase.compareTo(usuarioLocal) < 0){
                            FirebaseHelper.atualizarUsuario(usuarioLocal);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });
    }

    public void resgatarMovimentacoesFirebase (){

        FirebaseHelper.movimentacaoValueEventListener = FirebaseHelper.getMovimentacaoReference()
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot dados : snapshot.getChildren()) {
                        for (DataSnapshot dados2 : dados.getChildren()) {
                            Movimentacao movimentacao = dados2.getValue(Movimentacao.class);
                            Log.i("resgatarmovfirebase: ", String.valueOf(movimentacao));
                            movimentacaoDAO.put(movimentacao);
                        }
                    }

                    float valorDespesaTotal = 0.0f;
                    float valorReceitaTotal = 0.0f;

                    for (Movimentacao movimentacao : movimentacaoDAO.listar()) {
                        if(movimentacao.getTipo().equals("r")){
                            valorReceitaTotal += movimentacao.getValor();
                        }else
                        if(movimentacao.getTipo().equals("d")){
                            valorDespesaTotal += movimentacao.getValor();
                        }
                    }

                    Usuario usuario = mySharedPreferencs.getUsuarioAtual();
                    usuario.setReceitaTotal(valorReceitaTotal);
                    usuario.setDespesaTotal(valorDespesaTotal);
                    usuario.setDataModificação();

                    mySharedPreferencs.salvarUsuarioAtual(usuario);
                    FirebaseHelper.atualizarUsuario(usuario);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

    }

    public void swipe () {

        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

               int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
               int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
               return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                excluirMovimentacao(viewHolder);
            }
        };

        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerView);
    }

    public void addOnItemClickListener () {

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                try {
                                    movimentacao = movimentacoes.get(position);
                                    if (movimentacao.getTipo().equals("r")){
                                        Intent intent = new Intent(PrincipalActivity.this, ReceitasActivity.class);
                                        intent.putExtra("movimentacaoSelecionada", movimentacao);
                                        startActivity(intent);
                                    }else
                                    if (movimentacao.getTipo().equals("d")) {
                                        Intent intent = new Intent(PrincipalActivity.this, DespesasActivity.class);
                                        intent.putExtra("movimentacaoSelecionada", movimentacao);
                                        startActivity(intent);
                                    }

                                }catch (Exception e){
                                    Toast.makeText(getApplicationContext(),
                                            "Erro ao recuperar movimentação",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );

    }

    public void excluirMovimentacao ( RecyclerView.ViewHolder viewHolder) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Excluir Movimentação da Conta");
        alertDialog.setMessage("Você tem certeza que deseja realmente excluir?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = viewHolder.getAdapterPosition();
                movimentacao = movimentacoes.get(position);

                if (movimentacaoDAO.deletar(movimentacao)){
                    FirebaseHelper.deletar(movimentacao);
                    movimentacaoDAO.putCallBack(movimentacao, "DEL");
                    adapterMovimentacao.notifyItemRemoved(position);
                }

                Usuario usuario = mySharedPreferencs.getUsuarioAtual();
                if (movimentacao.getTipo().equals("r")){
                    receitaTotal -= movimentacao.getValor();
                    usuario.setReceitaTotal(receitaTotal);
                    usuario.setDataModificação();
                }else
                if (movimentacao.getTipo().equals("d")){
                    despesaTotal -= movimentacao.getValor();
                    usuario.setDespesaTotal(despesaTotal);
                    usuario.setDataModificação();
                }

                if (mySharedPreferencs.salvarUsuarioAtual(usuario)){
                    FirebaseHelper.atualizarUsuario(usuario);
                }

                atualizarView();
                recuperarResumo();
            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapterMovimentacao.notifyDataSetChanged();
                Toast.makeText(PrincipalActivity.this,
                        "Cancelado",
                        Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    public void atualizarView() {

        movimentacoes.clear();
        movimentacoes.addAll(movimentacaoDAO.listar(mesAnoSelecionado));
        Collections.sort(movimentacoes);
        configurarSaldoMensal();

        adapterMovimentacao.notifyDataSetChanged();
    }

    public void configurarSaldoMensal () {

        Double valorTotal = 0.00;
        Double valorDespesas = 0.00;
        Double valorReceitas = 0.00;

        for (Movimentacao m: movimentacoes){

            if (m.getTipo().equals("d")){
                valorDespesas += m.getValor();
            }else
            if (m.getTipo().equals("r")){
                valorReceitas += m.getValor();
            }

        }

        valorTotal = valorReceitas - valorDespesas;

        if(valorTotal < 0.00){
            txtValorSaldoMensal.setTextColor(getResources().getColor(R.color.colorRedDark));
        }else
        if(valorTotal > 0.00){
            txtValorSaldoMensal.setTextColor(getResources().getColor(R.color.colorGreenDark));
        }else{
            txtValorSaldoMensal.setTextColor(getResources().getColor(R.color.colorBackGround));
        }
        DecimalFormat decimalFormat = new DecimalFormat( "0.00" );
        txtValorSaldoMensal.setText("R$ " + decimalFormat.format(valorTotal));
    }

    public void recuperarResumo (){

        Usuario usuario = mySharedPreferencs.getUsuarioAtual();
        despesaTotal = usuario.getDespesaTotal();
        receitaTotal = usuario.getReceitaTotal();
        Float resumoTotal = receitaTotal - despesaTotal;

        DecimalFormat decimalFormat = new DecimalFormat( "0.00" );
        txtSaldo.setText("R$ " + decimalFormat.format(resumoTotal));
        txtSaudacao.setText("Olá " + usuario.getNome());

    }

    public void configuracaoCalendarView (){
        calendarView.setPreviousButtonImage(getResources().getDrawable(R.drawable.ic_esquerda_preto));
        calendarView.setForwardButtonImage(getResources().getDrawable(R.drawable.ic_direita_preto));

        mesAnoSelecionado = DateCustom.parseMillisOfMesAno(calendarView.getCurrentPageDate().getTimeInMillis());

        calendarView.setOnForwardPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                mesAnoSelecionado = DateCustom.parseMillisOfMesAno(calendarView.getCurrentPageDate().getTimeInMillis());
                FirebaseHelper.removeMovimentacaoEventListener();
                atualizarView();
                recuperarResumo();
            }
        });

        calendarView.setOnPreviousPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                mesAnoSelecionado = DateCustom.parseMillisOfMesAno(calendarView.getCurrentPageDate().getTimeInMillis());
                FirebaseHelper.removeMovimentacaoEventListener();
                atualizarView();
                recuperarResumo();
            }
        });
    }

    public void addReceita (View view){
        startActivity(new Intent(this, ReceitasActivity.class));
    }

    public void addDespesa (View view){
        startActivity(new Intent( this, DespesasActivity.class));
    }

    public void finalizarAplication (){
        connectivityManager.unregisterNetworkCallback(networkCallback);
        movimentacaoDAO.limpar();
        movimentacaoDAO.clearCallBack();
        FirebaseHelper.stop();
        FirebaseHelper.signOut();
        mySharedPreferencs.finalizar();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
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

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Organize Finance");
                alertDialog.setMessage("Deseja sair da Conta ?");
                alertDialog.setCancelable(true);

                alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finalizarAplication();
                    }
                });

                alertDialog.setNegativeButton("Cancelar", null);

                AlertDialog alert = alertDialog.create();
                alert.show();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseHelper.stop();
    }

    @Override
    protected void onDestroy() {
        connectivityManager.unregisterNetworkCallback(networkCallback);
        mySharedPreferencs.finalizar();
        super.onDestroy();
    }
}