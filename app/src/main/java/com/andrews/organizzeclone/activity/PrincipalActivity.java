package com.andrews.organizzeclone.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andrews.organizzeclone.R;
import com.andrews.organizzeclone.adapter.AdapterMovimentacao;
import com.andrews.organizzeclone.config.ConfiguracaoFirebase;
import com.andrews.organizzeclone.helper.Base64Custom;
import com.andrews.organizzeclone.model.Movimentacao;
import com.andrews.organizzeclone.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PrincipalActivity extends AppCompatActivity {

    private TextView textoSaudacao, textoSaldo, textoSaldoMensal;
    private Double despesaTotal = 0.0;
    private Double receitaTotal = 0.0;
    private Double despesaMensal = 0.0;
    private Double receitaMensal = 0.0;
    private Double resumoUsuarioTotal = 0.0;
    private Double resumoUsuarioMensal = 0.0;
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference usuarioRef;
    private ValueEventListener valueEventListenerUsuario;
    private ValueEventListener valueEventListenerMovimentacoes;
    private RecyclerView recyclerView;
    private AdapterMovimentacao adapterMovimentacao;
    private List<Movimentacao> movimentacoes = new ArrayList<>();
    private Movimentacao movimentacao;
    private DatabaseReference movimentacaoRef;
    private TextView txtMesAno;
    private Button btNext, btPrev;
    private String mesAnoSelecionado;
    private int mes, ano;
    CharSequence meses[] = {"Janeiro","Fevereiro", "Março","Abril","Maio","Junho","Julho","Agosto","Setembro","Outubro","Novembro","Dezembro"};

    public void setarMesAnoAtual() {
        Calendar calendar = Calendar.getInstance();
        mes = calendar.get(Calendar.MONTH); // Pegar o mês atual
        ano = calendar.get(Calendar.YEAR); // Pegar o ano atual
        mesAnoSelecionado = String.format(Locale.getDefault(), "%02d%d", mes + 1, ano); // Formatar a string do mês e ano selecionados
        txtMesAno.setText(String.format(Locale.getDefault(), "%s %d", meses[mes], ano)); // Setar o texto do TextView com o mês e ano
    }

    // Método para configurar os botões de próximo e anterior
    public void configurarMesAno() {
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mes++; // Incrementar o mês
                if (mes > 11) { // Se o mês for maior que 11 (dezembro), voltar para janeiro do próximo ano
                    mes = 0;
                    ano++;
                }
                atualizarMesAnoSelecionado(); // Atualizar o mês e ano selecionados
                movimentacaoRef.removeEventListener(valueEventListenerMovimentacoes); // Remover o listener de movimentações
                recuperarMovimentacoes(); // Recuperar as movimentações
            }
        });

        btPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mes--; // Decrementar o mês
                if (mes < 0) { // Se o mês for menor que 0 (janeiro), voltar para dezembro do ano anterior
                    mes = 11;
                    ano--;
                }
                atualizarMesAnoSelecionado(); // Atualizar o mês e ano selecionados
                movimentacaoRef.removeEventListener(valueEventListenerMovimentacoes); // Remover o listener de movimentações
                recuperarMovimentacoes(); // Recuperar as movimentações
            }
        });
    }

    // Método para atualizar o texto do TextView com o mês e ano selecionados
    private void atualizarMesAnoSelecionado() {
        mesAnoSelecionado = String.format(Locale.getDefault(), "%02d%d", mes + 1, ano); // Formatar a string do mês e ano selecionados
        txtMesAno.setText(String.format(Locale.getDefault(), "%s %d", meses[mes], ano)); // Setar o texto do TextView com o mês e ano
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Organizze");
        setSupportActionBar(toolbar);

        textoSaldo = findViewById(R.id.txtSaldoGeral);
        textoSaldoMensal = findViewById(R.id.txtValorSaldoMensal);
        textoSaudacao = findViewById(R.id.txtSaudacao);
        recyclerView = findViewById(R.id.recycleMovimentos);
        txtMesAno = findViewById(R.id.txtMesAno);
        btNext = findViewById(R.id.btNext);
        btPrev = findViewById(R.id.btPrev);
        setarMesAnoAtual();
        configurarMesAno();
        swipe();

        //Configurar adapter
        adapterMovimentacao = new AdapterMovimentacao(movimentacoes,this);

        //Configurar RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter( adapterMovimentacao );
    }

    public void swipe(){

        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                excluirMovimentacao( viewHolder );
            }
        };

        new ItemTouchHelper( itemTouch ).attachToRecyclerView( recyclerView );

    }

    public void excluirMovimentacao(final RecyclerView.ViewHolder viewHolder){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        //Configura AlertDialog
        alertDialog.setTitle("Excluir Movimentação da Conta");
        alertDialog.setMessage("Você tem certeza que deseja realmente excluir essa movimentação de sua conta?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = viewHolder.getAdapterPosition();
                movimentacao = movimentacoes.get( position );

                String emailUsuario = autenticacao.getCurrentUser().getEmail();
                String idUsuario = Base64Custom.codificarBase64( emailUsuario );
                movimentacaoRef = firebaseRef.child("movimentacao")
                        .child( idUsuario )
                        .child( mesAnoSelecionado );

                System.out.println("debug Mes Ano: " + mesAnoSelecionado);

                movimentacaoRef.child( movimentacao.getKey() ).removeValue();
                adapterMovimentacao.notifyItemRemoved( position );
                atualizarSaldo();

            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(PrincipalActivity.this,
                        "Cancelado",
                        Toast.LENGTH_SHORT).show();
                adapterMovimentacao.notifyDataSetChanged();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();


    }

    public void atualizarSaldo(){

        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64( emailUsuario );
        usuarioRef = firebaseRef.child("usuarios").child( idUsuario );

        if ( movimentacao.getTipo().equals("r") ){
            receitaTotal = receitaTotal - movimentacao.getValor();
            usuarioRef.child("receitaTotal").setValue(receitaTotal);
        }

        if ( movimentacao.getTipo().equals("d") ){
            despesaTotal = despesaTotal - movimentacao.getValor();
            usuarioRef.child("despesaTotal").setValue( despesaTotal );
        }

    }

    public void recuperarMovimentacoes(){

        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64( emailUsuario );
        movimentacaoRef = firebaseRef.child("movimentacao")
                                     .child( idUsuario )
                                     .child( mesAnoSelecionado );

        valueEventListenerMovimentacoes = movimentacaoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                movimentacoes.clear();
                for (DataSnapshot dados: dataSnapshot.getChildren() ){

                    Movimentacao movimentacao = dados.getValue( Movimentacao.class );
                    movimentacao.setKey( dados.getKey() );
                    movimentacoes.add( movimentacao );

                }

                recuperarResumoMensal();
                adapterMovimentacao.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void recuperarResumoTotal(){

        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64( emailUsuario );
        usuarioRef = firebaseRef.child("usuarios").child( idUsuario );

        valueEventListenerUsuario = usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Usuario usuario = dataSnapshot.getValue( Usuario.class );

                despesaTotal = usuario.getDespesaTotal();
                receitaTotal = usuario.getReceitaTotal();
                resumoUsuarioTotal = receitaTotal - despesaTotal;

                DecimalFormat decimalFormat = new DecimalFormat("0.##");
                String resultadoFormatado = decimalFormat.format( resumoUsuarioTotal );

                textoSaudacao.setText("Olá, " + usuario.getNome() );
                textoSaldo.setText( "R$ " + resultadoFormatado );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void recuperarResumoMensal() {

        receitaMensal = 0.0;
        despesaMensal = 0.0;

        for (Movimentacao mov : movimentacoes) {
            if (mov.getTipo().equals("r")) {
                receitaMensal += mov.getValor();
            } else {
                despesaMensal += mov.getValor();
            }
        }

        resumoUsuarioMensal = receitaMensal - despesaMensal;

        DecimalFormat decimalFormat = new DecimalFormat("0.##");
        String resultadoFormatado = decimalFormat.format(resumoUsuarioMensal);

        if(receitaMensal > despesaMensal) {
            textoSaldoMensal.setTextColor(getResources().getColor(R.color.colorGreenDark));
        }else if(receitaMensal < despesaMensal) {
            textoSaldoMensal.setTextColor(getResources().getColor(R.color.colorAccentDespesa));
        } else {
            textoSaldoMensal.setTextColor(getResources().getColor(R.color.colorBackGround));
        }

        textoSaldoMensal.setText("R$ " + resultadoFormatado);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuSair) {
            autenticacao.signOut();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void adicionarDespesa(View view){
        startActivity(new Intent(this, DespesasActivity.class));
    }

    public void adicionarReceita(View view){
        startActivity(new Intent(this, ReceitasActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarResumoTotal();
        recuperarMovimentacoes();
    }

    @Override
    protected void onStop() {
        super.onStop();
        usuarioRef.removeEventListener( valueEventListenerUsuario );
        movimentacaoRef.removeEventListener( valueEventListenerMovimentacoes );
    }
}
