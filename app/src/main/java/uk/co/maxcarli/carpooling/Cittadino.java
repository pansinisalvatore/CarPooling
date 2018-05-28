package uk.co.maxcarli.carpooling;

import android.app.AlertDialog;

//cias
public class Cittadino {
    private String nome;
    private String cognome;
    private String codiceFiscale;
    private String residenza;

    public Cittadino(String nome, String cognome, String codiceFiscale, String residenza){
        this.nome = nome;
        this.cognome = cognome;
        this.codiceFiscale = codiceFiscale;
        this.residenza = residenza;
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



    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public String getResidenza() {
        return residenza;
    }

    public void setResidenza(String residenza) {
        this.residenza = residenza;
    }
}
