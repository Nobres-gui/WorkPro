package com.example.projeto;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NovoTreinoActivity extends AppCompatActivity {

    private LinearLayout btnNovoExercicio, layoutExercicios, btnVoltar;
    private Button btnSalvarTreino;
    private EditText tituloTreino;

    private ArrayList<ExercicioTreino> listaExercicios = new ArrayList<>();
    private List<String> listaSpinner = Arrays.asList("01", "02", "03", "04");

    private long treinoId = -1;

    private final ActivityResultLauncher<Intent> adicionarExercicioLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    String nome = data.getStringExtra("exercicio_nome");
                    String grupo = data.getStringExtra("exercicio_grupo");
                    String gif = data.getStringExtra("exercicio_gif");

                    long idExercicio = System.nanoTime();
                    adicionarExercicioNaTela(idExercicio, nome, grupo, gif, "01", "01");
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_treino);

        btnVoltar = findViewById(R.id.btn_voltar);
        btnVoltar.setOnClickListener(v -> voltarPagina());

        btnNovoExercicio = findViewById(R.id.btn_novoExercicio);
        layoutExercicios = findViewById(R.id.lista_exercicios_adicionados);
        btnSalvarTreino = findViewById(R.id.btn_salvarTreino);
        tituloTreino = findViewById(R.id.editar_tituloTreino);

        treinoId = getIntent().getLongExtra("treino_id", -1);

        if (treinoId != -1) {
            carregarTreinoExistente();
            carregarExerciciosDoTreino();
        }

        btnNovoExercicio.setOnClickListener(v -> {
            Intent intent = new Intent(NovoTreinoActivity.this, TreinoAddExerciciosSelecaoActivity.class);
            adicionarExercicioLauncher.launch(intent);
        });

        btnSalvarTreino.setOnClickListener(v -> salvarTreinoNoBD());
    }

    private void voltarPagina() {
        String origem = getIntent().getStringExtra("origem");
        Intent intent;

        if (origem == null) {
            // fallback
            finish();
            return;
        }

        switch (origem) {
            case "TreinoActivity":
                intent = new Intent(this, TreinoActivity.class);
                break;
            case "TreinoAddExerciciosActivity":
                intent = new Intent(this, TreinoAddExerciciosActivity.class);
                break;
            // adicione outros casos se precisar
            default:
                finish();
                return;
        }
    }

    private void carregarTreinoExistente() {
        if (treinoId == -1) return;

        CriaBanco banco = new CriaBanco(this);
        SQLiteDatabase db = banco.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT nome FROM treinos WHERE idTreino = ?",
                new String[]{String.valueOf(treinoId)}
        );

        if (cursor.moveToFirst()) {
            tituloTreino.setText(cursor.getString(0));
        }

        cursor.close();
        db.close();
    }

    private void carregarExerciciosDoTreino() {
        listaExercicios.clear();
        layoutExercicios.removeAllViews();

        CriaBanco banco = new CriaBanco(this);
        SQLiteDatabase db = banco.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT idExercicio, nome, grupo_muscular, link_gif, series, repeticoes FROM exercicios WHERE treino_id = ?",
                new String[]{String.valueOf(treinoId)}
        );

        while (cursor.moveToNext()) {
            long idExercicio = cursor.getLong(0);
            String nome = cursor.getString(1);
            String grupo = cursor.getString(2);
            String gif = cursor.getString(3);
            String series = cursor.getString(4);
            String repeticoes = cursor.getString(5);

            adicionarExercicioNaTela(idExercicio, nome, grupo, gif,
                    series != null ? series : "01",
                    repeticoes != null ? repeticoes : "01");
        }

        cursor.close();
        db.close();
    }

    private void adicionarExercicioNaTela(long idExercicio, String nome, String grupo, String gif,
                                          String seriesPadrao, String repeticoesPadrao) {

        View card = getLayoutInflater().inflate(R.layout.item_edicao_exercicio, null);
        card.setTag(idExercicio);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        int marginPx = (int) (10 * getResources().getDisplayMetrics().density);
        params.setMargins(0, 0, 0, marginPx);
        card.setLayoutParams(params);

        TextView txtNome = card.findViewById(R.id.txtNome);
        Spinner spSeries = card.findViewById(R.id.select_seriesExer);
        Spinner spRepeticoes = card.findViewById(R.id.select_RepeticoesExer);

        txtNome.setText(nome);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listaSpinner);
        spSeries.setAdapter(adapter);
        spRepeticoes.setAdapter(adapter);

        int posSeries = listaSpinner.indexOf(seriesPadrao);
        int posRepeticoes = listaSpinner.indexOf(repeticoesPadrao);
        spSeries.setSelection(posSeries >= 0 ? posSeries : 0);
        spRepeticoes.setSelection(posRepeticoes >= 0 ? posRepeticoes : 0);

        ExercicioTreino exercicio = new ExercicioTreino(nome, grupo, gif, seriesPadrao, repeticoesPadrao);
        listaExercicios.add(exercicio);

        spSeries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) { exercicio.series = listaSpinner.get(pos); }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        spRepeticoes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) { exercicio.repeticoes = listaSpinner.get(pos); }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        layoutExercicios.addView(card);
    }

    private void salvarTreinoNoBD() {
        CriaBanco banco = new CriaBanco(this);
        SQLiteDatabase db = banco.getWritableDatabase();

        String nomeTreino = tituloTreino.getText().toString().trim();
        if (nomeTreino.isEmpty()) nomeTreino = "Treino Sem Nome";

        // pega idUsuario logado
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        int idUsuario = prefs.getInt("idUsuario", -1);

        if (idUsuario == -1) {
            Toast.makeText(this, "Usuário não identificado. Faça login.", Toast.LENGTH_LONG).show();
            db.close();
            return;
        }

        if (treinoId == -1) {
            ContentValues valoresTreino = new ContentValues();
            valoresTreino.put("nome", nomeTreino);
            valoresTreino.put("usuario_id", idUsuario); // >>> importante: inserir usuario_id
            long novoId = db.insert("treinos", null, valoresTreino);
            if (novoId == -1) {
                Toast.makeText(this, "Erro ao criar treino.", Toast.LENGTH_LONG).show();
                db.close();
                return;
            }
            treinoId = novoId;
        } else {
            ContentValues valoresTreino = new ContentValues();
            valoresTreino.put("nome", nomeTreino);
            db.update("treinos", valoresTreino, "idTreino = ?", new String[]{String.valueOf(treinoId)});
            // apaga exercícios antigos do treino (vai regravar conforme listaExercicios)
            db.delete("exercicios", "treino_id = ?", new String[]{String.valueOf(treinoId)});
        }

        // grava cada exercício
        for (ExercicioTreino ex : listaExercicios) {
            ContentValues valoresEx = new ContentValues();
            valoresEx.put("nome", ex.nome);
            valoresEx.put("grupo_muscular", ex.grupo);
            valoresEx.put("descricao", "");
            valoresEx.put("link_gif", ex.gif);

            // converte series/repeticoes para inteiro (safety)
            int seriesInt = 1;
            int repsInt = 1;
            try { seriesInt = Integer.parseInt(ex.series.replaceAll("\\D+","")); } catch (Exception ignored) {}
            try { repsInt = Integer.parseInt(ex.repeticoes.replaceAll("\\D+","")); } catch (Exception ignored) {}

            valoresEx.put("series", seriesInt);
            valoresEx.put("repeticoes", repsInt);
            valoresEx.put("treino_id", treinoId);

            long res = db.insert("exercicios", null, valoresEx);
            if (res == -1) {
                Toast.makeText(this, "Erro ao salvar exercício: " + ex.nome, Toast.LENGTH_SHORT).show();
                // continua tentando salvar os outros; você pode optar por abortar aqui.
            }
        }

        db.close();
        Toast.makeText(this, "Treino salvo com sucesso!", Toast.LENGTH_SHORT).show();
        finish();
    }

    public static class ExercicioTreino {
        String nome, grupo, gif, series, repeticoes;
        public ExercicioTreino(String nome, String grupo, String gif, String series, String repeticoes) {
            this.nome = nome;
            this.grupo = grupo;
            this.gif = gif;
            this.series = series;
            this.repeticoes = repeticoes;
        }
    }
}
