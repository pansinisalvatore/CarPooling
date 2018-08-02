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

    String url ="http://carpoolingsms.altervista.org/PHP/query.php";
    AlertDialog.Builder builder;
    TextInputEditText nomeText;
    TextInputEditText cognomeText;
    TextInputEditText CFText;
    EditText residenzaText;
    EditText aux2;

    private String mNome;
    private String mCognome;
    private String mCodiceFiscale;
    private String mResidenza;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione_cittadino);
        nomeText=findViewById(R.id.nomeCittadino);
        cognomeText=findViewById(R.id.cognomeCittadino);
        residenzaText=findViewById(R.id.residenzaCittadino);
        CFText=findViewById(R.id.codiceFiscale);

        //riceviCittadino();

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
{

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String,String> params = new HashMap<String, String>();
                params.put("nome", nome);
                params.put("cognome", cognome);
                return params;
            }
        };
                Toast.makeText(RegistrazioneCittadino.this, "Error...", Toast.LENGTH_SHORT).show();
                error.printStackTrace();

            }
        })


        MySingleton.getmInstance(RegistrazioneCittadino.this).addTorequestque(stringRequest);



    }*/



    public void scegliIndirizzo(View view){

        Intent intent=new Intent(this,RicercaIndirizzo.class);
        startActivityForResult(intent,0);

    }

    /*public void riceviCittadino(){
        final Intent intent = getIntent();
        final String nome = intent.getStringExtra("nome");
        final String cognome = intent.getStringExtra("cognome");
        final String codiceFiscale = intent.getStringExtra("codiceFiscale");
        final String residenza = intent.getStringExtra("residenza");

        mNome = nome;
        mCognome = cognome;
        mCodiceFiscale = codiceFiscale;
        mResidenza = residenza;


        aux = (TextInputEditText) this.findViewById(R.id.nomeCittadino);
        aux.setText(nome);
        aux = (TextInputEditText) this.findViewById(R.id.cognomeCittadino);
        aux.setText(cognome);
        aux = (TextInputEditText) this.findViewById(R.id.codiceFiscale);
        aux.setText(codiceFiscale);
        aux2 = (EditText) this.findViewById(R.id.residenzaCittadino);
        aux2.setText(residenza);
    }*/

    private boolean controlliCampi(){
        boolean bool = false;

        if (controlloEditTextVuoto(nomeText) == true){
            mostraMessaggioErrore("Campo obbligatorio", "Inserisci il tuo nome", RegistrazioneCittadino.this);
            bool = true;
        }

        if(controlloEditTextVuoto(cognomeText) == true) {
            mostraMessaggioErrore("Campo obbligatorio", "Inserisci il tuo cognome", RegistrazioneCittadino.this);
            bool = true;
        }

        if (controlloEditTextVuoto(CFText) == true) {
            mostraMessaggioErrore("Campo obbligatorio", "Inserisci il codice fiscale", RegistrazioneCittadino.this);
            bool = true;
        }
            /*
       PRIMA DI CANCELLARE CHIEDERE A RINO! CAPITO BASTARDI?
        else if (verificaCodiceFiscale(aux) == true)
            mostraMessaggioErrore("Codice fiscale non valido","Il codice fiscale deve essere da 16 caratteri", RegistrazioneCittadino.this);
*/
        aux2 = (EditText) this.findViewById(R.id.residenzaCittadino);
        if(controlloEditTextVuoto(residenzaText) == true) {
            mostraMessaggioErrore("Campo obbligatorio", "Inserisci la tua residenza", RegistrazioneCittadino.this);
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

}
