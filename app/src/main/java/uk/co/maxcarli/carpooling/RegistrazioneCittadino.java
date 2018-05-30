package uk.co.maxcarli.carpooling;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static uk.co.maxcarli.carpooling.Database.registraCittadino;
import static uk.co.maxcarli.carpooling.Control.Controlli.*;

public class RegistrazioneCittadino extends AppCompatActivity {

    String url ="http://carpoolingsms.altervista.org/PHP/query.php";
    AlertDialog.Builder builder;
    EditText aux;

    private String mNome;
    private String mCognome;
    private String mCodiceFiscale;
    private String mResidenza;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione_cittadino);

       riceviCittadino();

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


    public void editTextRegistrazione() {

        if(controlliCampi()== false) {

            String nome = mNome;
            String cognome = mCognome;
            String codiceFiscale = mCodiceFiscale;
            String residenza = mResidenza;

            final Intent intent = new Intent(this, SceltaAzienda.class);

            intent.putExtra("nome", nome);
            intent.putExtra("cognome", cognome);
            intent.putExtra("codiceFiscale", codiceFiscale);
            startActivity(intent);
        }


    }
   /* public void registraCittadino(final String nome, final String cognome){
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



    }*/



    public void scegliIndirizzo(View view){

        aux = (EditText) this.findViewById(R.id.NomeCittadino);
        String nome = aux.getText().toString();
        aux = (EditText) this.findViewById(R.id.cognomeCittadino);
        String cognome = aux.getText().toString();
        aux = (EditText) this.findViewById(R.id.codiceFiscale);
        final String codiceFiscale = aux.getText().toString();

        final Intent intent = new Intent(this,RicercaIndirizzo.class);

        intent.putExtra("nome",nome);
        intent.putExtra("cognome",cognome);
        intent.putExtra("codiceFiscale",codiceFiscale);

        startActivity(intent);
        finish();
    }

    public void riceviCittadino(){
        EditText aux;
        final Intent intent = getIntent();
        final String nome = intent.getStringExtra("nome");
        final String cognome = intent.getStringExtra("cognome");
        final String codiceFiscale = intent.getStringExtra("codiceFiscale");
        final String residenza = intent.getStringExtra("residenza");

        mNome = nome;
        mCognome = cognome;
        mCodiceFiscale = codiceFiscale;
        mResidenza = residenza;


        aux = (EditText) this.findViewById(R.id.NomeCittadino);
        aux.setText(nome);
        aux = (EditText) this.findViewById(R.id.cognomeCittadino);
        aux.setText(cognome);
        aux = (EditText) this.findViewById(R.id.codiceFiscale);
        aux.setText(codiceFiscale);
        aux = (EditText) this.findViewById(R.id.residenzaCittadino);
        aux.setText(residenza);
    }

    private boolean controlliCampi(){
        Boolean bool = false;
        aux = (EditText) this.findViewById(R.id.NomeCittadino);
        if (controlloEditTextVuoto(aux) == true){
            mostraMessaggioErrore("Campo obbligatorio", "Inserisci il tuo nome", RegistrazioneCittadino.this);
            bool = true;
        }
        aux = (EditText) this.findViewById(R.id.cognomeCittadino);
        if(controlloEditTextVuoto(aux) == true) {
            mostraMessaggioErrore("Campo obbligatorio", "Inserisci il tuo cognome", RegistrazioneCittadino.this);
            bool = true;
        }
            aux = (EditText) this.findViewById(R.id.codiceFiscale);
        if (controlloEditTextVuoto(aux) == true) {
            mostraMessaggioErrore("Campo obbligatorio", "Inserisci il codice fiscale", RegistrazioneCittadino.this);
            bool = true;
        }
            /*
       PRIMA DI CANCELLARE CHIEDERE A RINO! CAPITO BASTARDI?
        else if (verificaCodiceFiscale(aux) == true)
            mostraMessaggioErrore("Codice fiscale non valido","Il codice fiscale deve essere da 16 caratteri", RegistrazioneCittadino.this);
*/
        aux = (EditText) this.findViewById(R.id.residenzaCittadino);
        if(controlloEditTextVuoto(aux) == true) {
            mostraMessaggioErrore("Campo obbligatorio", "Inserisci la tua residenza", RegistrazioneCittadino.this);
            bool = true;
        }
        return bool;
    }

}
