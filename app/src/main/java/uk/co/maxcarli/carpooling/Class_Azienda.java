package uk.co.maxcarli.carpooling;

public class Class_Azienda {
    int posizione;
    String nome;
    String cognome;
    int punti;

    public Class_Azienda() {
    }

    public Class_Azienda(int posizione, String nome, String cognome, int punti) {
        this.posizione = posizione;
        this.nome = nome;
        this.cognome = cognome;
        this.punti = punti;
    }

    public int getPosizione() {
        return posizione;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public int getPunti() {
        return punti;
    }
}
