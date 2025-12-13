package com.example.projeto;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SenhaLoginActivity extends AppCompatActivity {

    private Button btn_login_senha;
    private EditText text_login_senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senha_login);

        text_login_senha = findViewById(R.id.text_login_senha);
        btn_login_senha = findViewById(R.id.btn_login_senha);

        btn_login_senha.setOnClickListener(v -> {
            if(validaDados()){
                startActivity(new Intent(SenhaLoginActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private boolean validaDados(){
        String senha = text_login_senha.getText().toString().trim();
        String email = getIntent().getStringExtra("email");

        if(email == null || email.isEmpty()){
            Toast.makeText(this,"ERRO: email não foi recebido!",Toast.LENGTH_LONG).show();
            return false;
        }
        if(senha.isEmpty()){
            Toast.makeText(this,"Digite sua senha!",Toast.LENGTH_LONG).show();
            return false;
        }

        BancoControllerUsuarios bd = new BancoControllerUsuarios(this);
        Cursor dados = bd.carregaDadosLogin(email, senha);

        if(dados != null && dados.moveToFirst()){
            int idUsuario = dados.getInt(dados.getColumnIndexOrThrow("idUsuario"));
            String nome = dados.getString(dados.getColumnIndexOrThrow("nome"));
            int freq = dados.getInt(dados.getColumnIndexOrThrow("frequencia_semana"));

            SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
            prefs.edit().putInt("idUsuario", idUsuario)
                    .putString("email", email)
                    .putString("nome", nome)
                    .putInt("frequencia", freq)
                    .apply();

            dados.close();
            return true;
        } else {
            Toast.makeText(this,"Email ou senha inválidos!",Toast.LENGTH_LONG).show();
            if(dados != null) dados.close();
            return false;
        }
    }
}
