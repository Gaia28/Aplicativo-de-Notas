package Model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ModelNotas {

    private int id;
    private String titulo;
    private String anotacao;
    private String data;


    public ModelNotas(int id, String titulo, String anotacao, String data) {
        this.id = id;
        this.titulo = titulo;
        this.anotacao = anotacao;
        this.data = data;
    }

    public ModelNotas(String titulo, String texto) {
        this.titulo = titulo;
        this.anotacao = texto;
        this.data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }
    public ModelNotas(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAnotacao() {
        return anotacao;
    }

    public void setAnotacao(String anotacao) {
        this.anotacao = anotacao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
