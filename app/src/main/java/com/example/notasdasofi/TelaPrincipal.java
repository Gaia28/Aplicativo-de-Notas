package com.example.notasdasofi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notasdasofi.Adapter.RecyclerViewAdapter;
import com.example.notasdasofi.BancoDeDados.BancoDeDados;
import com.example.notasdasofi.BancoDeDados.ConexaoSQLite;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import Model.ModelNotas;

public class TelaPrincipal extends AppCompatActivity {


    private Context context;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        context = this; // Inicializando o contexto
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_principal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);
            return insets;
        });
        ativarModoImersao();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> new Handler().postDelayed(() -> {
            Intent in = new Intent(TelaPrincipal.this, TelaDeNotas.class);
            startActivity(in);
            finish();
        }, 200));

        listarNotas();
    }

    private void listarNotas() {
        RecyclerView recyclerView = findViewById(R.id.RecyclerView);
        BancoDeDados bancoDeDados = new BancoDeDados(this);

        List<ModelNotas> listNotas = bancoDeDados.BuscarAnotacoes();
        if (listNotas.isEmpty()) {
            Toast.makeText(this, "Nenhuma anotação encontrada", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> titulo = new ArrayList<>();
        List<String> data = new ArrayList<>();

        for (ModelNotas anotacoes : listNotas) {
            titulo.add(anotacoes.getTitulo());
            data.add(anotacoes.getData());
        }

        String[] arrayTitulo = titulo.toArray(new String[0]);
        String[] arrayData = data.toArray(new String[0]);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new RecyclerViewAdapter(context, arrayTitulo, arrayData);
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void ativarModoImersao() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
    }

}
