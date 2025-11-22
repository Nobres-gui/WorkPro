package com.example.projeto;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SenhaLoginActivity extends AppCompatActivity {

    Button btn_login_senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senha_login);

        btn_login_senha= findViewById(R.id.btn_login_senha);
        btn_login_senha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SenhaLoginActivity.this, SenhaCadastrarActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
    }
}