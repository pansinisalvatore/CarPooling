package uk.co.maxcarli.carpooling;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import uk.co.maxcarli.carpooling.Control.Controlli;
import uk.co.maxcarli.carpooling.model.Cittadino;
import uk.co.maxcarli.carpooling.model.Passaggio;

public class Database {

    static String urlLogin = "http://carpoolingsms.altervista.org/PHP/Login.php";
    static String urlRegister = "http://carpoolingsms.altervista.org/PHP/registrazione.php";
    private final static String urlTracking ="http://carpoolingsms.altervista.org/PHP/trackingEffettuato.php";
    static AlertDialog.Builder builder;

    public static void trackingEffettuato(final int trackingEffettuato,final int idPassaggio, Context context){

            StringRequest stringRequest = new StringRequest(Request.Method.POST, urlTracking,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                        Log.d("response",response);
                        }
                    }

                    , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                   Log.d("error","erroreLettura");
                    error.printStackTrace();

                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("tracking", Integer.toString(trackingEffettuato));
                    params.put("IdPassaggio", Integer.toString(idPassaggio));


                    return params;
                }
            };


            MySingleton.getmInstance(context).addTorequestque(stringRequest);


        }

        public static void registraCittadino (final Cittadino cittadino, final String sede, final String partitaIva,final Context context) {
        builder = new AlertDialog.Builder(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlRegister,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                        if(response.equals("User Registration Successfully")){
                            Controlli.mostraMessaggioConChiusura(context.getString(R.string.registrazioneCompletaTitolo),context.getString(R.string.registrazioneCompletaTesto),context);
                        }else if(response.equals("Email o telefono esistente")){
                            Controlli.mostraMessaggioErrore(context.getString(R.string.erroreEmailEsistenteTitolo),context.getString(R.string.erroreEmailEsistente),context);
                        }

                    }
                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context, "Error...", Toast.LENGTH_SHORT).show();
                error.printStackTrace();

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("nome", cittadino.getNome());
                params.put("cognome",cittadino.getCognome());
                params.put("email",cittadino.getEmail());
                params.put("codicefiscale",cittadino.getCodiceFiscale());
                params.put("password",cittadino.getPassword());
                params.put("residenza",cittadino.getResidenza());
                params.put("telefono",cittadino.getNumeroTelefono());
                params.put("sede",sede);
                params.put("pIva",partitaIva);

                return params;
            }
        };


        MySingleton.getmInstance(context).addTorequestque(stringRequest);


    }


    public static void accedi(final String email, final String password, final Cittadino cittadino, final Context context) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlLogin,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        if (response.equals("success")) {
                            getCittadinoFromDatabase(email, password, cittadino,context);


                        } else if (response.equals("not autorized")) {
                            Controlli.mostraMessaggioErrore(context.getString(R.string.AccessoNonAutorizzatoTitolo), context.getString(R.string.AccessoNonAutorizzatoTesto), context);
                        } else if(response.equals("Something went wrong")){
                            Controlli.mostraMessaggioErrore(context.getString(R.string.loginErratoTitolo),context.getString(R.string.loginErratoTesto),context);
                        }else{
                            Controlli.mostraMessaggioErrore(context.getString(R.string.AccessoRifiutatoTitolo),context.getString(R.string.AccessorifiutatoTesto),context);
                        }

                    }
                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context, "Connection failed", Toast.LENGTH_SHORT).show();
                error.printStackTrace();

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };


        MySingleton.getmInstance(context).addTorequestque(stringRequest);


    }


    private static void getCittadinoFromDatabase(final String email, final String password, final Cittadino cittadino, final Context context) {
        String url = "http://carpoolingsms.altervista.org/PHP/getCittadino.php";



        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i("Dati",response);
                        try {

                            JSONArray jsonarray = new JSONArray(response);

                            int idSede=0;
                            for (int i = 0; i < jsonarray.length(); i++) {

                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                cittadino.setIdCittadino(jsonobject.getInt("IdCittadino"));
                                cittadino.setNome(jsonobject.getString("NomeCittadino"));
                                cittadino.setCognome(jsonobject.getString("CognomeCittadino"));
                                cittadino.setCodiceFiscale(jsonobject.getString("CodiceFiscaleCittadino"));
                                cittadino.setResidenza(jsonobject.getString("ResidenzaCittadino"));
                                cittadino.setNumeroTelefono(jsonobject.getString("TelefonoCittadino"));
                                cittadino.setTipoCittadino(jsonobject.getString("TipoCittadino"));
                                cittadino.setEmail(jsonobject.getString("EmailCittadino"));
                                cittadino.setPassword(jsonobject.getString("PasswordCittadino"));
                                cittadino.setNotificaPassaggio(jsonobject.getInt("NotificaPassaggioCittadino"));
                                cittadino.setNotificaAutorizzazione(jsonobject.getInt("NotificaAutorizzazioneCittadino"));
                                cittadino.getSede().getAzienda().setNome(jsonobject.getString("NomeAzienda"));
                                cittadino.getSede().getAzienda().setPartitaIva(jsonobject.getString("PartitaIvaAzienda"));
                                cittadino.getSede().getAzienda().setIdAzienda(jsonobject.getInt("IdAzienda"));

                                cittadino.getSede().setIdSede(jsonobject.getInt("IdSede"));
                                cittadino.getSede().setFaxSede(jsonobject.getString("FaxSede"));
                                cittadino.getSede().setEmailSede(jsonobject.getString("EmailSede"));
                                cittadino.getSede().setTelefonoSede(jsonobject.getString("TelefonoSede"));
                                cittadino.getSede().setIndirizzoSede(jsonobject.getString("IndirizzoSede"));

                            }

                            getPassaggiRichiestiFromCittadino(cittadino,context);




                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null) {

                            Toast.makeText(context.getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        MySingleton.getmInstance(context.getApplicationContext()).addTorequestque(stringRequest);

    }





    public static void getPassaggiRichiestiFromCittadino(final Cittadino cittadino, final Context context){
        String url= "http://carpoolingsms.altervista.org/PHP/LeggiPassaggiRichiesti.php";


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(context, response, Toast.LENGTH_SHORT).show();

                        if(!response.equals("Something went wrong")){
                            try {

                                JSONArray jsonarray = new JSONArray(response);

                                for(int i=0; i < jsonarray.length(); i++) {

                                    JSONObject jsonobject = jsonarray.getJSONObject(i);

                                    Passaggio p=new Passaggio();
                                    Cittadino cittadinoOfferente=new Cittadino();

                                    p.setIdPassaggiOfferti(jsonobject.getInt("IdPassaggio"));
                                    p.setData(jsonobject.getString("DataPassaggio"));
                                    p.setOra(jsonobject.getString("OraPassaggio"));
                                    p.setAuto(jsonobject.getString("AutoPassaggio"));
                                    p.setPostiDisponibili(jsonobject.getInt("PostiDisponibiliPassaggio"));
                                    p.setPostiOccupati(jsonobject.getInt("PostiOccupatiPassaggio"));
                                    p.setStatus(jsonobject.getString("Status"));
                                    p.setTipoPassaggio(jsonobject.getString("TipoPassaggio"));


                                    p.setSettimanale(jsonobject.getInt("SettimanalePassaggio"));

                                    String nomeAutomobilista=jsonobject.getString("NomeCittadino");
                                    String cognomeAutomobilista=jsonobject.getString("CognomeCittadino");
                                    cittadinoOfferente.setNome(nomeAutomobilista);
                                    cittadinoOfferente.setCognome(cognomeAutomobilista);
                                    cittadinoOfferente.setResidenza(jsonobject.getString("ResidenzaCittadino"));
                                    cittadinoOfferente.setNumeroTelefono(jsonobject.getString("TelefonoCittadino"));

                                    p.setCittadinoOfferente(cittadinoOfferente);

                                    cittadino.setMacAddress(jsonobject.getString("MacAddress"));
                                    cittadino.addPassaggioRichiesto(p);


                                }



                            } catch (JSONException e) {
                                e.printStackTrace();

                            }
                        }
                        getPassaggiOffertiFromCittadino(cittadino,context);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error != null){

                            Toast.makeText(context.getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("idCittadino", cittadino.getIdCittadino()+"");


                return params;
            }
        };

        MySingleton.getmInstance(context.getApplicationContext()).addTorequestque(stringRequest);
    }


    public static void getPassaggiOffertiFromCittadino(final Cittadino cittadino, final Context context){
        String url= "http://carpoolingsms.altervista.org/PHP/LeggiPassaggiOffertiFromCittadino.php";


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {



                        if(!response.equals("Something went wrong")){
                            try {

                                JSONArray jsonarray = new JSONArray(response);

                                for(int i=0; i < jsonarray.length(); i++) {


                                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                                    Passaggio p=new Passaggio();
                                    p.setIdPassaggiOfferti(jsonobject.getInt("IdPassaggio"));
                                    p.setData(jsonobject.getString("DataPassaggio"));
                                    p.setOra(jsonobject.getString("OraPassaggio"));
                                    p.setAuto(jsonobject.getString("AutoPassaggio"));
                                    p.setPostiDisponibili(jsonobject.getInt("PostiDisponibiliPassaggio"));
                                    p.setPostiOccupati(jsonobject.getInt("PostiOccupatiPassaggio"));
                                    p.setTipoPassaggio(jsonobject.getString("TipoPassaggio"));
                                    p.setRichieste(jsonobject.getInt("RichiestePassaggio"));
                                    p.setSettimanale(jsonobject.getInt("SettimanalePassaggio"));
                                    Cittadino cittadinoRichiedente=null;
                                    if(!jsonobject.getString("NomeCittadino").equals("null")){
                                        cittadinoRichiedente=new Cittadino();
                                        cittadinoRichiedente.setNome(jsonobject.getString("NomeCittadino"));
                                        cittadinoRichiedente.setCognome(jsonobject.getString("CognomeCittadino"));
                                        cittadinoRichiedente.setIdCittadino(jsonobject.getInt("IdCittadino"));
                                        cittadinoRichiedente.setResidenza(jsonobject.getString("ResidenzaCittadino"));
                                        cittadinoRichiedente.setNumeroTelefono(jsonobject.getString("TelefonoCittadino"));
                                        cittadinoRichiedente.setMacAddress(jsonobject.getString("MacAddress"));
                                    }

                                    if(cittadino.passaggiOfferti.contains(p)){
                                        if(cittadinoRichiedente!=null){
                                            cittadino.passaggiOfferti.get(cittadino.passaggiOfferti.indexOf(p)).cittadiniRichiedenti.add(cittadinoRichiedente);
                                            cittadino.passaggiOfferti.get(cittadino.passaggiOfferti.indexOf(p)).cittadinoStatus.add(jsonobject.getString("Status"));
                                        }
                                    }else{
                                        if(cittadinoRichiedente!=null){
                                            p.cittadiniRichiedenti.add(cittadinoRichiedente);
                                            p.cittadinoStatus.add(jsonobject.getString("Status"));
                                        }
                                        cittadino.passaggiOfferti.add(p);
                                    }


                                    //prendiRichiedentiPassaggio( p,context);
                                    //Log.i("DatiCittadini", p.cittadiniRichiedenti.get(0).getCognome()+"");



                                }




                            } catch (JSONException e) {
                                e.printStackTrace();

                            }

                        }
                        Intent intent= new Intent(context, menu.class);
                        intent.putExtra(Cittadino.Keys.IDCITTADINO,cittadino);
                        context.startActivity(intent);
                        ((Activity)context).finish();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error != null){

                            Toast.makeText(context.getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("idCittadino", cittadino.getIdCittadino()+"");


                return params;
            }
        };

        MySingleton.getmInstance(context.getApplicationContext()).addTorequestque(stringRequest);
    }

    static Cittadino cittadinoRichiedente=new Cittadino();


    public static void prendiRichiedentiPassaggio(final Passaggio passaggio, final Context context){
        String url= "http://carpoolingsms.altervista.org/PHP/PrendiRichiedenti.php";


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, response,Toast.LENGTH_SHORT).show();

                        if(!response.equals("Something went wrong")){
                            try {

                                JSONArray jsonarray = new JSONArray(response);

                                for(int i=0; i < jsonarray.length(); i++) {

                                    //Toast.makeText(context, viaggio+" "+data+" "+ora+" "+status+" "+postiOccupati,Toast.LENGTH_SHORT).show();



                                }



                            } catch (JSONException e) {
                                e.printStackTrace();

                            }

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error != null){

                            Toast.makeText(context.getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("idPassaggio", passaggio.getIdPassaggiOfferti()+"");


                return params;
            }
        };

        MySingleton.getmInstance(context.getApplicationContext()).addTorequestque(stringRequest);
    }


    public static void updateCittadino(final Cittadino cittadino,final Context context){

        String url="http://carpoolingsms.altervista.org/PHP/modificaCittadino.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url ,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        if (response.equals("success")) {
                            Controlli.mostraMessaggioErrore(context.getString(R.string.TitoloAggiornamentoConfermato), context.getString(R.string.TestoAggiornamentoConfermato),context);

                        } else {
                            Controlli.mostraMessaggioErrore("Error","error", context);
                        }

                    }
                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context, "Connection failed", Toast.LENGTH_SHORT).show();
                error.printStackTrace();

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("telefono", cittadino.getNumeroTelefono());
                params.put("residenza", cittadino.getResidenza());
                params.put("IdCittadino",cittadino.getIdCittadino()+"");
                params.put("Password",cittadino.getPassword());
                return params;
            }
        };


        MySingleton.getmInstance(context).addTorequestque(stringRequest);

    }



    public static void OffriPassaggi(final Passaggio passaggio,final Cittadino cittadino, final Context context){

        String urlOffriPassaggio ="http://carpoolingsms.altervista.org/PHP/OffriPassaggi.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlOffriPassaggio,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equals("success")){

                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle(context.getString(R.string.PassaggioOffertoConfermatoTitolo));
                            builder.setMessage(context.getString(R.string.PassaggioOffertoConfermatoTesto));
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ((Activity)context).finish();
                                }
                            });
                            AlertDialog alertDialog= builder.create();
                            alertDialog.show();
                        }else{
                            Controlli.mostraMessaggioErrore("Error","Error",context);
                        }

                    }
                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context, "Error...", Toast.LENGTH_SHORT).show();
                error.printStackTrace();

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String,String> params = new HashMap<String, String>();
                params.put("date",passaggio.getData());
                params.put("time", passaggio.getOra());
                params.put("car",passaggio.getAuto());
                params.put("type",passaggio.getTipoPassaggio());
                params.put("idCittadino",cittadino.getIdCittadino()+"");
                params.put("place_avaible",passaggio.getPostiDisponibili()+"");
                params.put("settimanale",passaggio.isSettimanale()+"");

                return params;
            }
        };


        MySingleton.getmInstance(context).addTorequestque(stringRequest);



    }



    public  static void modificaStatus(final String status, final String cell, final Context c,final Passaggio passaggio){
        String url = "http://carpoolingsms.altervista.org/PHP/AggiornaStatoPassaggioRichiesto.php";


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(c,response,Toast.LENGTH_LONG).show();

                        if(response.equals("Success")){




                        }else{
                            //Toast.makeText(context.getApplicationContext(),getString(R.string.RichiesteNonPresenti),Toast.LENGTH_LONG).show();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null) {

                            Toast.makeText(c, "Something went wrong.", Toast.LENGTH_LONG).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("idPassaggio", passaggio.getIdPassaggiOfferti()+"");
                params.put("cellRichiedente",cell);
                params.put("status",status);
                return params;
            }
        };

        MySingleton.getmInstance(c).addTorequestque(stringRequest);

    }

    public static void azzeraNotificheAutorizzazioni(final int idCittadino,final Context c){
        String urlOffriPassaggio ="http://carpoolingsms.altervista.org/PHP/SettaNotificaAutorizzazione.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlOffriPassaggio,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(c,response,Toast.LENGTH_LONG).show();

                    }
                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(c, "Error...", Toast.LENGTH_SHORT).show();
                error.printStackTrace();

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String,String> params = new HashMap<String, String>();
                params.put("idCittadino",idCittadino+"");


                return params;
            }
        };


        MySingleton.getmInstance(c).addTorequestque(stringRequest);
    }

    public static void azzeraNotifichePassaggi(final int idCittadino,final Context c){
        String urlOffriPassaggio ="http://carpoolingsms.altervista.org/PHP/SettaNotificaAZero.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlOffriPassaggio,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(c,response,Toast.LENGTH_LONG).show();

                    }
                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(c, "Error...", Toast.LENGTH_SHORT).show();
                error.printStackTrace();

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String,String> params = new HashMap<String, String>();
                params.put("idCittadino",idCittadino+"");


                return params;
            }
        };


        MySingleton.getmInstance(c).addTorequestque(stringRequest);
    }

}





