package com.example.notasdasofi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.notasdasofi.BancoDeDados.BancoDeDados;

import Model.ModelNotas;

public class TelaDeNotas extends AppCompatActivity {

    private BancoDeDados bancoDeDados;
    private EditText editTextTitulo, editTextAnotacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_de_notas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);
            return insets;

        });
        ativarModoImersao();
        IniciarComponentes();
        CarregarNotaEdicao();

        View layouAtual = findViewById(R.id.main);
        layouAtual.requestFocus();
    }
    private boolean NotaSalva = false;

    public void SalvarNotas(View view) {

        bancoDeDados = new BancoDeDados(this);
       String Titulo =  editTextTitulo.getText().toString();
       String Anotacao = editTextAnotacao.getText().toString();
       String data = getIntent().getStringExtra("data");

        try {
            int idExistente = -1;

            if (data != null){
                idExistente = bancoDeDados.VerificarExistenciaNota(data);

            }

               ModelNotas novaAnotacao = new ModelNotas(Titulo, Anotacao);
               boolean sucesso;

               if (idExistente != -1){
                   novaAnotacao.setId(idExistente);
                   sucesso = bancoDeDados.atualizarAnotacao(novaAnotacao);
                   tirarFocoEditText();

                   if (sucesso){
                       Toast.makeText(this, "Anotação atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                       NotaSalva = true;
                       VoltarTelaPrincipal(view);

                   }
            }else {
                   sucesso = bancoDeDados.InserirAnotacao(novaAnotacao);
                   if (sucesso) {
                       Toast.makeText(this, "Anotação salva com sucesso!", Toast.LENGTH_SHORT).show();
                      NotaSalva = true;
                       VoltarTelaPrincipal(view);
                   }
               }

           }catch(Exception e){
               Toast.makeText(this, "Ocorreu um erro ao salvar sua anotação. Tente novamente.", Toast.LENGTH_SHORT).show();
               e.printStackTrace();
           }

    }

    public void VoltarTelaPrincipal(View view) {

        if (NotaSalva) {

            Intent in = new Intent(TelaDeNotas.this, TelaPrincipal.class);
            startActivity(in);
            finish();
        }else {

            new AlertDialog.Builder(this)
                    .setTitle("Sair sem salvar")
                    .setMessage("Anotação não registrada. Deseja sair sem salvar?")
                    .setPositiveButton("Sim", (dialog, which) -> {
                        // Fecha a atividade e volta para a tela principal
                        Intent in = new Intent(TelaDeNotas.this, TelaPrincipal.class);
                        startActivity(in);
                        finish();
                    })
                    .setNegativeButton("Não", (dialog, which) -> {
                        // Fecha o diálogo
                        dialog.dismiss();
                    })
                    .show();

        }

    }
    public void CarregarNotaEdicao(){
        String titulo = getIntent().getStringExtra("titulo");
        String data = getIntent().getStringExtra("data");

        bancoDeDados = new BancoDeDados(this);
        int id = bancoDeDados.BuscarIdporTituloEData(titulo, data);

        if (id != -1){
            ModelNotas anotacao = bancoDeDados.BuscarNotaPorId(id);

            if (anotacao!= null){
                editTextTitulo.setText(anotacao.getTitulo());
                editTextAnotacao.setText(anotacao.getAnotacao());
            }

        }
    }

    public void tirarFocoEditText(){
        View focoAtual = getCurrentFocus();
        if (focoAtual != null){
            focoAtual.clearFocus();
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (focoAtual != null) {
            imm.hideSoftInputFromWindow(focoAtual.getWindowToken(), 0);
        }

    }

    public void IniciarComponentes(){
        editTextTitulo = findViewById(R.id.EditTextTitulo);
        editTextAnotacao = findViewById(R.id.EditTextNotas);

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