package uk.co.maxcarli.carpooling;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import uk.co.maxcarli.carpooling.model.Cittadino;
import uk.co.maxcarli.carpooling.model.Sede;

public class ListaUtenti extends AppCompatActivity {


    String[] DatiUtente;
    String[][] r_utente ;
    List<Cittadino> utenti=new ArrayList<>();
    TableView<String[]> tab;

    int idSede;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_utenti);
        if(savedInstanceState!=null){
         idSede=savedInstanceState.getInt(Sede.Keys.IDSEDE);
        }else{
            idSede=getIntent().getIntExtra(Sede.Keys.IDSEDE,0);
        }

        DatiUtente=new String[3];
        DatiUtente[0]=getString(R.string.Dipendente);
        DatiUtente[1]=getString(R.string.Telefono);
        DatiUtente[2]=getString(R.string.Autorizzato);
        tab=(TableView<String[]>)findViewById(R.id.TabellaUtenti);
        tab.setColumnCount(3);
        tab.setHeaderBackgroundColor(Color.parseColor("#2ecc71"));
        tab.setHeaderAdapter(new SimpleTableHeaderAdapter(getApplication(),DatiUtente));
        prendiDatiUtenti();

        tab.addDataClickListener(new TableDataClickListener<String[]>() {
            @Override
            public void onDataClicked(int rowIndex, String[] clickedData) {
                passaCittadino(clickedData[1]);
            }
        });



    }
    public void populateData(List<Cittadino> utenti){

        this.r_utente=new String[utenti.size()][3];


        for(int i=0;i<utenti.size();i++){

             Cittadino ut= utenti.get(i);

            this.r_utente[i][0]=ut.getCognome()+" "+ut.getNome();
            this.r_utente[i][1]=ut.getNumeroTelefono();
            if(ut.getConvalidato()==0){
                this.r_utente[i][2]=getString(R.string.AttesaConvalidazione);
            }else{
                this.r_utente[i][2]=getString(R.string.Autorizzato);
            }



        }
    }

    public void prendiDatiUtenti(){
        String url= "http://carpoolingsms.altervista.org/PHP/Utenti.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {

                            JSONArray jsonarray = new JSONArray(response);

                            int convalidato=0;
                            for(int i=0; i < jsonarray.length(); i++) {

                                JSONObject jsonobject = jsonarray.getJSONObject(i);

                                String nome= jsonobject.getString("NomeCittadino");
                                String cognome=jsonobject.getString("CognomeCittadino");
                                String cell=jsonobject.getString("TelefonoCittadino");
                                convalidato=jsonobject.getInt("ConvalidatoCittadino");
                                Cittadino c=new Cittadino();
                                c.setNome(nome);
                                c.setCognome(cognome);
                                c.setNumeroTelefono(cell);
                                c.setConvalidato(convalidato);
                                utenti.add(c);
                            }
                            populateData(utenti);
                            tab.setDataAdapter(new SimpleTableDataAdapter(getApplication(),r_utente));
                        } catch (JSONException e) {
                            e.printStackTrace();

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
                params.put("idSede", idSede+"");
                return params;
            }



        };

        MySingleton.getmInstance(getApplicationContext()).addTorequestque(stringRequest);
    }

    public void passaCittadino(final String cell){
        String url= "http://carpoolingsms.altervista.org/PHP/DettagliUtente.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {

                            JSONArray jsonarray = new JSONArray(response);
                            Cittadino c=new Cittadino();
                            int convalidato=0;
                            for(int i=0; i < jsonarray.length(); i++) {

                                JSONObject jsonobject = jsonarray.getJSONObject(i);

                                String nome= jsonobject.getString("NomeCittadino");
                                String cognome=jsonobject.getString("CognomeCittadino");
                                String codice_fiscale=jsonobject.getString("CodiceFiscaleCittadino");
                                String cell=jsonobject.getString("TelefonoCittadino");
                                String residenza=jsonobject.getString("ResidenzaCittadino");
                                String email=jsonobject.getString("EmailCittadino");
                                convalidato=jsonobject.getInt("ConvalidatoCittadino");
                                int idCittadino=jsonobject.getInt("IdCittadino");

                                c.setIdCittadino(idCittadino);
                                c.setNome(nome);
                                c.setCognome(cognome);
                                c.setNumeroTelefono(cell);
                                c.setResidenza(residenza);
                                c.setCodiceFiscale(codice_fiscale);
                                c.setEmail(email);
                                c.setConvalidato(convalidato);
                                Log.i("Dati", nome+" "+cognome+" "+ codice_fiscale );
                            }

                            Intent intent=new Intent(ListaUtenti.this,DettagliUtente.class);
                            intent.putExtra(Cittadino.Keys.IDCITTADINO,c);
                            startActivityForResult(intent,0);


                        } catch (JSONException e) {
                            e.printStackTrace();

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
                params.put("cell", cell);
                return params;
            }



        };

        MySingleton.getmInstance(getApplicationContext()).addTorequestque(stringRequest);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent databack) {

        if (databack != null) {

            this.recreate();
        }

    }

    public void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        bundle.putInt(Sede.Keys.IDSEDE,idSede);
    }

}
