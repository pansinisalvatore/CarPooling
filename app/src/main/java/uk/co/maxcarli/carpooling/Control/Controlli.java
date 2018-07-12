package uk.co.maxcarli.carpooling.Control;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controlli {

    public static void mostraMessaggioSuccesso(String title, String msg, Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog= builder.create();
        alertDialog.show();
    }

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

    public static boolean controlloEditTextVuoto(TextInputEditText campo){
        String text = campo.getText().toString();
        if(text.length()==0){
            campo.setError("Campo obbligatorio");
            return true; //ritorna vero se è presente l'errore
        }
        return false;
    }


    public static boolean controlloEditTextVuoto(AutoCompleteTextView campo){
        String text = campo.getText().toString();
        if(text.length()==0){
            campo.setError("Campo obbligatorio");
            return true; //ritorna vero se è presente l'errore
        }
        return false;
    }
    public static boolean verificaNumeroTelefonico(TextInputEditText campo){

        String text = campo.getText().toString();
        int lunghezza = campo.length();
        if (lunghezza == 0) return false;
        if (lunghezza == 10){
            return false;
        }
        else return true; //ritorna vero se è presente l'errore
    }

    public static boolean confrontaPassword(TextInputEditText password,TextInputEditText confermaPassword ){
        String sPassword = password.getText().toString();
        String sConfermaPassword = password.getText().toString();
        sPassword = sPassword.trim();
        sConfermaPassword = sConfermaPassword.trim();
        if (sPassword.equals(sConfermaPassword)) {
            Log.d("sPasswordEguals","false");
            return false;
        }
        else return true;
    }

    public static boolean mailSyntaxCheck(String email)
    {
        // Create the Pattern using the regex
        Pattern p = Pattern.compile(".+@.+\\.[a-z]+");

        // Match the given string with the pattern
        Matcher m = p.matcher(email);

        // check whether match is found
        boolean matchFound = m.matches();

        StringTokenizer st = new StringTokenizer(email, ".");
        String lastToken = null;
        while (st.hasMoreTokens()) {
            lastToken = st.nextToken();
        }

        // validate the country code
        if (matchFound && lastToken.length() >= 2
                && email.length() - 1 != lastToken.length()) {

            return true;
        } else {
            return false;
        }

    }

    public static Boolean lunghezzaPassword( TextInputEditText password,String text){

            String pas = password.getText().toString();
            int lunghezza = password.length();
            if (lunghezza >5 && lunghezza < 17) {
                return false; //non è presente l'errore
            }
            else {
                password.setError(text);
                return true; //ritorna vero se è presente l'errore
            }
    }

    public static int oreInMinuti(String stringaOrario){

        stringaOrario = stringaOrario.replace(" ", "");
        String stringHours = stringaOrario.substring(0,1);
        String stringMinutes = stringaOrario.substring(3,4);
        int oraInt = Integer.parseInt(stringHours);
        int minInt = Integer.parseInt(stringMinutes);
        int orarioConvertito = oraInt + minInt;
        return orarioConvertito;
    }









}
