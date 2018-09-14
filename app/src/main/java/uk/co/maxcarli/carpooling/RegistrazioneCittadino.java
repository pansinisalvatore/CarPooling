package uk.co.maxcarli.carpooling;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


import uk.co.maxcarli.carpooling.model.Cittadino;

import static uk.co.maxcarli.carpooling.Control.Controlli.*;

public class RegistrazioneCittadino extends AppCompatActivity {


    TextInputEditText nomeText;
    TextInputEditText cognomeText;
    TextInputEditText CFText;
    EditText residenzaText;
    EditText aux2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione_cittadino);
        nomeText=findViewById(R.id.nomeCittadino);
        cognomeText=findViewById(R.id.cognomeCittadino);
        residenzaText=findViewById(R.id.residenzaCittadino);
        CFText=findViewById(R.id.codiceFiscale);
        if(savedInstanceState!=null){
            nomeText.setText(savedInstanceState.getString("nome"));
            cognomeText.setText(savedInstanceState.getString("cognome"));
            residenzaText.setText(savedInstanceState.getString("residenza"));
            CFText.setText(savedInstanceState.getString("CF"));
        }


        findViewById(R.id.buttonAvanti).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextRegistrazione();
            }
        });

    }
    protected void onStart(){
        super.onStart();
    }

    protected void onDestroy(){
        super.onDestroy();
    }

    /**
     * Prende tutti campi dall'editText e inserisce i dati nella classe cittadino. Dopodichè vengono passati alla classe SceltaAzienda
     */
    public void editTextRegistrazione() {

        if(controlliCampi()== false) {

            String nome = nomeText.getText().toString();
            String cognome = cognomeText.getText().toString();
            String codiceFiscale = CFText.getText().toString();
            String residenza = residenzaText.getText().toString();
            Cittadino cittadino=new Cittadino();
            cittadino.setNome(nome);
            cittadino.setCognome(cognome);
            cittadino.setCodiceFiscale(codiceFiscale);
            cittadino.setResidenza(residenza);

            final Intent intent = new Intent(this, SceltaAzienda.class);

            intent.putExtra(Cittadino.Keys.IDCITTADINO, cittadino);
            startActivity(intent);
        }


    }


    /**
     * Al click dell'editText si apre l'activity RicercaIndirizzo.
     * @param view
     */
    public void scegliIndirizzo(View view){

        Intent intent=new Intent(this,RicercaIndirizzo.class);
        startActivityForResult(intent,0);

    }

    /**
     * Controlla se i campi sono corretti
     * @return
     */
    private boolean controlliCampi(){
        boolean bool = false;

        if (controlloEditTextVuoto(nomeText,this) == true){

            bool = true;
        }

        if(controlloEditTextVuoto(cognomeText,this) == true) {

            bool = true;
        }

        if (controlloEditTextVuoto(CFText,this) == true) {

            bool = true;
        }
            /*
       PRIMA DI CANCELLARE CHIEDERE A RINO! CAPITO BASTARDI?
       if (verificaCodiceFiscale(aux) == true)
            mostraMessaggioErrore("Codice fiscale non valido","Il codice fiscale deve essere da 16 caratteri", RegistrazioneCittadino.this);
*/
        aux2 = (EditText) this.findViewById(R.id.residenzaCittadino);
        if(controlloEditTextVuoto(residenzaText,this) == true) {
            bool = true;
        }
        return bool;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(data!=null){

            String indirizzo=(String)data.getStringExtra(Cittadino.Keys.RESIDENZA);
            residenzaText.setText(indirizzo);

        }

    }

    public void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        bundle.putString("nome",nomeText.getText().toString());
        bundle.putString("cognome",cognomeText.getText().toString());
        bundle.putString("CF",CFText.getText().toString());
        bundle.putString("residenza",residenzaText.getText().toString());
    }


}
