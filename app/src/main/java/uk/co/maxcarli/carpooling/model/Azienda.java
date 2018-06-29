package uk.co.maxcarli.carpooling.model;

import android.os.Parcel;
import android.os.Parcelable;


public class Azienda implements Parcelable{

    public interface Keys{
        String IDAZIENDA = "idAzienda";
        String NOME = "nome";
        String PARTITAIVA = "partitaIva";

    }

    private int idAzienda;
    private String partitaIva;
    private String nome;

    public Azienda() {
        idAzienda = 0;
        partitaIva = "";
        nome = "";
    }

    public int getIdAzienda() {
        return idAzienda;
    }

    public void setIdAzienda(int idAzienda) {
        this.idAzienda = idAzienda;
    }

    public String getPartitaIva() {
        return partitaIva;
    }

    public void setPartitaIva(String partitaIva) {
        this.partitaIva = partitaIva;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    protected Azienda(Parcel in) {
        idAzienda = in.readInt();
        partitaIva = in.readString();
        nome = in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idAzienda);
        dest.writeString(partitaIva);
        dest.writeString(nome);
    }


    public static final Parcelable.Creator<Azienda> CREATOR = new Parcelable.Creator<Azienda>() {
        @Override
        public Azienda createFromParcel(Parcel in) {
            return new Azienda(in);
        }

        @Override
        public Azienda[] newArray(int size) {
            return new Azienda[size];
        }
    };

}
