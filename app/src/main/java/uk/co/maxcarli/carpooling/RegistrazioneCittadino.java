package uk.co.maxcarli.carpooling;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static uk.co.maxcarli.carpooling.Database.registraCittadino;

public class RegistrazioneCittadino extends AppCompatActivity {

    String url ="http://carpoolingsms.altervista.org/PHP/query.php";
    AlertDialog.Builder builder;
    EditText aux;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione_cittadino);

        final String cittaResidenza = riceviIndirizzo();
        EditText residenzaCittadino = (EditText) this.findViewById(R.id.residenzaCittadino);
        residenzaCittadino.setText(cittaResidenza);

        findViewById(R.id.buttonAvanti).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextRegistrazione(cittaResidenza);
            }
        });
    }


    public void editTextRegistrazione(String residenza) {
        aux = (EditText) findViewById(R.id.NomeCittadino);
        String nome = aux.getText().toString();
        aux = (EditText) findViewById(R.id.ResidenzaCittadino);
        String cognome = aux.getText().toString();
        aux = (EditText) findViewById(R.id.codiceFiscale);
        String codiceFiscale = aux.getText().toString();

        Cittadino cittadino = new Cittadino(nome,cognome, codiceFiscale,residenza);
        registraCittadino(nome,cognome,RegistrazioneCittadino.this);

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

    public boolean verificaCodiceFiscale(String codiceFiscale){

        int lunghezza = codiceFiscale.length();
        if (lunghezza < 16 || lunghezza > 16){
            return true; //ritorna vero se è presente l'errore
        }
        else return false;
    }

    public void scegliIndirizzo(View view){
        final Intent intent = new Intent(this,RicercaIndirizzo.class);
        startActivity(intent);
    }

    public String riceviIndirizzo(){
        final Intent intent = getIntent();
        final String indirizzo = intent.getStringExtra("indirizzo");

        return indirizzo;
    }






}
