package uk.co.maxcarli.carpooling;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class DettagliUtente extends AppCompatActivity {
    TextView nam;
    TextView surnam;
    TextView mail;
    TextView fiscal_cod;
    TextView residenc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nam=findViewById(R.id.txtName);
        surnam=findViewById(R.id.txtSurname);
        mail=findViewById(R.id.txtAddressEmai);
        fiscal_cod=findViewById(R.id.txtCodFiscale);
        residenc=findViewById(R.id.txtResidence);
        setContentView(R.layout.activity_dettagli_utente);
        String url = "http://carpoolingsms.altervista.org/PHP/DettagliUtente.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {

                            JSONArray jsonarray = new JSONArray(response);

                            for (int i = 0; i < jsonarray.length(); i++) {

                                JSONObject jsonobject = jsonarray.getJSONObject(i);


                                String nome = jsonobject.getString("NomeCittadino");
                                String cognome = jsonobject.getString("CognomeCittadino");
                                String email = jsonobject.getString("EmailCittadino");
                                String codice_fiscale = jsonobject.getString("CodiceFiscaleCittadino");
                                String residenza = jsonobject.getString("ResidenzaCittadino");

                                nam.setText(nome);
                                surnam.setText(cognome);
                                mail.setText(email);
                                fiscal_cod.setText(codice_fiscale);
                                residenc.setText(residenza);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null) {

                            Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                        }
                    }
                }) {



        };

        MySingleton.getmInstance(getApplicationContext()).addTorequestque(stringRequest);

    }
}