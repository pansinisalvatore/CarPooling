package uk.co.maxcarli.carpooling.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import uk.co.maxcarli.carpooling.Control.Controlli;

public class Passaggio implements Parcelable{





    public interface Keys{
        String IDPASSAGGIO = "idPassaggio";
        String DATA = "data";
        String ORA = "ora";
        String TIPOPASSAGGIO= "tipoPassaggio";
        String SETTIMANALE = "settimanale";
        String AUTOMOBILE = "auto";
        String POSTIDISPONIBILI = "postiDisponibili";
        String POSTIOCCUPATI = "postiOccupati";
        String STATUS="Status";
    }


    private int idPassaggio;
    private String data;
    private String ora;
    private String tipoPassaggio;
    private int settimanale;
    private int richieste;
    private String auto;
    private int postiDisponibili;
    private int postiOccupati;
    private String status;



    private static final byte PRESENT=1;
    private static final byte NOT_PRESENT=0;


    public   final ArrayList<Cittadino> cittadiniRichiedenti;
    public final ArrayList<String> cittadinoStatus;

    private Cittadino cittadinoOfferente;


    public Passaggio() {
        idPassaggio = 0;
        data = "";
        ora = "";
        tipoPassaggio = "";
        settimanale = 0;
        auto = "";
        postiDisponibili = 0;
        postiOccupati = 0;
        status = "";
        richieste=0;
        cittadiniRichiedenti=new ArrayList<Cittadino>();
        cittadinoStatus=new ArrayList<String>();

    }


    public int getIdPassaggiOfferti() {
        return idPassaggio;
    }

    public void setIdPassaggiOfferti(int idPassaggiOfferti) {
        this.idPassaggio = idPassaggiOfferti;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getOra() {
        return ora;
    }

    public void setOra(String ora) {
        this.ora = ora;
    }

    public int isSettimanale() {
        return settimanale;
    }

    public void setSettimanale(int settimanale) {
        this.settimanale = settimanale;
    }

    public String getAuto() {
        return auto;
    }

    public void setAuto(String auto) {
        this.auto = auto;
    }

    public int getPostiDisponibili() {
        return postiDisponibili;
    }

    public void setPostiDisponibili(int postiDisponibili) {
        this.postiDisponibili = postiDisponibili;
    }

    public int getPostiOccupati() {
        return postiOccupati;
    }

    public void setPostiOccupati(int postiOccupati) {
        this.postiOccupati = postiOccupati;
    }

    public String getTipoPassaggio() {
        return tipoPassaggio;
    }

    public void setTipoPassaggio(String tipoPassaggio) {
        this.tipoPassaggio = tipoPassaggio;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRichieste() {
        return richieste;
    }

    public void setRichieste(int richieste) {
        this.richieste = richieste;
    }

    public Cittadino getCittadinoOfferente() {
        return cittadinoOfferente;
    }

    public void setCittadinoOfferente(Cittadino cittadinoOfferente) {
        this.cittadinoOfferente = cittadinoOfferente;
    }


    public boolean equals(Object o){
        Passaggio p=(Passaggio)o;
        if(this.data.equals(p.data)){
            if(Math.abs(Controlli.oreInMinuti(p.ora)- Controlli.oreInMinuti(this.ora))>=180){
                return false;
            }else{
                return true;
            }
        }else{
            return false;
        }

    }


    protected Passaggio(Parcel in) {
        idPassaggio = in.readInt();
        data = in.readString();
        ora = in.readString();
        tipoPassaggio = in.readString();
        settimanale = in.readInt();
        richieste=in.readInt();
        auto = in.readString();
        postiDisponibili = in.readInt();
        postiOccupati = in.readInt();
        status = in.readString();
        cittadiniRichiedenti=new ArrayList<Cittadino>();
        in.readTypedList(cittadiniRichiedenti, Cittadino.CREATOR);
        cittadinoStatus=new ArrayList<String>();
        in.readList(cittadinoStatus, String.class.getClassLoader());

        if(in.readByte()==PRESENT){
            cittadinoOfferente=in.readParcelable(Cittadino.class.getClassLoader());
        }else{
            cittadinoOfferente=null;
        }

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idPassaggio);
        dest.writeString(data);
        dest.writeString(ora);
        dest.writeString(tipoPassaggio);
        dest.writeInt(settimanale) ;
        dest.writeInt(richieste);
        dest.writeString(auto);
        dest.writeInt(postiDisponibili);
        dest.writeInt(postiOccupati);
        dest.writeString(status);
        dest.writeTypedList(cittadiniRichiedenti);
        dest.writeList(cittadinoStatus);

        if(cittadinoOfferente==null){
            dest.writeByte(NOT_PRESENT);
        }else{
            dest.writeByte(PRESENT);
            dest.writeParcelable(cittadinoOfferente,0);
        }
    }


    public static final Parcelable.Creator<Passaggio> CREATOR = new Parcelable.Creator<Passaggio>() {
        @Override
        public Passaggio createFromParcel(Parcel in) {
            return new Passaggio(in);
        }

        @Override
        public Passaggio[] newArray(int size) {
            return new Passaggio[size];
        }
    };
}
