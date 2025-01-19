package com.example.notasdasofi.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notasdasofi.BancoDeDados.BancoDeDados;
import com.example.notasdasofi.R;
import com.example.notasdasofi.TelaDeNotas;

import java.util.ArrayList;
import java.util.List;

import Model.ModelNotas;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private String[] titulos;
    private String[] datas;
    Context context;

    public RecyclerViewAdapter(Context context, String[] titulos, String[] datas) {
        this.titulos = titulos;
        this.datas = datas;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textTitulo;
        public TextView textData;
        public ImageView icone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitulo = itemView.findViewById(R.id.textTitulo);
            textData = itemView.findViewById(R.id.textData);
            icone = itemView.findViewById(R.id.iconeLixeira);
        }
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.textTitulo.setText(titulos[position]);
        holder.textData.setText(datas[position]);

        holder.icone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Rasgar anotação")
                        .setMessage("Deseja rasgar essa página das sua notas?")
                        .setPositiveButton("Rasgá-la", (dialog, which) -> {

                            BancoDeDados bancoDeDados = new BancoDeDados(context);

                            String tituloParaExcluir = titulos[position];
                            String dataParaBuscarId = datas[position];

                            int id = bancoDeDados.BuscarIdporTituloEData(tituloParaExcluir, dataParaBuscarId);

                            if (id != -1) {
                                bancoDeDados.ExcluirAnotacao(id);
                                Toast.makeText(context, "Anotação excluída com sucesso!", Toast.LENGTH_SHORT).show();

                                List<ModelNotas> listaNotas = bancoDeDados.BuscarAnotacoes();
                                List<String> novoTitulo = new ArrayList<>();
                                List<String> novaData = new ArrayList<>();

                                for (ModelNotas anotacoes: listaNotas){
                                    novoTitulo.add(anotacoes.getTitulo());
                                    novaData.add(anotacoes.getData());
                                }
                                titulos = novoTitulo.toArray(new String[0]);
                                datas = novaData.toArray(new String[0]);

                                // Notifica o adapter que os dados mudaram
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Mantê-lá", null)
                        .show();
            }

        });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, TelaDeNotas.class);
                    intent.putExtra("titulo", titulos[position]);
                    intent.putExtra("data", datas[position]);

                    context.startActivity(intent);

                }
            });
    }

    @Override
    public int getItemCount() {
        return titulos.length;
    }
}
