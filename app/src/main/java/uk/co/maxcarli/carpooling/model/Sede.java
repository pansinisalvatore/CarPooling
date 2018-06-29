package uk.co.maxcarli.carpooling.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Sede implements Parcelable{



    public interface Keys{
        String IDSEDE="idSede";
        String INDIRIZZO="indirizzo";
        String TELEFONOSEDE="telefono";
        String EMAILSEDE="email";
        String FAXSEDE="fax";
    }

    private static final byte PRESENT=1;
    private static final byte NOT_PRESENT=0;


    private int idSede;
    private String indirizzoSede;
    private String telefonoSede;
    private String emailSede;
    private String faxSede;
    private Azienda azienda;


    public Sede() {
        idSede=0;
        indirizzoSede="";
        telefonoSede="";
        emailSede="";
        faxSede="";
    }


    public int getIdSede() {
        return idSede;
    }

    public void setIdSede(int idSede) {
        this.idSede = idSede;
    }

    public String getIndirizzoSede() {
        return indirizzoSede;
    }

    public void setIndirizzoSede(String indirizzoSede) {
        this.indirizzoSede = indirizzoSede;
    }

    public String getTelefonoSede() {
        return telefonoSede;
    }

    public void setTelefonoSede(String telefonoSede) {
        this.telefonoSede = telefonoSede;
    }

    public String getEmailSede() {
        return emailSede;
    }

    public void setEmailSede(String emailSede) {
        this.emailSede = emailSede;
    }

    public String getFaxSede() {
        return faxSede;
    }

    public void setFaxSede(String faxSede) {
        this.faxSede = faxSede;
    }

    public Azienda getAzienda() {
        return azienda;
    }

    public void setAzienda(Azienda azienda) {
        this.azienda = azienda;
    }


    protected Sede(Parcel in) {
        idSede = in.readInt();
        indirizzoSede = in.readString();
        telefonoSede = in.readString();
        emailSede = in.readString();
        faxSede = in.readString();
        if(in.readByte()==PRESENT){
            azienda = (Azienda) in.readValue(Azienda.class.getClassLoader());
        }else{
            azienda=null;
        }

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idSede);
        dest.writeString(indirizzoSede);
        dest.writeString(telefonoSede);
        dest.writeString(emailSede);
        dest.writeString(faxSede);
        if(azienda==null){
            dest.writeByte(NOT_PRESENT);
        }else{
            dest.writeByte(PRESENT);
            dest.writeValue(azienda);
        }

    }


    public static final Parcelable.Creator<Sede> CREATOR = new Parcelable.Creator<Sede>() {
        @Override
        public Sede createFromParcel(Parcel in) {
            return new Sede(in);
        }

        @Override
        public Sede[] newArray(int size) {
            return new Sede[size];
        }
    };
}


