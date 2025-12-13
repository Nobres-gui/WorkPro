package com.example.projeto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BancoControllerUsuarios {

    private final CriaBanco banco;
    private final Context context;

    public BancoControllerUsuarios(Context context) {
        this.context = context;
        this.banco = new CriaBanco(context);
        criarTabelaTreinosConcluidos();
    }

    private void criarTabelaTreinosConcluidos() {
        SQLiteDatabase db = banco.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS treinos_concluidos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "usuario_id INTEGER NOT NULL, " +
                "data TEXT NOT NULL, " +
                "FOREIGN KEY(usuario_id) REFERENCES usuarios(idUsuario) ON DELETE CASCADE" +
                ");");
        db.close();
    }

    // Inserir usuário
    public long insereUsuario(String nome, int frequenciaSemana, String email, String senha) {
        SQLiteDatabase db = banco.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nome", nome);
        valores.put("email", email);
        valores.put("senha", senha);
        valores.put("frequencia_semana", frequenciaSemana);
        valores.put("streak", 0);
        valores.put("ultimo_treino", (String) null);
        valores.put("treinos_mes", 0);
        long id = db.insert("usuarios", null, valores);
        db.close();
        return id;
    }

    // Login
    public Cursor carregaDadosLogin(String email, String senha) {
        SQLiteDatabase db = banco.getReadableDatabase();
        return db.query(
                "usuarios",
                new String[]{"idUsuario", "nome", "email", "senha", "frequencia_semana", "streak", "ultimo_treino", "treinos_mes"},
                "email = ? AND senha = ?",
                new String[]{email, senha},
                null, null, null
        );
    }

    // Buscar usuário pelo ID
    public Cursor buscarUsuarioPeloID(int idUsuario) {
        SQLiteDatabase db = banco.getReadableDatabase();
        return db.query(
                "usuarios",
                new String[]{"idUsuario", "nome", "email", "frequencia_semana", "streak", "ultimo_treino", "treinos_mes"},
                "idUsuario = ?",
                new String[]{String.valueOf(idUsuario)},
                null, null, null
        );
    }

    public boolean deletarTreino(long idTreino) {
        SQLiteDatabase db = banco.getWritableDatabase();
        try {
            // Deleta os exercícios relacionados
            db.delete("exercicios", "treino_id = ?", new String[]{String.valueOf(idTreino)});
            // Deleta o treino
            int linhas = db.delete("treinos", "idTreino = ?", new String[]{String.valueOf(idTreino)});
            return linhas > 0;
        } finally {
            db.close();
        }
    }

    // Reset streak
    public void forcarStreakZero(int idUsuario) {
        SQLiteDatabase db = banco.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("streak", 0);
        db.update("usuarios", valores, "idUsuario = ?", new String[]{String.valueOf(idUsuario)});
        db.close();
    }

    // Retorna número de treinos da semana
    public int getTreinosDaSemana(int idUsuario) {
        SQLiteDatabase db = banco.getReadableDatabase();
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);

        // Ajusta para segunda-feira da semana atual
        int diff = cal.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
        if (diff < 0) diff += 7;
        cal.add(Calendar.DAY_OF_YEAR, -diff);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String inicio = sdf.format(cal.getTime());

        // Domingo da semana
        cal.add(Calendar.DAY_OF_YEAR, 6);
        String fim = sdf.format(cal.getTime());

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM treinos_concluidos WHERE usuario_id = ? AND data BETWEEN ? AND ?",
                new String[]{String.valueOf(idUsuario), inicio, fim}
        );

        int total = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) total = cursor.getInt(0);
            cursor.close();
        }

        db.close();
        return total;
    }

    // Incrementa treino + streak + treinos do mês
    public boolean incrementaTreino(int idUsuario) {
        Cursor c = buscarUsuarioPeloID(idUsuario);
        SQLiteDatabase db = banco.getWritableDatabase();
        if (c == null || !c.moveToFirst()) {
            if (c != null) c.close();
            db.close();
            return false;
        }

        int streak = c.getInt(c.getColumnIndexOrThrow("streak"));
        int treinosMes = c.getInt(c.getColumnIndexOrThrow("treinos_mes"));
        String ultimoTreino = c.getString(c.getColumnIndexOrThrow("ultimo_treino"));
        c.close();

        Calendar hoje = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String hojeStr = sdf.format(hoje.getTime());

        Calendar calUltimo = null;
        if (ultimoTreino != null) {
            try {
                calUltimo = Calendar.getInstance();
                calUltimo.setTime(sdf.parse(ultimoTreino));
            } catch (ParseException ignored) {}
        }

        // Reset mensal se mudou de mês
        if (calUltimo != null) {
            boolean mudouMes = calUltimo.get(Calendar.MONTH) != hoje.get(Calendar.MONTH)
                    || calUltimo.get(Calendar.YEAR) != hoje.get(Calendar.YEAR);
            if (mudouMes) treinosMes = 0;
        }

        int novoStreak = 1;
        if (calUltimo != null) {
            String ultStr = sdf.format(calUltimo.getTime());
            if (ultStr.equals(hojeStr)) {
                novoStreak = streak; // treino já registrado hoje
            } else {
                Calendar ontem = (Calendar) hoje.clone();
                ontem.add(Calendar.DAY_OF_YEAR, -1);
                if (ultStr.equals(sdf.format(ontem.getTime()))) novoStreak = streak + 1;
            }
        }

        treinosMes++;

        ContentValues valores = new ContentValues();
        valores.put("streak", novoStreak);
        valores.put("ultimo_treino", hojeStr);
        valores.put("treinos_mes", treinosMes);

        int linhas = db.update("usuarios", valores, "idUsuario = ?", new String[]{String.valueOf(idUsuario)});

        ContentValues cv = new ContentValues();
        cv.put("usuario_id", idUsuario);
        cv.put("data", hojeStr);
        db.insert("treinos_concluidos", null, cv);

        db.close();
        return linhas > 0;
    }
}
