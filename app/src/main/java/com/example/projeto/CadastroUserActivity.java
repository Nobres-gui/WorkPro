package com.example.projeto;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class CadastroUserActivity extends AppCompatActivity {

    EditText text_usuario, text_email;
    Spinner spinner_frequencia;
    Button btn_cadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_user);

        text_usuario = findViewById(R.id.text_usuario);
        text_email = findViewById(R.id.text_email);
        spinner_frequencia = findViewById(R.id.spinner_frequencia);
        btn_cadastrar = findViewById(R.id.btn_cadastrar);

        String[] frequencias = {"1","2","3","4","5","6","7"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, frequencias);
        spinner_frequencia.setAdapter(adapter);

        btn_cadastrar.setOnClickListener(v -> {
            String nome = text_usuario.getText().toString().trim();
            String email = text_email.getText().toString().trim();

            if(nome.isEmpty() || email.length() < 6) {
                Toast.makeText(this, "Atenção - preencha Nome e E-mail válidos!", Toast.LENGTH_LONG).show();
                return;
            }

            int freq = Integer.parseInt((String)spinner_frequencia.getSelectedItem());

            Intent intent = new Intent(CadastroUserActivity.this, SenhaCadastrarActivity.class);
            intent.putExtra("nome", nome);
            intent.putExtra("email", email);
            intent.putExtra("frequencia", freq);
            startActivity(intent);
        });
    }
}
