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
import uk.co.maxcarli.carpooling.model.Azienda;
import uk.co.maxcarli.carpooling.model.Cittadino;
import uk.co.maxcarli.carpooling.model.Passaggio;
import uk.co.maxcarli.carpooling.model.Sede;

public class Database {

    static String urlLogin = "http://carpoolingsms.altervista.org/PHP/Login.php";
    static String urlRegister = "http://carpoolingsms.altervista.org/PHP/query.php";
    static AlertDialog.Builder builder;

    public static void registraCittadino(final String nome, final String cognome, final Context context) {
        builder = new AlertDialog.Builder(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlRegister,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        builder.setTitle("Server Response");
                        builder.setMessage("Response" + response);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
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
                params.put("nome", nome);
                params.put("cognome", cognome);
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
                        } else {
                            Toast.makeText(context, "Connection failed", Toast.LENGTH_SHORT).show();
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

                                cittadino.getSede().getAzienda().setNome(jsonobject.getString("NomeAzienda"));
                                cittadino.getSede().getAzienda().setPartitaIva(jsonobject.getString("PartitaIvaAzienda"));
                                cittadino.getSede().getAzienda().setIdAzienda(jsonobject.getInt("IdAzienda"));

                                cittadino.getSede().setIdSede(jsonobject.getInt("IdSede"));
                                cittadino.getSede().setFaxSede(jsonobject.getString("FaxSede"));
                                cittadino.getSede().setEmailSede(jsonobject.getString("EmailSede"));
                                cittadino.getSede().setTelefonoSede(jsonobject.getString("TelefonoSede"));
                                cittadino.getSede().setIndirizzoSede(jsonobject.getString("IndirizzoSede"));

                            }

                            Intent intent= new Intent(context, menu.class);
                            intent.putExtra(Cittadino.Keys.IDCITTADINO,cittadino);
                            context.startActivity(intent);
                            ((Activity)context).finish();

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
                                    p.setStatus(jsonobject.getString("Status"));
                                    p.setTipoPassaggio(jsonobject.getString("TipoPassaggio"));
                                    if(jsonobject.getInt("SettimanalePassaggio")==0){
                                        p.setSettimanale(false);
                                    }else{
                                        p.setSettimanale(true);
                                    }
                                    cittadino.setMacAddress(jsonobject.getString("MacAddress"));
                                    cittadino.addPassaggioRichiesto(p);

                                    Log.i("Dati", p.getData());

                                }
                                getPassaggiOffertiFromCittadino(cittadino,context);


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

                        Toast.makeText(context, response,Toast.LENGTH_SHORT).show();

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
                                    if(jsonobject.getInt("SettimanalePassaggio")==0){
                                        p.setSettimanale(false);
                                    }else{
                                        p.setSettimanale(true);
                                    }

                                    cittadino.addPassaggioOfferto(p);
                                    //Toast.makeText(context, viaggio+" "+data+" "+ora+" "+status+" "+postiOccupati,Toast.LENGTH_SHORT).show();

                                    Log.i("Dati", p.getData());


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
                params.put("idCittadino", cittadino.getIdCittadino()+"");


                return params;
            }
        };

        MySingleton.getmInstance(context.getApplicationContext()).addTorequestque(stringRequest);
    }

}





