package com.andrewmarques.android.organize.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.andrewmarques.android.organize.R;
import com.andrewmarques.android.organize.config.ConfigFirebase;
import com.andrewmarques.android.organize.databinding.ActivityPrincipalBinding;
import com.andrewmarques.android.organize.helper.Base64Custom;
import com.andrewmarques.android.organize.model.User;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
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

import java.text.DecimalFormat;


public class PrincipalActivity extends AppCompatActivity {

    private FirebaseAuth auth = ConfigFirebase.getAuth();
    private DatabaseReference firebase = ConfigFirebase.getDatabaseReference();;
    private AppBarConfiguration appBarConfiguration;
    private ActivityPrincipalBinding binding;
    private CalendarView calendarView;
    private TextView txtSaudacao, txtSaldo;
    private Double despesaTotal = 0.00;
    private Double receitaTotal = 0.00;
    private Double resumoTotal = 0.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPrincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setTitle("Organize");
        setSupportActionBar(binding.toolbar);

        txtSaudacao = findViewById(R.id.txtSaudacao);
        txtSaldo = findViewById(R.id.txtSaldo);
        calendarView = findViewById(R.id.calendarView);

        calendarView.setPreviousButtonImage(getResources().getDrawable(R.drawable.ic_esquerda_preto));
        calendarView.setForwardButtonImage(getResources().getDrawable(R.drawable.ic_direita_preto));

        binding.fabMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        recuperarResumo();
    }

    public void recuperarResumo (){

        String emailUser = auth.getCurrentUser().getEmail();
        String idUser = Base64Custom.codificarBase64(emailUser);
        DatabaseReference userRef = firebase.child("usuarios").child(idUser);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                despesaTotal = user.getDespesaTotal();
                receitaTotal = user.getReceitaTotal();
                resumoTotal = receitaTotal - despesaTotal;

                DecimalFormat decimalFormat = new DecimalFormat( "R$ 0.##" );
                txtSaldo.setText(decimalFormat.format(decimalFormat));
                txtSaudacao.setText("Olá " + user.getNome());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

}