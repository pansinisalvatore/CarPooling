package uk.co.maxcarli.carpooling;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import uk.co.maxcarli.carpooling.Control.Controlli;
import uk.co.maxcarli.carpooling.model.Cittadino;

public class DettagliUtente extends AppCompatActivity {
    TextInputEditText nam;
    TextInputEditText surnam;
    TextInputEditText mail;
    TextInputEditText fiscal_cod;
    TextInputEditText residenc;
    TextInputEditText phone_number;

    Button accept;
    Button refuse;

    Cittadino c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_dettagli_utente);

        nam=findViewById(R.id.edtselezionanome);
        nam.setKeyListener(null);
        surnam=findViewById(R.id.edtselezionacognome);
        surnam.setKeyListener(null);
        mail=findViewById(R.id.edtselezionaemail);
        mail.setKeyListener(null);
        fiscal_cod=findViewById(R.id.edtselezionaCF);
        fiscal_cod.setKeyListener(null);
        residenc=findViewById(R.id.edtselezionaresidenza);
        residenc.setKeyListener(null);
        phone_number=findViewById(R.id.edtselezionatelefono);
        phone_number.setKeyListener(null);



        if(savedInstanceState!=null){
            c=savedInstanceState.getParcelable(Cittadino.Keys.IDCITTADINO);
        }else{
            c=getIntent().getParcelableExtra(Cittadino.Keys.IDCITTADINO);
        }

        nam.setText(c.getNome());
        surnam.setText(c.getCognome());
        mail.setText(c.getEmail());
        fiscal_cod.setText(c.getCodiceFiscale());
        residenc.setText(c.getResidenza());
        phone_number.setText(c.getNumeroTelefono());

        accept=findViewById(R.id.AccettaUtente);
        refuse=findViewById(R.id.RifiutaUtente);

        if(c.getConvalidato()==1){
            accept.setVisibility(View.INVISIBLE);
            refuse.setVisibility(View.INVISIBLE);
        }else{
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    modificaAutorizzazioneUtente(1);

                    Intent returnIntent=new Intent();
                    returnIntent.putExtra("convalidato",1);
                    setResult(0,returnIntent);
                }
            });

            refuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    modificaAutorizzazioneUtente(2);


                }
            });
        }


    }

    private void modificaAutorizzazioneUtente(final int autorizzazione) {
        String url= "http://carpoolingsms.altervista.org/PHP/modificaAutorizzazione.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equals("success")){
                            Intent returnIntent=new Intent();
                            returnIntent.putExtra("convalidato",autorizzazione);
                            setResult(0,returnIntent);
                            if(autorizzazione==1){
                                Controlli.mostraMessaggioConChiusura(getString(R.string.utenteAccettatoTitolo),getString(R.string.utenteAccettatoTesto),DettagliUtente.this);
                            }else{
                                Controlli.mostraMessaggioConChiusura(getString(R.string.utenteRifiutatoTitolo),getString(R.string.utenteRifiutatoTesto),DettagliUtente.this);
                            }

                        }else{
                            Toast.makeText(DettagliUtente.this,"Error  php",Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error != null){

                            Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                        }
                    }
                }){

            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("idCittadino", c.getIdCittadino()+"");
                params.put("convalida",autorizzazione+"");
                return params;
            }



        };

        MySingleton.getmInstance(getApplicationContext()).addTorequestque(stringRequest);
    }


    public void onRestoreInstanceState(Bundle bundle){
        bundle.putParcelable(Cittadino.Keys.IDCITTADINO,c);
    }
}
