package uk.co.maxcarli.carpooling.Control;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.ContextMenu;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.co.maxcarli.carpooling.R;

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

    public static void mostraMessaggioConChiusura(String title, String msg, final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((Activity)context).finish();
            }
        });
        AlertDialog alertDialog= builder.create();
        alertDialog.show();
    }

    public static boolean controlloEditTextVuoto(EditText campo, Context c){
        String text = campo.getText().toString();
        if(text.length()==0){
            campo.setError(c.getString(R.string.CampoObbligatorio));
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

    public static boolean controlloEditTextVuoto(TextInputEditText campo, Context c){
        String text = campo.getText().toString();
        if(text.length()==0){
            campo.setError(c.getString(R.string.CampoObbligatorio));
            return true; //ritorna vero se è presente l'errore
        }
        return false;
    }


    public static boolean controlloEditTextVuoto(AutoCompleteTextView campo, Context c){
        String text = campo.getText().toString();
        if(text.length()==0){
            campo.setError(c.getString(R.string.CampoObbligatorio));
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
        stringaOrario = stringaOrario.trim();
        Log.d("StringaOrario",stringaOrario);
        int indice=stringaOrario.indexOf(":");

        String stringHours = stringaOrario.substring(0,indice);
        Log.d("stringHours", stringHours);
        String stringMinutes = stringaOrario.substring(indice+1);
        Log.d("stringMin",stringMinutes);
        int oraInt = Integer.parseInt(stringHours);
        int minInt = Integer.parseInt(stringMinutes);
        int orarioConvertito = (oraInt * 60) + minInt;
        return orarioConvertito;
    }


    public static String controllaStringaStatus(String status, Context c){
        switch(status){
            case "accettato":
                return c.getString(R.string.Accetato);
            case "rifiutato":
                return c.getString(R.string.Rifiutato);
            default:
                return c.getString(R.string.Sospeso);
        }
    }

    public static int getOraCorrente(){

        Calendar cal = Calendar.getInstance();
        int ora = cal.get(Calendar.HOUR_OF_DAY);
        String sOra = Integer.toString(ora);
        Log.d("hh",sOra);
        int minuti = cal.get(Calendar.MINUTE);
        String sMin = Integer.toString(minuti);
        Log.d("mm",sMin);
        int oraCorrente = (ora * 60) + minuti;
        return oraCorrente;
    }

    public static String getCurrentData(){

        Date today = Calendar.getInstance().getTime();

        String reportDate = DateFormat.getDateInstance().format(today.getTime());
        Log.d("DataCurrent", reportDate);
        return reportDate;

    }









}
