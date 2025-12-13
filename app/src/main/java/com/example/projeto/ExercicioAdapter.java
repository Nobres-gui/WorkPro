package com.example.projeto;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ExercicioAdapter extends ArrayAdapter<ExerciciosActivity.Exercicio> {

    private final ArrayList<ExerciciosActivity.Exercicio> lista;

    public ExercicioAdapter(@NonNull Context context, ArrayList<ExerciciosActivity.Exercicio> lista) {
        super(context, 0, lista);
        this.lista = lista;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_exercicio, parent, false);
        }

        // Pegar item atual
        ExerciciosActivity.Exercicio exercicio = lista.get(position);

        // Vincular elementos do XML
        TextView txtNome = convertView.findViewById(R.id.txtNome);
        TextView txtGrupo = convertView.findViewById(R.id.txtGrupo);
        ImageView imgExercicio = convertView.findViewById(R.id.imgExercicio);

        // Preencher dados
        txtNome.setText(exercicio.nome);
        txtGrupo.setText(exercicio.grupo);

        // Log para depuração e carregamento do GIF
        if (exercicio.gif == null || exercicio.gif.isEmpty()) {
            Log.d("ExercicioAdapter", "GIF está nulo ou vazio para: " + exercicio.nome);
            imgExercicio.setImageResource(R.drawable.ic_launcher_foreground);
        } else {
            Glide.with(getContext())
                    .asGif()
                    .load(exercicio.gif)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(imgExercicio);

            Log.d("ExercicioAdapter", "Carregando GIF para: " + exercicio.nome + " Link: " + exercicio.gif);
        }

        return convertView;
    }
}
