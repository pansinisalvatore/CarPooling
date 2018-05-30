package uk.co.maxcarli.carpooling;

public class Passaggio {

    String viaggio;
    String data;
    String ora;
    String status;
    int postiOccupati;
    int idCittadino;

    public Passaggio(){

    }

    public Passaggio(String viaggio,String data, String ora, String status, int postiOccupati, int idCittadino){
        this.viaggio=viaggio;
        this.data=data;
        this.ora=ora;
        this.status=status;
        this.postiOccupati=postiOccupati;
        this.idCittadino=idCittadino;
    }

    public String getViaggio(){
        return viaggio;
    }

    public String getData(){
        return data;
    }

    public String getOra() {
        return ora;
    }

    public String getStatus(){
        return status;
    }

    public int getPostiOccupati(){
        return postiOccupati;
    }

    public int getIdCittadino(){
        return idCittadino;
    }

}
