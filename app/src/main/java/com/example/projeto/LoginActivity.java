package com.example.projeto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class LoginActivity extends AppCompatActivity {



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
        
        EditText text_login = findViewById(R.id.text_login);
        text_login.setBackgroundResource(R.drawable.edit_text_custom);
    }

    private int getColorFromAttr(int colorOnSurface) {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(colorOnSurface, typedValue, true);
        return typedValue.data;
    }

}