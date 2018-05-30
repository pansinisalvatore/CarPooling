package uk.co.maxcarli.carpooling.Control;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.EditText;

public class Controlli {

    public static boolean controlloEditTextVuoto(EditText campo){
        String text = campo.getText().toString();
        if(text.length()==0){
            campo.setError("Campo obbligatorio");
            return true; //ritorna vero se è presente l'errore
        }
        return false;
    }

    public static boolean verificaCodiceFiscale(EditText campo){

        String text = campo.getText().toString();
        int lunghezza = campo.length();
        if (lunghezza < 16 || lunghezza > 16){
            return true; //ritorna vero se è presente l'errore
        }
        else return false;
    }

    public static void mostraMessaggioErrore(String title,String text, Context context){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(text);
        builder.show();
    }



}
