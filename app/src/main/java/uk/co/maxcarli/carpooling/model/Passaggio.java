package uk.co.maxcarli.carpooling.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Passaggio implements Parcelable{


    public String getAutomobilista() {
        return automobilista;
    }

    public void setAutomobilista(String automobilista) {
        this.automobilista = automobilista;
    }

    public String getCellAutomobilista() {
        return cellAutomobilista;
    }

    public void setCellAutomobilista(String cellAutomobilista) {
        this.cellAutomobilista = cellAutomobilista;
    }

    public int getRichieste() {
        return richieste;
    }

    public void setRichieste(int richieste) {
        this.richieste = richieste;
    }

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
    private String automobilista;
    private String cellAutomobilista;

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
        automobilista="";
        cellAutomobilista="";
        richieste=0;
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
        automobilista=in.readString();
        cellAutomobilista=in.readString();
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
        dest.writeString(automobilista);
        dest.writeString(cellAutomobilista);
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
