package com.example.projeto;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ExerciciosActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercicios);


        /* Funcionamento do Bottom Menu*/
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_exercicios);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.navigation_home) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            else if (id == R.id.navigation_timer) {
                startActivity(new Intent(getApplicationContext(), ExerciciosActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            else if (id == R.id.navigation_treino) {
                startActivity(new Intent(getApplicationContext(), TreinoActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            else if (id == R.id.navigation_exercicios) {
                return true; // já está aqui
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