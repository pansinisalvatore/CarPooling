package uk.co.maxcarli.carpooling;

import android.app.AlertDialog;

//cias
public class Cittadino {
    private String nome;
    private String cognome;

    public Cittadino(String nome, String cognome){
        this.nome = nome;
        this.cognome = cognome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getNome() {
        return nome;
    }


}
