package uk.co.maxcarli.carpooling;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class ListaUtenti extends AppCompatActivity {


    String[] DatiUtente={"Nome","Cognome","CodiceFiscale"};
    String[][] r_utente ;
    List<Utenti> utenti=new ArrayList<>();
    TableView<String[]> tab;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_utenti);
        tab=(TableView<String[]>)findViewById(R.id.TabellaUtenti);
        tab.setColumnCount(3);
        tab.setHeaderBackgroundColor(Color.parseColor("#2ecc71"));

        String url= "http://carpoolingsms.altervista.org/PHP/Utenti.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {

                            JSONArray jsonarray = new JSONArray(response);

                            for(int i=0; i < jsonarray.length(); i++) {

                                JSONObject jsonobject = jsonarray.getJSONObject(i);

                                String nome= jsonobject.getString("NomeCittadino");
                                String cognome=jsonobject.getString("CognomeCittadino");
                                String codice_fiscale=jsonobject.getString("CodiceFiscaleCittadino");

                                Log.i("Dati", nome+" "+cognome+" "+ codice_fiscale );
                                utenti.add(new Utenti(nome,cognome,codice_fiscale));


                            }
                            populateData(utenti);
                            tab.setHeaderAdapter(new SimpleTableHeaderAdapter(getApplication(),DatiUtente));
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

         /*   protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Azienda", azienda);
                return params;
            }
        //
        */

        };

        MySingleton.getmInstance(getApplicationContext()).addTorequestque(stringRequest);

        Toast.makeText(getApplicationContext(),utenti.size()+" ",Toast.LENGTH_SHORT).show();




    }
    public void populateData(List<Utenti> passaggiRichiesti){

        this.r_utente=new String[utenti.size()][3];


        for(int i=0;i<utenti.size();i++){

            Utenti ut= utenti.get(i);

            this.r_utente[i][0]=ut.getNome();
            this.r_utente[i][1]=ut.getCognome();
            this.r_utente[i][2]=ut.getCodice_fiscale();


        }
    }


}
