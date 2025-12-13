package com.example.projeto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ExerciciosActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    ListView listaView;
    ArrayList<Exercicio> exercicios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercicios);

        // 1. Pegar ListView
        listaView = findViewById(R.id.lista_exercicios);
        listaView.setDivider(null);
        listaView.setDividerHeight(20);
        // 2. Ler JSON
        String json = lerJsonRaw();
        Gson gson = new Gson();

        // 3. Converter JSON → Lista
        Type tipo = new TypeToken<ArrayList<Exercicio>>() {}.getType();
        exercicios = gson.fromJson(json, tipo);

        // 4. Criar adapter customizado
        ExercicioAdapter adapter = new ExercicioAdapter(this, exercicios);
        listaView.setAdapter(adapter);

        // 5. Clique no item
        listaView.setOnItemClickListener((parent, view, position, id) -> {
            Exercicio e = exercicios.get(position);
        });

        /* ---- BOTTOM NAV ---- */
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


    /* ---- LER JSON DO RAW ---- */
    private String lerJsonRaw() {
        InputStream is = getResources().openRawResource(R.raw.database_exercicios);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String linha;
        try {
            while ((linha = br.readLine()) != null) sb.append(linha);
            br.close();
        } catch (IOException e) { e.printStackTrace(); }
        return sb.toString();
    }


    public class Exercicio {
        public int id;
        public String nome;
        public String grupo;
        public String descricao;
        public String gif;

        public Exercicio() {}


        public Exercicio(String nome, String grupoMuscular, String descricao, String linkGif) {
            this.nome = nome;
            this.grupo = grupoMuscular;
            this.descricao = descricao;
            this.gif = linkGif;
        }

        public String getNome() {
            return nome;
        }

        public String getGrupoMuscular() {
            return grupo;
        }

        public String getDescricao() {
            return descricao;
        }

        public String getLinkGif() {
            return gif;
        }

        // 🔹 SETTERS (Gson também usa se precisar)
        public void setNome(String nome) {
            this.nome = nome;
        }

        public void setGrupoMuscular(String grupoMuscular) {
            this.grupo = grupoMuscular;
        }

        public void setDescricao(String descricao) {
            this.descricao = descricao;
        }

        public void setLinkGif(String linkGif) {
            this.gif = linkGif;
        }
    }
}