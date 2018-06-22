package uk.co.maxcarli.carpooling.model;

class PassaggioOfferto {

    private int idPassaggiOfferti;
    private String data;
    private String ora;
    private boolean settimanale;
    private String auto;
    private int postiDisponibili;
    private int postiOccupati;

    PassaggioOfferto() { }

    public int getIdPassaggiOfferti() {
        return idPassaggiOfferti;
    }

    public void setIdPassaggiOfferti(int idPassaggiOfferti) {
        this.idPassaggiOfferti = idPassaggiOfferti;
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

    public boolean isSettimanale() {
        return settimanale;
    }

    public void setSettimanale(boolean settimanale) {
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
}
