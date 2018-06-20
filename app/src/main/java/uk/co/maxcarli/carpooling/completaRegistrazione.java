package uk.co.maxcarli.carpooling;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static uk.co.maxcarli.carpooling.Control.Controlli.mostraMessaggioErrore;

public class completaRegistrazione extends AppCompatActivity {

    private String idAzienda = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completa_registrazione);
        Cittadino cittadino = new Cittadino();
        String partitaIva = fromIntent();
        partitaIva = partitaIva.trim();
        Log.d("PartitaIva", partitaIva);
        recuperaId(partitaIva);



    }

    private String fromIntent(){

        final Intent intent = getIntent();
        String mAzienda = "";
        Cittadino cittadino = new Cittadino();

        if(intent != null){

           final String mNome = intent.getStringExtra("nome");
           final String mCognome = intent.getStringExtra("cognome");
           final String mCodiceFiscale = intent.getStringExtra("codiceFiscale");
           final String mResidenza = intent.getStringExtra("residenza");
           mAzienda = intent.getStringExtra("azienda");
           final String mNumeroTelefono = intent.getStringExtra("numeroTelefono");

           cittadino.setNome(mNome);
           cittadino.setCognome(mCognome);
           cittadino.setCodiceFiscale(mCodiceFiscale);
           cittadino.setResidenza(mResidenza);
           cittadino.setIdAzienda("");
           cittadino.setNumeroTelefono(mNumeroTelefono);
        }
        return mAzienda;
    }

    private void recuperaId(final String partitaIva){

        Log.d("finalStringPartitaIva", partitaIva);
        String url ="http://carpoolingsms.altervista.org/PHP/getIdAzienda.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                      Cittadino cittadino = new Cittadino();
                      response = response.trim();
                      cittadino.setIdAzienda(response);
                      idAzienda = cittadino.getIdAzienda(); //id Azienda si può cancellare

                        Log.d("azienda", idAzienda);
                      Log.d("response", response);

                    }
                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("partitaIva", partitaIva);
                return params;
            }
        };


        MySingleton.getmInstance(this).addTorequestque(stringRequest);
    }

}
