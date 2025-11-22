package com.example.projeto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class LoginActivity extends AppCompatActivity {

    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //Muda a cor do Botão de acordo com o tema, pois por causa da personalização ele não pode fazer automaticamente
        Button btn_login_google = findViewById(R.id.btn_login_google);
        Button btn_login_apple = findViewById(R.id.btn_login_apple);
        Button btn_login_facebook = findViewById(R.id.btn_login_facebook);

        int color = getColorFromAttr(com.google.android.material.R.attr.colorOnSurface);

        btn_login_google.setTextColor(color);
        btn_login_apple.setTextColor(color);
        btn_login_facebook.setTextColor(color);

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SenhaLoginActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
    }

    private int getColorFromAttr(int colorOnSurface) {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(colorOnSurface, typedValue, true);
        return typedValue.data;
    }
}