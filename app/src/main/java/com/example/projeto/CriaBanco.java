package com.example.projeto;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CriaBanco extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "banco_exemplo.db";
    private static final int VERSAO = 3;

    public CriaBanco(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlUsuario = "CREATE TABLE IF NOT EXISTS usuarios (" +
                "idUsuario INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT NOT NULL, " +
                "email TEXT UNIQUE NOT NULL, " +
                "senha TEXT NOT NULL, " +
                "frequencia_semana INTEGER DEFAULT 0, " +
                "streak INTEGER DEFAULT 0, " +
                "ultimo_treino TEXT, " +
                "treinos_mes INTEGER DEFAULT 0" +
                ");";
        db.execSQL(sqlUsuario);

        String sqlTreino = "CREATE TABLE IF NOT EXISTS treinos (" +
                "idTreino INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT NOT NULL, " +
                "usuario_id INTEGER NOT NULL, " +
                "FOREIGN KEY(usuario_id) REFERENCES usuarios(idUsuario) ON DELETE CASCADE" +
                ");";
        db.execSQL(sqlTreino);

        String sqlExercicios = "CREATE TABLE IF NOT EXISTS exercicios (" +
                "idExercicio INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT NOT NULL, " +
                "grupo_muscular TEXT NOT NULL, " +
                "descricao TEXT, " +
                "link_gif TEXT, " +
                "series INTEGER, " +
                "repeticoes INTEGER, " +
                "treino_id INTEGER NOT NULL, " +
                "FOREIGN KEY(treino_id) REFERENCES treinos(idTreino) ON DELETE CASCADE" +
                ");";
        db.execSQL(sqlExercicios);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS exercicios;");
        db.execSQL("DROP TABLE IF EXISTS treinos;");
        db.execSQL("DROP TABLE IF EXISTS usuarios;");
        onCreate(db);
    }
}
