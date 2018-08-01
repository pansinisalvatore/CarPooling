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

    private String partitaIva = "";
    private Cittadino cittadino;
    private String sede;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completa_registrazione);
        fromIntent();

        Log.d("PartitaIva", partitaIva);

    }

    private void fromIntent(){

        final Intent intent = getIntent();


        if(intent != null){
            Cittadino cittadino = intent.getParcelableExtra(Cittadino.Keys.IDCITTADINO);
            partitaIva=intent.getStringExtra("partitaIva");
            sede=intent.getStringExtra("sede");

        }

    }


    public void clickButton(View view){
        Boolean lenghtPasw = true;
        Boolean verEmail = true;
        TextInputEditText emailEdit = (TextInputEditText) this.findViewById(R.id.emailCompletaRegistrazione);
        TextInputEditText passwordEdit = (TextInputEditText) this.findViewById(R.id.passwordCompletaRegistrazione);
        TextInputEditText confermaPasswordEdit = (TextInputEditText) this.findViewById(R.id.confermaPasswordCompletaRegistrazione);

            if (campiVuoti(emailEdit,passwordEdit,confermaPasswordEdit) == false){
                verEmail = verificaEmail(emailEdit);
                String text = getString(R.string.passwordCorta);
                Log.d("text", text);
                lenghtPasw = lunghezzaPassword(passwordEdit,text);
                return;
            }

            if(verEmail == false ) {
                Log.d("verEmail","true");

                if (confrontaPassword(passwordEdit, confermaPasswordEdit)) {
                    String text = getString(R.string.passwordErrataText);
                    String title = getString(R.string.passwordErrata);
                    mostraMessaggioErrore(title, text, completaRegistrazione.this);
                    return;
                }
                cittadino.setEmail(emailEdit.getText().toString());
                cittadino.setPassword(passwordEdit.getText().toString());
                Database.registraCittadino(cittadino,sede,partitaIva,this);
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
                String text = getString(R.string.formatoEmailNonValido);
                emailEdit.setError(text);
            return true;
       }
    }

}
