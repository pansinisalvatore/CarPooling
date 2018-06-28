package uk.co.maxcarli.carpooling.model;

import java.io.Serializable;
import java.util.ArrayList;

//cias
public class Cittadino implements Serializable{

    public static enum TipoCittadino {Normale,MobilityManager};

    private int idCittadino;
    private String nome;
    private String cognome;
    private String codiceFiscale;
    private String residenza;
    private String numeroTelefono;
    private TipoCittadino tipoCittadino;
    private Sede sede;

    public final ArrayList<PassaggioOfferto> passaggiOfferti;
    public final ArrayList<PassaggioRichiesto> passaggiRichiesti;


    public Cittadino() {

        passaggiOfferti=new ArrayList<>();
        passaggiRichiesti=new ArrayList<>();
    }

    public Cittadino(ArrayList<PassaggioOfferto> passaggiOfferti, ArrayList<PassaggioRichiesto> passaggiRichiesti){
        this.passaggiOfferti=passaggiOfferti;
        this.passaggiRichiesti=passaggiRichiesti;
    }



    public int getIdCittadino() {
        return idCittadino;
    }

    public void setIdCittadino(int idCittadino) {
        this.idCittadino = idCittadino;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
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

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public TipoCittadino getTipoCittadino() {
        return tipoCittadino;
    }

    public void setTipoCittadino(TipoCittadino tipoCittadino) {
        this.tipoCittadino = tipoCittadino;
    }

    public Sede getSede() {
        return sede;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
    }















}
