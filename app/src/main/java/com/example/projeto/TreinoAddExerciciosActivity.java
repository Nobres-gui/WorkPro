package com.example.projeto;

import static com.example.projeto.R.id.select_seriesNomeExer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.List;

public class TreinoAddExerciciosActivity extends AppCompatActivity {
    Spinner select_seriesNomeExer;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treino_add_exercicios);


        select_seriesNomeExer = findViewById(R.id.select_seriesNomeExer);

        List<String> itens = Arrays.asList("01","02","03","04");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                itens
        );

        select_seriesNomeExer.setAdapter(adapter);
    }
}