package com.example.projeto;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;




public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Valida de é a primera execução do aplicativo, se sim vai para a página de permissões*/
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstRun = prefs.getBoolean("firstRun", true);

        if (firstRun) {
            // Marca como já executado
            prefs.edit().putBoolean("firstRun", false).apply();

            // Abre tela de permissões
            Intent intent = new Intent(MainActivity.this, PermissoesActivity.class);
            startActivity(intent);
            finish(); // impede voltar para cá
            return;
        }

        /*Muda a imagem dependendo do modo claro ou escuro*/
        ImageView img_peito = findViewById(R.id.img_peito);
        img_peito.setImageResource(R.drawable.icone_peito);

        ImageView img_costas = findViewById(R.id.img_costas);
        img_costas.setImageResource(R.drawable.icone_costas);

        ImageView img_perna = findViewById(R.id.img_perna);
        img_perna.setImageResource(R.drawable.icone_perna);

        /* Funcionamento do Bottom Menu*/
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // define o botão selecionado inicialmente
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.navigation_home) {
                // já está na home
                return true;
            }
            else if (id == R.id.navigation_exercicios) {
                startActivity(new Intent(getApplicationContext(), ExerciciosActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            else if (id == R.id.navigation_treino) {
                startActivity(new Intent(getApplicationContext(), TreinoActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            else if (id == R.id.navigation_timer) {
                startActivity(new Intent(getApplicationContext(), TimerActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            else if (id == R.id.navigation_perfil) {
                startActivity(new Intent(getApplicationContext(), PerfilActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }

            return false;
        });
    }



}