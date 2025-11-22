package com.example.projeto;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



public class SenhaCadastrarActivity extends AppCompatActivity {

    Button btn_cadastrar_senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senha_cadastrar);

        btn_cadastrar_senha = findViewById(R.id.btn_cadastrar_senha);
        btn_cadastrar_senha.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
            Intent intent = new Intent(SenhaCadastrarActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }
    });
    }
}