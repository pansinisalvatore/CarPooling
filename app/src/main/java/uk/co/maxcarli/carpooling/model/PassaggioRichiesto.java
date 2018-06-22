package uk.co.maxcarli.carpooling.model;

class PassaggioRichiesto {

    public static enum Tipoviaggio {casa_lavoro,lavoro_casa};
    public static enum Status {sospeso,accettato,rifiutato};

    private int idPassaggiRichiesti;
    private Tipoviaggio tipoviaggio;
    private Status status;
    private PassaggioOfferto passaggioOfferto;

    PassaggioRichiesto() {
    }


    public int getIdPassaggiRichiesti() {
        return idPassaggiRichiesti;
    }

    public void setIdPassaggiRichiesti(int idPassaggiRichiesti) {
        this.idPassaggiRichiesti = idPassaggiRichiesti;
    }

    public Tipoviaggio getTipoviaggio() {
        return tipoviaggio;
    }

    public void setTipoviaggio(Tipoviaggio tipoviaggio) {
        this.tipoviaggio = tipoviaggio;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public PassaggioOfferto getPassaggioOfferto() {
        return passaggioOfferto;
    }

    public void setPassaggioOfferto(PassaggioOfferto passaggioOfferto) {
        this.passaggioOfferto = passaggioOfferto;
    }



}
