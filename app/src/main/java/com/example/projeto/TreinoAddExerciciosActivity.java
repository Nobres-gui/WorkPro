package com.example.projeto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class TreinoAddExerciciosActivity extends AppCompatActivity {

    private LinearLayout btn_editar_treinoID;
    private LinearLayout layoutExercicios, btnVoltar;
    private TextView tituloTreino;
    private long treinoId;
    private Button btnTreinoConcluido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treino_add_exercicios);

        btnVoltar = findViewById(R.id.btn_voltar);
        btnVoltar.setOnClickListener(v -> voltarPagina());

        btn_editar_treinoID = findViewById(R.id.btn_editar_treinoID);
        layoutExercicios = findViewById(R.id.lista_exercicios_adicionados);
        tituloTreino = findViewById(R.id.tituloTreino);
        btnTreinoConcluido = findViewById(R.id.btn_TreinoConcluido);

        treinoId = getIntent().getLongExtra("idTreino", -1);
        Log.d("TreinoAddExercicios", "ID do treino recebido: " + treinoId);

        carregarTituloTreino();
        carregarExerciciosDoBanco();

        btn_editar_treinoID.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditarTreinoActivity.class);
            intent.putExtra("idTreino", treinoId);
            startActivity(intent);
        });

        btnTreinoConcluido.setOnClickListener(v -> registrarTreinoConcluido());
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

    private void registrarTreinoConcluido() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        int idUsuario = prefs.getInt("idUsuario", -1);

        if (idUsuario == -1) {
            Toast.makeText(this, "Erro: usuário não identificado. Faça login novamente.", Toast.LENGTH_LONG).show();
            return;
        }

        BancoControllerUsuarios controller = new BancoControllerUsuarios(this);
        boolean ok = controller.incrementaTreino(idUsuario);

        if (!ok) {
            Toast.makeText(this, "Erro ao registrar treino!", Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(this, "Treino concluído! 🔥", Toast.LENGTH_SHORT).show();

        // volta para MainActivity e força refresh
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarExerciciosDoBanco();
        carregarTituloTreino();
    }

    private void carregarTituloTreino() {
        CriaBanco banco = new CriaBanco(this);
        SQLiteDatabase db = banco.getReadableDatabase();

        if (treinoId == -1) {
            tituloTreino.setText("Seu Treino");
            db.close();
            return;
        }

        Cursor cursor = db.rawQuery("SELECT nome FROM treinos WHERE idTreino = ?", new String[]{String.valueOf(treinoId)});
        if (cursor.moveToFirst()) tituloTreino.setText(cursor.getString(0));
        cursor.close();
        db.close();
    }

    private void carregarExerciciosDoBanco() {
        layoutExercicios.removeAllViews();
        CriaBanco banco = new CriaBanco(this);
        SQLiteDatabase db = banco.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT idExercicio, nome, grupo_muscular, link_gif FROM exercicios WHERE treino_id = ?",
                new String[]{String.valueOf(treinoId)}
        );

        while (cursor.moveToNext()) {
            long idExercicio = cursor.getLong(0);
            String nome = cursor.getString(1);
            String grupo = cursor.getString(2);
            String gif = cursor.getString(3);
            adicionarExercicioNaTela(idExercicio, nome, grupo, gif);
        }

        cursor.close();
        db.close();
    }

    private void adicionarExercicioNaTela(long idExercicio, String nome, String grupo, String gif) {
        View card = getLayoutInflater().inflate(R.layout.item_exercicio, null);
        card.setTag(idExercicio);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        int marginPx = (int) (10 * getResources().getDisplayMetrics().density);
        params.setMargins(0, 0, 0, marginPx);
        card.setLayoutParams(params);

        TextView txtNome = card.findViewById(R.id.txtNome);
        TextView txtGrupo = card.findViewById(R.id.txtGrupo);
        ImageView img = card.findViewById(R.id.imgExercicio);

        txtNome.setText(nome);
        txtGrupo.setText(grupo);

        if (gif != null && !gif.isEmpty()) {
            Glide.with(this).asGif().load(gif).into(img);
        } else {
            img.setImageResource(R.drawable.ic_launcher_foreground);
        }

        layoutExercicios.addView(card);
    }

}
