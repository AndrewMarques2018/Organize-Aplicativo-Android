package com.andrewmarques.android.organize.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.andrewmarques.android.organize.R;
import com.andrewmarques.android.organize.config.ConfigFirebase;
import com.andrewmarques.android.organize.databinding.ActivityPrincipalBinding;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.navigation.ui.AppBarConfiguration;


public class PrincipalActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private AppBarConfiguration appBarConfiguration;
    private ActivityPrincipalBinding binding;
    private CalendarView calendarView;
    private TextView txtSaudacao, txtSaldo;

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
                auth = ConfigFirebase.getAuth();
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