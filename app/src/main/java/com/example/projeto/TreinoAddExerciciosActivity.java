package com.example.projeto;


import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

public class TreinoAddExerciciosActivity extends AppCompatActivity {

    LinearLayout btn_editar_treinoID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treino_add_exercicios);

        btn_editar_treinoID = findViewById(R.id.btn_editar_treinoID);
        btn_editar_treinoID.setOnClickListener(view -> {
            Intent intent = new Intent(this, TreinoAddExerciciosSelecaoActivity.class);
            startActivity(intent);
        });

    }
}