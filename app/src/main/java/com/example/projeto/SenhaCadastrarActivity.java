package com.example.projeto;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SenhaCadastrarActivity extends AppCompatActivity {

    Button btn_cadastrar_senha;
    EditText text_cadastrar_senha, text_cadastrar_senhaConfirmar;
    String nome, email;
    int frequencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senha_cadastrar);

        text_cadastrar_senha = findViewById(R.id.text_cadastrar_senha);
        text_cadastrar_senhaConfirmar = findViewById(R.id.text_cadastrar_senhaConfirmar);
        btn_cadastrar_senha = findViewById(R.id.btn_cadastrar_senha);

        nome = getIntent().getStringExtra("nome");
        email = getIntent().getStringExtra("email");
        frequencia = getIntent().getIntExtra("frequencia", 0);

        btn_cadastrar_senha.setOnClickListener(view -> {
            String senha = text_cadastrar_senha.getText().toString().trim();
            String confirmar = text_cadastrar_senhaConfirmar.getText().toString().trim();

            if (!validaDados(senha, confirmar)) return;

            BancoControllerUsuarios bd = new BancoControllerUsuarios(this);
            long id = bd.insereUsuario(nome, frequencia, email, senha);

            if(id == -1){
                Toast.makeText(this, "Erro ao cadastrar usuário.", Toast.LENGTH_LONG).show();
                return;
            }

            SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
            prefs.edit().putInt("idUsuario", (int)id).putString("email", email).apply();

            Toast.makeText(this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(SenhaCadastrarActivity.this, MainActivity.class));
            finish();
        });
    }

    private boolean validaDados(String senha, String confirmar){
        if(senha.isEmpty()){
            Toast.makeText(this,"O Campo Senha deve ser preenchido!", Toast.LENGTH_LONG).show();
            return false;
        }
        if(confirmar.isEmpty()){
            Toast.makeText(this,"O Campo CONFIRMA SENHA deve ser preenchido!", Toast.LENGTH_LONG).show();
            return false;
        }
        if(!senha.equals(confirmar)){
            Toast.makeText(this,"As senhas não estão iguais!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
