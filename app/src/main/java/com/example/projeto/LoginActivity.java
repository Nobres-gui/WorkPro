package com.example.projeto;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    Button btn_login, btnGoogle, btnApple, btnFacebook;
    TextView btn_login_crieConta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnGoogle = findViewById(R.id.btn_login_google);
        btnApple = findViewById(R.id.btn_login_apple);
        btnFacebook = findViewById(R.id.btn_login_facebook);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstRun = prefs.getBoolean("firstRun", true);

        if(firstRun){
            prefs.edit().putBoolean("firstRun", false).apply();
            startActivity(new Intent(LoginActivity.this, PermissoesActivity.class));
            finish();
            return;
        }

        EditText text_login = findViewById(R.id.text_login);
        btn_login_crieConta = findViewById(R.id.btn_login_crieConta);
        btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(v -> {
            String email = text_login.getText().toString().trim();
            if(email.length() < 6){
                Toast.makeText(this,"Digite um E-mail válido!", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(LoginActivity.this, SenhaLoginActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

        btn_login_crieConta.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, CadastroUserActivity.class))
        );
        aplicarTemaNosBotoes();
    }

    private void aplicarTemaNosBotoes() {
        int currentNightMode = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;

        int textColor;

        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            // Tema escuro
            textColor = Color.WHITE;
        } else {
            // Tema claro
            textColor = Color.BLACK;
        }

        // Aplica apenas a cor do texto nos botões
        Button[] botoes = {btn_login, btnGoogle, btnApple, btnFacebook};
        for (Button btn : botoes) {
            btn.setTextColor(textColor);
        
        }
    }
}
