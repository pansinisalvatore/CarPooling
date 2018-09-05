package uk.co.maxcarli.carpooling.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



//cias
public class Cittadino implements Parcelable{


    public int getPunteggio() {
        return punteggio;
    }

    public void setPunteggio(int punteggio) {
        this.punteggio = punteggio;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getConvalidato() {
        return convalidato;
    }

    public void setConvalidato(int convalidato) {
        this.convalidato = convalidato;
    }

    public int getNotificaPassaggio() {
        return notificaPassaggio;
    }

    public void setNotificaPassaggio(int notificaPassaggio) {
        this.notificaPassaggio = notificaPassaggio;
    }

    public int getNotificaAutorizzazione() {
        return notificaAutorizzazione;
    }

    public void setNotificaAutorizzazione(int notificaAutorizzazione) {
        this.notificaAutorizzazione = notificaAutorizzazione;
    }

    public interface Keys {
        String IDCITTADINO = "idCittadino";
        String NOME = "nome";
        String COGNOME = "cognome";
        String CODICEFISCALE= "codiceFiscale";
        String RESIDENZA = "residenza";
        String NUMEROTELEFONO = "numeroTelefono";
        String TIPOCITTADINO = "tipoCittadino";
        String PUNTEGGIO="punteggio";
        String MACADDRESS = "macAddress";
    }

    private static final byte PRESENT=1;
    private static final byte NOT_PRESENT=0;




    private int idCittadino;
    private String nome;
    private String cognome;
    private String codiceFiscale;
    private String residenza;
    private String numeroTelefono;
    private String tipoCittadino;
    private int punteggio;
    private String macAddress;
    private String email;
    private String password;
    private Sede sede;
    private  int convalidato;

    private int notificaPassaggio;

    private int notificaAutorizzazione;


    public final ArrayList<Passaggio> passaggiOfferti;
    public final ArrayList<Passaggio> passaggiRichiesti;


    public Cittadino() {

        passaggiOfferti=new ArrayList<>();
        passaggiRichiesti=new ArrayList<>();
        punteggio=0;
        idCittadino=0;
        nome = "";
        cognome = "";
        codiceFiscale = "";
        residenza = "";
        numeroTelefono = "";
        tipoCittadino = "";
        macAddress = "";
        email="";
        password="";
        convalidato=0;
        notificaPassaggio=0;
        notificaAutorizzazione=0;
    }

    public Cittadino(ArrayList<Passaggio> passaggiOfferti, ArrayList<Passaggio> passaggiRichiesti){
        this.passaggiOfferti=passaggiOfferti;
        this.passaggiRichiesti=passaggiRichiesti;
        punteggio=0;
        idCittadino=0;
        nome = "";
        cognome = "";
        codiceFiscale = "";
        residenza = "";
        numeroTelefono = "";
        tipoCittadino = "";
        macAddress = "";
        email="";
        password="";
        convalidato=0;
        notificaPassaggio=0;
        notificaAutorizzazione=0;
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

    public String getTipoCittadino() {
        return tipoCittadino;
    }

    public void setTipoCittadino(String tipoCittadino) {
        this.tipoCittadino = tipoCittadino;
    }

    public Sede getSede() {
        return sede;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
    }


    public void addPassaggioRichiesto(final Passaggio p){
        passaggiRichiesti.add(p);
    }

    public void addPassaggioOfferto(final Passaggio p){
        passaggiOfferti.add(p);
    }


    public Passaggio getPassaggioRichiesto(int i){
        return passaggiRichiesti.get(i);
    }

    public Passaggio getPassaggioOfferto(int i){
        return passaggiOfferti.get(i);
    }

    private Cittadino(Parcel in) {
        idCittadino = in.readInt();
        nome = in.readString();
        cognome = in.readString();
        codiceFiscale = in.readString();
        residenza = in.readString();
        numeroTelefono = in.readString();
        tipoCittadino = in.readString();
        email=in.readString();
        password=in.readString();
        punteggio=in.readInt();
        macAddress=in.readString();
        convalidato=in.readInt();
        notificaPassaggio=in.readInt();
        notificaAutorizzazione=in.readInt();
        if(in.readByte()==PRESENT){
            sede = (Sede) in.readParcelable(Sede.class.getClassLoader());
        }else{
            sede = null;
        }
        if (in.readByte() == PRESENT) {
            passaggiOfferti = new ArrayList<Passaggio>();
            in.readTypedList(passaggiOfferti, Passaggio.CREATOR);
        } else {
            passaggiOfferti = null;
        }
        if (in.readByte() == PRESENT) {
            passaggiRichiesti = new ArrayList<Passaggio>();
            in.readTypedList(passaggiRichiesti, Passaggio.CREATOR);
        } else {
            passaggiRichiesti = null;
        }
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idCittadino);
        dest.writeString(nome);
        dest.writeString(cognome);
        dest.writeString(codiceFiscale);
        dest.writeString(residenza);
        dest.writeString(numeroTelefono);
        dest.writeString(tipoCittadino);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeInt(punteggio);
        dest.writeString(macAddress);
        dest.writeInt(convalidato);
        dest.writeInt(notificaPassaggio);
        dest.writeInt(notificaAutorizzazione);
        if(sede==null){
            dest.writeByte(NOT_PRESENT);
        }else {
            dest.writeByte(PRESENT);
            dest.writeParcelable(sede,0);
        }


        if (passaggiOfferti == null) {
            dest.writeByte(NOT_PRESENT);
        } else {
            dest.writeByte(PRESENT);
            dest.writeTypedList(passaggiOfferti);
        }
        if (passaggiRichiesti == null) {
            dest.writeByte(NOT_PRESENT);
        } else {
            dest.writeByte(PRESENT);
            dest.writeTypedList(passaggiRichiesti);
        }
    }


    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Cittadino> CREATOR = new Parcelable.Creator<Cittadino>() {
        @Override
        public Cittadino createFromParcel(Parcel in) {
            return new Cittadino(in);
        }

        @Override
        public Cittadino[] newArray(int size) {
            return new Cittadino[size];
        }
    };

    public String toString(){
        return cognome+" "+nome;
    }







}
