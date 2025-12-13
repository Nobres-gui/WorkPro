package com.example.projeto;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kotlin.jvm.internal.Lambda;

public class EditarTreinoActivity extends AppCompatActivity {

    private LinearLayout btnNovoExercicio, layoutExercicios, btnVoltar;
    private Button btnSalvarTreino;
    private ImageView btnDeletarTreino;
    private EditText editarTitulo;
    private long idTreino = -1;
    private List<String> listaSpinner = Arrays.asList("01","02","03","04");
    private ArrayList<ExercicioTreino> listaExercicios = new ArrayList<>();

    private final ActivityResultLauncher<Intent> adicionarExercicioLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if(result.getResultCode() == RESULT_OK && result.getData()!=null){
                    Intent data = result.getData();
                    String nome = data.getStringExtra("exercicio_nome");
                    String grupo = data.getStringExtra("exercicio_grupo");
                    String gif = data.getStringExtra("exercicio_gif");
                    adicionarExercicioNaTela(-1, nome, grupo, gif, "01","01");
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_treino);

        btnVoltar = findViewById(R.id.btn_voltar);
        btnVoltar.setOnClickListener(v -> voltarPagina());

        btnDeletarTreino = findViewById(R.id.btnDeletarTreino);
        btnNovoExercicio = findViewById(R.id.btn_novoExercicio);
        layoutExercicios = findViewById(R.id.lista_exercicios_adicionados);
        btnSalvarTreino = findViewById(R.id.btn_salvarTreino);
        editarTitulo = findViewById(R.id.editar_tituloTreino);

        idTreino = getIntent().getLongExtra("idTreino",-1);

        carregarTreino();
        carregarExerciciosDoTreino();

        btnNovoExercicio.setOnClickListener(v -> {
            Intent intent = new Intent(this, TreinoAddExerciciosSelecaoActivity.class);
            adicionarExercicioLauncher.launch(intent);
        });

        btnDeletarTreino.setOnClickListener(v -> mostrarConfirmacaoDeletar());

        btnSalvarTreino.setOnClickListener(v -> salvarTreino());
    }
    private void voltarPagina(){
        String origem = getIntent().getStringExtra("origem");
        Intent intent;

        if(origem == null){
            // fallback
            finish();
            return;
        }

        switch (origem){
            case "TreinoActivity":
                intent = new Intent(this, TreinoActivity.class);
                break;
            case "TreinoAddExerciciosActivity":
                intent = new Intent(this, TreinoAddExerciciosActivity.class);
                break;
            // adicione outros casos se precisar
            default:
                finish();
                return;
        }

        // Se quiser passar o idTreino de volta
        intent.putExtra("idTreino", idTreino);
        startActivity(intent);
        finish();
    }
    private void mostrarConfirmacaoDeletar() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar Exclusão")
                .setMessage("Tem certeza que deseja deletar este treino?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    BancoControllerUsuarios controller = new BancoControllerUsuarios(EditarTreinoActivity.this);
                    boolean deletou = controller.deletarTreino(idTreino);

                    if(deletou) {
                        Toast.makeText(EditarTreinoActivity.this, "Treino deletado com sucesso!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditarTreinoActivity.this, TreinoActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(EditarTreinoActivity.this, "Erro ao deletar treino.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void carregarTreino(){
        if(idTreino==-1) return;

        CriaBanco banco = new CriaBanco(this);
        SQLiteDatabase db = banco.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nome FROM treinos WHERE idTreino=?", new String[]{String.valueOf(idTreino)});

        if(cursor.moveToFirst()) editarTitulo.setText(cursor.getString(0));
        cursor.close();
        db.close();
    }

    private void carregarExerciciosDoTreino(){
        listaExercicios.clear();
        layoutExercicios.removeAllViews();

        CriaBanco banco = new CriaBanco(this);
        SQLiteDatabase db = banco.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT idExercicio,nome,grupo_muscular,link_gif,series,repeticoes FROM exercicios WHERE treino_id=?", new String[]{String.valueOf(idTreino)});

        while(cursor.moveToNext()){
            long idExercicio = cursor.getLong(0);
            String nome = cursor.getString(1);
            String grupo = cursor.getString(2);
            String gif = cursor.getString(3);
            String series = cursor.getString(4);
            String repeticoes = cursor.getString(5);
            adicionarExercicioNaTela(idExercicio,nome,grupo,gif,series,repeticoes);
        }

        cursor.close();
        db.close();
    }

    private void adicionarExercicioNaTela(long idExercicio, String nome, String grupo, String gif, String series, String repeticoes){
        View card = getLayoutInflater().inflate(R.layout.item_edicao_exercicio,null);
        card.setTag(idExercicio);

        TextView txtNome = card.findViewById(R.id.txtNome);
        Spinner spSeries = card.findViewById(R.id.select_seriesExer);
        Spinner spRepeticoes = card.findViewById(R.id.select_RepeticoesExer);

        txtNome.setText(nome);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listaSpinner);
        spSeries.setAdapter(adapter);
        spRepeticoes.setAdapter(adapter);

        spSeries.setSelection(listaSpinner.indexOf(series));
        spRepeticoes.setSelection(listaSpinner.indexOf(repeticoes));

        ExercicioTreino exercicio = new ExercicioTreino(nome,grupo,gif,series,repeticoes);
        listaExercicios.add(exercicio);

        spSeries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { exercicio.series = listaSpinner.get(position);}
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
        spRepeticoes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { exercicio.repeticoes = listaSpinner.get(position);}
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        layoutExercicios.addView(card);
    }

    private void salvarTreino(){
        if(idTreino==-1) return;

        CriaBanco banco = new CriaBanco(this);
        SQLiteDatabase db = banco.getWritableDatabase();

        String novoNome = editarTitulo.getText().toString().trim();
        if(novoNome.isEmpty()) novoNome = "Treino Sem Nome";

        ContentValues valoresTreino = new ContentValues();
        valoresTreino.put("nome", novoNome);
        db.update("treinos", valoresTreino, "idTreino=?", new String[]{String.valueOf(idTreino)});

        db.delete("exercicios", "treino_id=?", new String[]{String.valueOf(idTreino)});

        for(ExercicioTreino ex : listaExercicios){
            ContentValues valoresEx = new ContentValues();
            valoresEx.put("nome", ex.nome);
            valoresEx.put("grupo_muscular", ex.grupo);
            valoresEx.put("descricao", "");
            valoresEx.put("link_gif", ex.gif);
            valoresEx.put("series", ex.series);
            valoresEx.put("repeticoes", ex.repeticoes);
            valoresEx.put("treino_id", idTreino);
            db.insert("exercicios", null, valoresEx);
        }

        db.close();
        finish();
    }

    public static class ExercicioTreino{
        String nome, grupo, gif, series, repeticoes;
        public ExercicioTreino(String nome,String grupo,String gif,String series,String repeticoes){
            this.nome = nome;
            this.grupo = grupo;
            this.gif = gif;
            this.series = series;
            this.repeticoes = repeticoes;
        }
    }
}
