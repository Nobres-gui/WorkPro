package com.example.projeto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ProgressBar progressBar;
    TextView txtFreqValor;
    TextView txtTotalMes;
    TextView txtStreak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        txtFreqValor = findViewById(R.id.qtd_freq_semana_valor);
        txtTotalMes = findViewById(R.id.qtd_total_treino_mes);
        txtStreak = findViewById(R.id.qtd_total_treino_seguidos);

        SharedPreferences prefsFirst = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstRun = prefsFirst.getBoolean("firstRun", true);
        if (firstRun) {
            prefsFirst.edit().putBoolean("firstRun", false).apply();
            startActivity(new Intent(MainActivity.this, PermissoesActivity.class));
            finish();
            return;
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_home) return true;
            if (id == R.id.navigation_exercicios) {
                startActivity(new Intent(getApplicationContext(), ExerciciosActivity.class));
                overridePendingTransition(0,0);
                return true;
            }
            if (id == R.id.navigation_treino) {
                startActivity(new Intent(getApplicationContext(), TreinoActivity.class));
                overridePendingTransition(0,0);
                return true;
            }
            if (id == R.id.navigation_timer) {
                startActivity(new Intent(getApplicationContext(), TimerActivity.class));
                overridePendingTransition(0,0);
                return true;
            }
            if (id == R.id.navigation_perfil) {
                startActivity(new Intent(getApplicationContext(), PerfilActivity.class));
                overridePendingTransition(0,0);
                return true;
            }
            return false;
        });

        carregarProgressoUsuario();
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarProgressoUsuario();
    }

    private void carregarProgressoUsuario() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        int idUsuario = prefs.getInt("idUsuario", -1);

        if (idUsuario == -1) {
            resetarValores();
            return;
        }

        BancoControllerUsuarios controller = new BancoControllerUsuarios(this);
        Cursor c = controller.buscarUsuarioPeloID(idUsuario);

        if (c != null && c.moveToFirst()) {
            int frequenciaSemana = c.getInt(c.getColumnIndexOrThrow("frequencia_semana"));
            int treinosMes = c.getInt(c.getColumnIndexOrThrow("treinos_mes"));
            int streak = c.getInt(c.getColumnIndexOrThrow("streak"));
            String ultimoTreino = c.getString(c.getColumnIndexOrThrow("ultimo_treino"));

            int treinosSemanaAtual = controller.getTreinosDaSemana(idUsuario);

            // Ajusta progress bar
            int freqMax = frequenciaSemana > 0 ? frequenciaSemana : 3;
            progressBar.setMax(freqMax);
            progressBar.setProgress(treinosSemanaAtual);

            // Atualiza textos
            txtFreqValor.setText(treinosSemanaAtual + "/" + freqMax);
            txtTotalMes.setText(String.valueOf(treinosMes));
            txtStreak.setText(String.valueOf(ajustarStreak(idUsuario, ultimoTreino, controller)));
        }

        if (c != null) c.close();
    }

    private void resetarValores() {
        progressBar.setMax(1);
        progressBar.setProgress(0);
        txtFreqValor.setText("0/0");
        txtTotalMes.setText("0");
        txtStreak.setText("0");
    }

    private int ajustarStreak(int idUsuario, String ultimaData, BancoControllerUsuarios controller) {
        if (ultimaData == null || ultimaData.isEmpty()) return 0;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String hojeStr = sdf.format(new Date());

            if (ultimaData.equals(hojeStr)) {
                Cursor c = controller.buscarUsuarioPeloID(idUsuario);
                int streakAtual = 0;
                if (c != null && c.moveToFirst()) {
                    streakAtual = c.getInt(c.getColumnIndexOrThrow("streak"));
                }
                if (c != null) c.close();
                return streakAtual;
            }

            Calendar calUlt = Calendar.getInstance();
            calUlt.setTime(sdf.parse(ultimaData));

            Calendar ontem = Calendar.getInstance();
            ontem.add(Calendar.DAY_OF_YEAR, -1);

            if (!sdf.format(calUlt.getTime()).equals(sdf.format(ontem.getTime()))) {
                controller.forcarStreakZero(idUsuario);
                return 0;
            }

            Cursor c = controller.buscarUsuarioPeloID(idUsuario);
            int streakAtual = 0;
            if (c != null && c.moveToFirst()) {
                streakAtual = c.getInt(c.getColumnIndexOrThrow("streak"));
            }
            if (c != null) c.close();
            return streakAtual;

        } catch (Exception e) {
            Log.e("MainActivity", "Erro no ajuste de streak: " + e.getMessage());
            return 0;
        }
    }
}
