package uk.co.maxcarli.carpooling.model;

public class Sede {

    private int idSede;
    private String indirizzoSede;
    private String telefonoSede;
    private String emailSede;
    private String faxSede;
    private Azienda azienda;


    public Sede() { }


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
}


