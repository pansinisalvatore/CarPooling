package uk.co.maxcarli.carpooling;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import static uk.co.maxcarli.carpooling.Control.Controlli.*;
public class Database {

    static String urlLogin="http://carpoolingsms.altervista.org/PHP/Login.php";
    static String urlRegister ="http://carpoolingsms.altervista.org/PHP/query.php";
    static AlertDialog.Builder builder;

    public static void registraCittadino(final String nome, final String cognome, final Context context){
        builder = new AlertDialog.Builder(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlRegister,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        builder.setTitle("Server Response");
                        builder.setMessage("Response"+ response);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        AlertDialog alertDialog= builder.create();
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
                Map <String,String> params = new HashMap<String, String>();
                params.put("nome", nome);
                params.put("cognome", cognome);
                return params;
            }
        };


        MySingleton.getmInstance(context).addTorequestque(stringRequest);



    }


    public static void accedi(final String email, final String password, final Context context){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlLogin,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(context, "response", Toast.LENGTH_SHORT).show();
                        if(response.equals("success")){
                            getCittadinoFromDatabase(email,password,context);

                        }else if(response.equals("not autorized")){
                            mostraMessaggioErrore("Non autorizzato","Non sei stato ancora autorizzato",context);
                        }else{
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
                Map <String,String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };


        MySingleton.getmInstance(context).addTorequestque(stringRequest);



    }


    private static void getCittadinoFromDatabase(final String email, final String password,final Context context){
        String url= "http://carpoolingsms.altervista.org/PHP/getCittadino.php?email="+email+"&password="+password;

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {



                        try {

                            JSONArray jsonarray = new JSONArray(response);

                            for(int i=0; i < jsonarray.length(); i++) {

                                JSONObject jsonobject = jsonarray.getJSONObject(i);


                                String nome = jsonobject.getString("nome");
                                String cognome = jsonobject.getString("cognome");

                                Intent intent= new Intent(context ,Principale.class);
                                intent.putExtra("nome", nome);
                                intent.putExtra("cognome",cognome);
                                context.startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error != null){

                            Toast.makeText(context.getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                        }
                    }
                }

        );

        MySingleton.getmInstance(context.getApplicationContext()).addTorequestque(stringRequest);
    }




}


