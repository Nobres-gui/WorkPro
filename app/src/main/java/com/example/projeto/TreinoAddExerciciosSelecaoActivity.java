package com.example.projeto;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class TreinoAddExerciciosSelecaoActivity extends AppCompatActivity {

    private ListView listaView;
    private LinearLayout btnVoltar;
    private ArrayList<ExerciciosActivity.Exercicio> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treino_add_exercicios_selecao);

        btnVoltar = findViewById(R.id.btn_voltar);
        btnVoltar.setOnClickListener(v -> voltarPagina());

        listaView = findViewById(R.id.lista_exercicios);
        listaView.setDivider(null);
        listaView.setDividerHeight(20);

        String json = lerJsonRaw();
        if (json.isEmpty()) {
            Toast.makeText(this, "Erro ao carregar exercícios", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Gson gson = new Gson();
        Type tipo = new TypeToken<ArrayList<ExerciciosActivity.Exercicio>>(){}.getType();
        lista = gson.fromJson(json, tipo);

        ExercicioAdapter adapter = new ExercicioAdapter(this, lista);
        listaView.setAdapter(adapter);

        listaView.setOnItemClickListener((parent, view, position, id) -> {
            ExerciciosActivity.Exercicio e = lista.get(position);

            Intent intent = new Intent();
            intent.putExtra("exercicio_nome", e.nome);
            intent.putExtra("exercicio_grupo", e.grupo);
            intent.putExtra("exercicio_gif", e.gif);

            setResult(RESULT_OK, intent);
            finish();
        });
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
    private String lerJsonRaw() {
        InputStream is = getResources().openRawResource(R.raw.database_exercicios);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String linha;
        try {
            while ((linha = br.readLine()) != null) sb.append(linha);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return sb.toString();
    }
}
