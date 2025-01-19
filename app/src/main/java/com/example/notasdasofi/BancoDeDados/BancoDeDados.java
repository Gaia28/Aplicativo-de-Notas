package com.example.notasdasofi.BancoDeDados;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import Model.ModelNotas;

public class BancoDeDados extends SQLiteOpenHelper {

    /*------DADOS DO BANCO-----*/
    private static final String NOME_BANCO = "Notas.db";
    private static final int VERSAO_BANCO = 1;
    /*-------------------------*/

    /*-------CONSULTAS SQL--------*/
    private static final String CRIAR_TABELA = "CREATE TABLE IF NOT EXISTS anotacoes (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "titulo TEXT NOT NULL, anotacao TEXT NOT NULL, data TEXT NOT NULL);";

    private static final String BUSCAR_NOTAS = "SELECT *FROM anotacoes ORDER BY id DESC";
    private static final String BUSCAR_ID = "SELECT id from anotacoes WHERE titulo = ? AND data = ?";
    private static final String BUSCAR_NOTA_ID = "SELECT *FROM anotacoes WHERE id = ?";

    /*-------------------------------*/

    /*------CRIAÇÃO DAS ENTIDADES SQL-----*/
    public BancoDeDados(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CRIAR_TABELA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS anotacoes");
        onCreate(database);
    }
    /*---------------------------------------*/


    /*-----------RELAÇÃO DO APP COM O BANCO DE DADOS------------*/
    public boolean InserirAnotacao(ModelNotas notas) {

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        long resultado = -1;
        try {


            valores.put("titulo", notas.getTitulo());
            valores.put("anotacao", notas.getAnotacao());
            valores.put("data", notas.getData());

            resultado = database.insert("anotacoes", null, valores);

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            database.close();
        }
        return resultado != -1;
    }

    public boolean ExcluirAnotacao(int id) {

        SQLiteDatabase database = this.getWritableDatabase();
        int resultado = 0;

        try {
            resultado = database.delete("anotacoes", "id = ?", new String[]{String.valueOf(id)});

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            database.close();
        }
        return resultado > 0;
    }

    public int BuscarIdporTituloEData(String titulo, String data){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = null;
        int id = -1;

        try {
            cursor = database.rawQuery(BUSCAR_ID, new String[]{titulo, data});

            if (cursor.moveToNext()){


                id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            }

        }catch (Exception e){
            e.printStackTrace();

        }finally {
            database.close();
        }

        return id;
    }

    public ModelNotas BuscarNotaPorId(int id){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = null;
        ModelNotas nota = null;

        try {
            cursor = database.rawQuery(BUSCAR_NOTA_ID, new String[]{String.valueOf(id)});

            if (cursor.moveToNext()){

                nota = new ModelNotas();
                nota.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                nota.setTitulo(cursor.getString(cursor.getColumnIndexOrThrow("titulo")));
                nota.setAnotacao(cursor.getString(cursor.getColumnIndexOrThrow("anotacao")));
                nota.setData(cursor.getString(cursor.getColumnIndexOrThrow("data")));
            }

        }catch (Exception e){
            e.printStackTrace();

        }finally {
            database.close();
        }
        return nota;

    }

    public List<ModelNotas> BuscarAnotacoes() {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = null;
        List<ModelNotas> listNotas = new ArrayList<>();

        try {
            cursor = database.rawQuery(BUSCAR_NOTAS, null);

            while (cursor.moveToNext()) {
                ModelNotas anotacao = new ModelNotas();
                anotacao.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                anotacao.setTitulo(cursor.getString(cursor.getColumnIndexOrThrow("titulo")));
                anotacao.setAnotacao(cursor.getString(cursor.getColumnIndexOrThrow("anotacao")));
                anotacao.setData(cursor.getString(cursor.getColumnIndexOrThrow("data")));

                listNotas.add(anotacao);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close(); // Fecha o cursor
            }
            database.close(); // Fecha o banco
        }
        return listNotas;
    }

    public int VerificarExistenciaNota(String data){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = null;
        int id = -1;
        String querry = "SELECT id FROM anotacoes WHERE data = ?";

        try {
            cursor = database.rawQuery(querry, new String[]{data});

            if (cursor.moveToNext()){
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));

            }

        }catch (Exception e){

            e.printStackTrace();
        }finally {
            database.close();
        }
            return id;

    }

   // Atualizar uma anotação
     public boolean atualizarAnotacao(ModelNotas anotacao) {
         SQLiteDatabase db = this.getWritableDatabase();
         ContentValues valores = new ContentValues();

        valores.put("titulo", anotacao.getTitulo());
        valores.put("anotacao", anotacao.getAnotacao());
        valores.put("data", anotacao.getData());
         int resultado = db.update("anotacoes", valores, "id = ?", new String[]{String.valueOf(anotacao.getId())});
         return resultado > 0;
     }

}


/**
 *
 *
 *
 *     // Consultar todas as anotações
 *     public List<Anotacao> listarAnotacoes() {
 *         List<Anotacao> lista = new ArrayList<>();
 *         SQLiteDatabase db = this.getReadableDatabase();
 *         Cursor cursor = db.rawQuery("SELECT * FROM " + TABELA_ANOTACOES + " ORDER BY " + COLUNA_DATA + " DESC", null);
 *
 *         if (cursor.moveToFirst()) {
 *             do {
 *                 Anotacao anotacao = new Anotacao();
 *                 anotacao.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUNA_ID)));
 *                 anotacao.setTitulo(cursor.getString(cursor.getColumnIndexOrThrow(COLUNA_TITULO)));
 *                 anotacao.setTexto(cursor.getString(cursor.getColumnIndexOrThrow(COLUNA_TEXTO)));
 *                 anotacao.setData(cursor.getString(cursor.getColumnIndexOrThrow(COLUNA_DATA)));
 *
 *                 lista.add(anotacao);
 *             } while (cursor.moveToNext());
 *         }
 *         cursor.close();
 *         return lista;
 *     }
 *
 *     // Excluir uma anotação
 *     public boolean excluirAnotacao(int id) {
 *         SQLiteDatabase db = this.getWritableDatabase();
 *         int resultado = db.delete(TABELA_ANOTACOES, COLUNA_ID + " = ?", new String[]{String.valueOf(id)});
 *         return resultado > 0;
 *     }
 *
 *
 * }
 */
