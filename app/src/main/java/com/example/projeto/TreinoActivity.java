package com.example.projeto;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TreinoActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    LinearLayout listaTreinos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treino);

        listaTreinos = findViewById(R.id.lista_treinos);

        carregarTreinos();

        findViewById(R.id.btn_novoTreino).setOnClickListener(v ->
                startActivity(new Intent(TreinoActivity.this, NovoTreinoActivity.class))
        );

        // Bottom Navigation
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_treino);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent = null;

            if (id == R.id.navigation_home)
                intent = new Intent(this, MainActivity.class);
            else if (id == R.id.navigation_exercicios)
                intent = new Intent(this, ExerciciosActivity.class);
            else if (id == R.id.navigation_timer)
                intent = new Intent(this, TimerActivity.class);
            else if (id == R.id.navigation_perfil)
                intent = new Intent(this, PerfilActivity.class);

            if (intent != null) {
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;
            }
            return id == R.id.navigation_treino;
        });
    }

    private void carregarTreinos() {
        listaTreinos.removeAllViews();

        CriaBanco banco = new CriaBanco(this);
        SQLiteDatabase db = banco.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT t.idTreino, t.nome, COUNT(e.idExercicio) AS qtdEx " +
                        "FROM treinos t LEFT JOIN exercicios e ON t.idTreino = e.treino_id " +
                        "GROUP BY t.idTreino, t.nome", null
        );

        while (cursor.moveToNext()) {
            long idTreino = cursor.getLong(0);
            String nomeTreino = cursor.getString(1);
            int qtdEx = cursor.getInt(2);

            View card = getLayoutInflater().inflate(R.layout.treino_card, null);
            card.setTag(idTreino);

            TextView txtNome = card.findViewById(R.id.card_treino_nome);
            TextView txtQtd = card.findViewById(R.id.card_treino_qtd);

            txtNome.setText(nomeTreino);
            txtQtd.setText(qtdEx + " exercícios");

            card.setOnClickListener(v -> {
                long treinoSelecionado = (long) v.getTag();
                Log.d("TreinoActivity", "Card clicado - ID do treino: " + treinoSelecionado);

                Intent intent = new Intent(TreinoActivity.this, TreinoAddExerciciosActivity.class);
                intent.putExtra("idTreino", treinoSelecionado);
                startActivity(intent);
            });

            float dp = 10f;
            float px = dp * getResources().getDisplayMetrics().density;

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, (int) px);
            card.setLayoutParams(params);

            listaTreinos.addView(card);
        }

        cursor.close();
        db.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarTreinos();
    }
}
