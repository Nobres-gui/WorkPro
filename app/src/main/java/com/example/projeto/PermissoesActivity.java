package com.example.projeto;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PermissoesActivity extends AppCompatActivity {

    private static final int ALL_PERMISSIONS_CODE = 102;
    private Button btn_permissoes;

    /*Executado quando o aplicativo é pela primeira vez iniciado para pegar as permissões*/
    SharedPreferences sPreferences = null;

    @Override
    public void onCreate(Bundle cicle) {
        super.onCreate(cicle);
        setContentView(R.layout.activity_permissoes); // Coloque seu Layout aqui!

        btn_permissoes = findViewById(R.id.btn_permissoes);

        // SharedPreferences
        sPreferences = getSharedPreferences("firstRun", MODE_PRIVATE);
        btn_permissoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Quando o botão é clicado, verificamos as permissões.
                requestAppPermissions();
            }
        });
    }

        private void requestAppPermissions () {
            // Lista de permissões que precisamos solicitar
            String[] permissionsToRequest = {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    // Adiciona a permissão de notificação se for Android 13+
                    // Caso contrário, ela será ignorada ou já concedida automaticamente
            };

            // Vamos verificar quais permissões ainda não foram concedidas
            List<String> listPermissionsNeeded = new ArrayList<>();

            for (String perm : permissionsToRequest) {
                if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(perm);
                }
            }

            // Adiciona a permissão de notificação para Android 13+ especificamente
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(Manifest.permission.POST_NOTIFICATIONS);
                }
            }

            // Se houver permissões a serem solicitadas, mostre o diálogo do sistema
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                        ALL_PERMISSIONS_CODE);
            } else {
                // Todas as permissões já foram concedidas
                handleAllPermissionsGranted();
            }
        }

    // Lida com o resultado da solicitação de permissão do sistema
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == ALL_PERMISSIONS_CODE) {
            // Verifica se TODAS as permissões necessárias foram concedidas
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break; // Sai do loop assim que encontrar uma negada
                }
            }
        }
    }

    // Método chamado quando todas as permissões necessárias (do sistema) são tratadas
    private void handleAllPermissionsGranted() {
        //Vai para a tela de Login
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    @Override
    public void onResume () {
        super.onResume();

        if (sPreferences.getBoolean("firstRun", true)) {
            sPreferences.edit().putBoolean("firstRun", false).apply();
            Toast.makeText(getApplicationContext(), "WorkPro", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "WorkPro...", Toast.LENGTH_LONG).show();
        }
    }
}