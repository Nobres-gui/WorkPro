package com.example.projeto;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;




public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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