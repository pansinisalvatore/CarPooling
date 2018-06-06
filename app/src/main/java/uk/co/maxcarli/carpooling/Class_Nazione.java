package uk.co.maxcarli.carpooling;

public class Class_Nazione {


    String nome ;
    String cognome;
    String azienda;
    int punti;


    public Class_Nazione() {

    }

    public Class_Nazione( String nome, String cognome, String azienda, int punti) {

        this.nome = nome;
        this.cognome = cognome;
        this.azienda = azienda;
        this.punti = punti;
    }

    

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getAzienda() {
        return azienda;
    }

    public int getPunti() {
        return punti;
    }
}
