package com.example.projeto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.List;

public class EditarTreinoActivity extends AppCompatActivity {

    Spinner select_seriesNomeExer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_treino);

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