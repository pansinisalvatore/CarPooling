package uk.co.maxcarli.carpooling;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
    TextInputEditText emailEdit;
    TextInputEditText passwordEdit;
    TextInputEditText confermaPasswordEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completa_registrazione);
        if(savedInstanceState!=null){
            emailEdit = (TextInputEditText) this.findViewById(R.id.emailCompletaRegistrazione);
            passwordEdit = (TextInputEditText) this.findViewById(R.id.passwordCompletaRegistrazione);
            confermaPasswordEdit = (TextInputEditText) this.findViewById(R.id.confermaPasswordCompletaRegistrazione);
            emailEdit.setText(savedInstanceState.getString("email"));
            passwordEdit.setText(savedInstanceState.getString("password"));
            confermaPasswordEdit.setText(savedInstanceState.getString("confermaPassword"));
            partitaIva=savedInstanceState.getString("partitaIva");
            sede=savedInstanceState.getString("sede");
        }
        fromIntent();
        Toast.makeText(this,sede,Toast.LENGTH_LONG).show();

        Log.d("PartitaIva", partitaIva);

    }

    private void fromIntent(){

        final Intent intent = getIntent();


        if(intent != null){
            cittadino = intent.getParcelableExtra(Cittadino.Keys.IDCITTADINO);
            partitaIva=intent.getStringExtra("partitaIva");
            sede=intent.getStringExtra("sede");

        }

    }


    public void clickButton(View view){
        Boolean lenghtPasw = true;
        Boolean verEmail = true;
        emailEdit = (TextInputEditText) this.findViewById(R.id.emailCompletaRegistrazione);
        passwordEdit = (TextInputEditText) this.findViewById(R.id.passwordCompletaRegistrazione);
        confermaPasswordEdit = (TextInputEditText) this.findViewById(R.id.confermaPasswordCompletaRegistrazione);

            if (campiVuoti(emailEdit,passwordEdit,confermaPasswordEdit) == false){
                verEmail = verificaEmail(emailEdit);
                String text = getString(R.string.passwordCorta);
                Log.d("text", text);
                lenghtPasw = lunghezzaPassword(passwordEdit,text);
                if(lenghtPasw){
                    return;
                }

            }

            if(verEmail == false ) {
                Log.d("verEmail","true");

                if (!confrontaPassword(passwordEdit, confermaPasswordEdit)) {
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

        Boolean errorEmail =  controlloEditTextVuoto(emailEdit,this);
        Boolean errorPassword = controlloEditTextVuoto(passwordEdit,this);
        Boolean errorConfermaPassword = controlloEditTextVuoto(confermaPasswordEdit,this);

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

    public void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        bundle.putString("email",emailEdit.getText().toString());
        bundle.putString("password",passwordEdit.getText().toString());
        bundle.putString("confermaPassword",confermaPasswordEdit.getText().toString());
        bundle.putString("partitaIva",partitaIva);
        bundle.putString("sede",sede);
    }

}
