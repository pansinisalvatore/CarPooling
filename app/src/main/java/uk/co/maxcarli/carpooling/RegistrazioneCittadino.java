package uk.co.maxcarli.carpooling;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegistrazioneCittadino extends AppCompatActivity {

    String url ="http://carpoolingsms.altervista.org/PHP/query.php";
    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione_cittadino);
    }


    public void editTextRegistrazione(View view) {
        EditText aux = (EditText) findViewById(R.id.NomeCittadino);
        String nome = aux.getText().toString();
        EditText aux1 = (EditText) findViewById(R.id.CognomeCittadino);
        String cognome = aux1.getText().toString();
        Cittadino cittadino = new Cittadino(nome,cognome);
        registraCittadino(nome,cognome);


    }
    public void registraCittadino(final String nome, final String cognome){
        builder = new AlertDialog.Builder(RegistrazioneCittadino.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,

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

                Toast.makeText(RegistrazioneCittadino.this, "Error...", Toast.LENGTH_SHORT).show();
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


        MySingleton.getmInstance(RegistrazioneCittadino.this).addTorequestque(stringRequest);



    }






}
