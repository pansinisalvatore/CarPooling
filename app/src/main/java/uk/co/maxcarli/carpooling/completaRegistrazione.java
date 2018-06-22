package uk.co.maxcarli.carpooling;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import uk.co.maxcarli.carpooling.model.Cittadino;

import static uk.co.maxcarli.carpooling.Control.Controlli.confrontaPassword;
import static uk.co.maxcarli.carpooling.Control.Controlli.controlloEditTextVuoto;
import static uk.co.maxcarli.carpooling.Control.Controlli.lunghezzaPassword;
import static uk.co.maxcarli.carpooling.Control.Controlli.mailSyntaxCheck;
import static uk.co.maxcarli.carpooling.Control.Controlli.mostraMessaggioErrore;

public class completaRegistrazione extends AppCompatActivity {

    private String idAzienda = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completa_registrazione);
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

                     // Cittadino cittadino = new Cittadino(nome, cognome);
                      response = response.trim();
                      //cittadino.setIdAzienda(response);

                      //idAzienda = cittadino.getIdSede(); //id Azienda si può cancellare

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

    public void clickButton(View view){
        Boolean lenghtPasw = true;
        Boolean verEmail = true;
        TextInputEditText emailEdit = (TextInputEditText) this.findViewById(R.id.emailCompletaRegistrazione);
        TextInputEditText passwordEdit = (TextInputEditText) this.findViewById(R.id.passwordCompletaRegistrazione);
        TextInputEditText confermaPasswordEdit = (TextInputEditText) this.findViewById(R.id.confermaPasswordCompletaRegistrazione);

            if (campiVuoti(emailEdit,passwordEdit,confermaPasswordEdit) == false){
                verEmail = verificaEmail(emailEdit);
                String text = getText(R.string.passwordCorta).toString();
                Log.d("text", text);
                lenghtPasw = lunghezzaPassword(passwordEdit,text);
            }

            if(verEmail == false ) {
                Log.d("verEmail","true");

                if (confrontaPassword(passwordEdit, confermaPasswordEdit)) {
                    String text = getText(R.string.passwordErrataText).toString();
                    String title = getText(R.string.passwordErrata).toString();
                    mostraMessaggioErrore(title, text, completaRegistrazione.this);
                }
            }


    }

    private Boolean campiVuoti(TextInputEditText emailEdit,TextInputEditText passwordEdit,TextInputEditText confermaPasswordEdit){

        Boolean errorEmail =  controlloEditTextVuoto(emailEdit);
        Boolean errorPassword = controlloEditTextVuoto(passwordEdit);
        Boolean errorConfermaPassword = controlloEditTextVuoto(confermaPasswordEdit);

        if(errorEmail == false && errorPassword == false && errorConfermaPassword == false) return false;
        else return true;
    }

    private Boolean verificaEmail( TextInputEditText emailEdit){
        String email = emailEdit.getText().toString();
       if( mailSyntaxCheck(email) == true) return false; //nell'email non sono presenti errori
        else {
                String text = getText(R.string.formatoEmailNonValido).toString();
                emailEdit.setError(text);
            return true;
       }
    }

}
