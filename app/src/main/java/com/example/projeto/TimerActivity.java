package com.example.projeto;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

import java.text.MessageFormat;
import java.util.Locale;

public class TimerActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;


    /*Inicio Programação Cronometro, criação das variaveis e funcao do cronometro e visualização na tela*/
    TextView text_time;
    Button btn_iniciar, btn_resetar, btn_parar;

    int horas, minutos, segundos, milisegundo;
    long milisegundos, startTime, timeBuff, updateTime = 0L;

    Handler handler;
    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            milisegundos = SystemClock.uptimeMillis() - startTime;
            updateTime = timeBuff + milisegundos;
            segundos = (int) (updateTime / 1000);

            minutos = (segundos / 60);
            segundos = segundos % 60;
            horas = minutos / 60;

            milisegundos = (int) (updateTime % 1000);


            text_time. setText(MessageFormat.format("{0}:{1}:{2}", String.format(Locale.getDefault(), "%02d", horas),
                    String.format(Locale.getDefault(), "%02d", minutos), String.format(Locale.getDefault(), "%02d", segundos)));

            handler.postDelayed(this, 0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        text_time = findViewById(R.id.text_time);
        btn_iniciar = findViewById(R.id.btn_iniciar);
        btn_parar = findViewById(R.id.btn_parar);
        btn_resetar = findViewById(R.id.btn_resetar);

        handler = new Handler(Looper.getMainLooper());

        btn_iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTime = SystemClock.uptimeMillis();
                handler.postDelayed(runnable, 0);
                btn_resetar.setEnabled(false);
                btn_parar.setEnabled(true);
                btn_iniciar.setEnabled(false);
            }
        });

        btn_parar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeBuff += milisegundos;
                handler.removeCallbacks(runnable);
                btn_resetar.setEnabled(true);
                btn_parar.setEnabled(false);
                btn_iniciar.setEnabled(true);
            }
        });

        btn_resetar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                milisegundos = 0L;
                startTime = 0L;
                timeBuff = 0L;
                updateTime = 0L;
                horas = 0;
                minutos = 0;
                segundos = 0;
                text_time.setText("00:00:00");
            }
        });

        text_time.setText("00:00:00");

        /* Funcionamento do Bottom Menu*/
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_timer);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.navigation_home) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(0, 0);
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