package uk.co.maxcarli.carpooling;


import android.app.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
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
import java.util.regex.Pattern;

import static uk.co.maxcarli.carpooling.Control.Controlli.*;

public class SceltaAzienda extends Activity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {


    // Initialize variables

    AutoCompleteTextView textView=null;
    private ArrayAdapter<String> adapter;
    private boolean verificaAzienda;
    String PartitaIvaAzienda = " ";

    private String mNome = " ";
    private String mCognome = " ";
    private String mCodiceFiscale = " ";
    private String mResidenza = " ";

    List<String> responseList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedIstanceState) {
        super.onCreate(savedIstanceState);
        setContentView(R.layout.activity_scelta_azienda);


        // Get AutoCompleteTextView reference from xml
        textView = (AutoCompleteTextView) findViewById(R.id.autocompleteId);

        String url= "http://carpoolingsms.altervista.org/PHP/getAzienda.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONArray jsonarray = new JSONArray(response);

                            // vettore = new String[jsonarray.length()];

                            for(int i=0; i < jsonarray.length(); i++) {

                                final JSONObject jsonobject = jsonarray.getJSONObject(i);


                                String idAzienda = jsonobject.getString("IdAzienda");
                                String partitaIva = jsonobject.getString("PartitaIvaAzienda");
                                String nomeAzienda = jsonobject.getString("NomeAzienda");
                                String completa = nomeAzienda + ", " + partitaIva;

                                responseList.add(completa);

                                // vettore[i] = nomeAzienda+", "+partitaIva;

                            }
                            //Arrays.sort(vettore);



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
                }

        );

        MySingleton.getmInstance(getApplicationContext()).addTorequestque(stringRequest);


        //Create adapter
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, responseList);

        textView.setThreshold(1);

        //Set adapter to AutoCompleteTextView
        textView.setAdapter(adapter);
        textView.setOnItemSelectedListener(this);
        textView.setOnItemClickListener(this);

    }



    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
                               long arg3) {
        // TODO Auto-generated method stub
        //Log.d("AutocompleteContacts", "onItemSelected() position " + position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

        InputMethodManager imm = (InputMethodManager) getSystemService(
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub

        // Show Alert
        Toast.makeText(getBaseContext(), "Position:"+arg2+" Month:"+arg0.getItemAtPosition(arg2),
                Toast.LENGTH_LONG).show();

        Log.d("AutocompleteContacts", "Position:"+arg2+" Month:"+arg0.getItemAtPosition(arg2));

    }

    protected void onResume() {
        super.onResume();
    }

    protected void onDestroy() {
        super.onDestroy();
    }
    protected void onStart(){
        super.onStart();
    }

    public void clickButton(View view){

        TextInputEditText textInputEditText = (TextInputEditText) this.findViewById(R.id.numeroTelefonicoCittadino);
        AutoCompleteTextView aux2 = (AutoCompleteTextView) this.findViewById(R.id.autocompleteId);
        String azienda = " ";
        String numeroTelefono = " ";

        if (errorControl(textInputEditText, aux2) == false){

            fromIntent();
            Log.d("Sono entrato nell'if","ciao");
            Log.d("stringhe", mNome+mCognome+mResidenza);
            azienda = aux2.getText().toString();
            numeroTelefono = textInputEditText.getText().toString();
            toIntent(numeroTelefono);


        }

    }

    public boolean errorControl(TextInputEditText textInputEditText, AutoCompleteTextView aux2){
        boolean control = false;
        boolean controlNumero = false;
        boolean controlEditText = false;

        control = controlloEditTextVuoto(textInputEditText);
        if (verificaNumeroTelefonico(textInputEditText) == true)
        {
            String title = getText(R.string.numeroTelefonoNonValido).toString();
            String text = getText(R.string.numeroTelefonoNonValidoText).toString();
            mostraMessaggioErrore(title, text, SceltaAzienda.this);
            controlNumero = true;
        }
        controlEditText = controlloEditTextVuoto(aux2);

        PartitaIvaAzienda = " ";
        Boolean success = false;
        String url = "http://carpoolingsms.altervista.org/PHP/verificaAzienda.php";
        AutoCompleteTextView aux = (AutoCompleteTextView) this.findViewById(R.id.autocompleteId);
        String nomeAziendaaux = aux.getText().toString();
        if(nomeAziendaaux.indexOf(",") != -1) {

            String[] splitArr = Pattern.compile(",").split(nomeAziendaaux);
            nomeAziendaaux = splitArr[1];
            PartitaIvaAzienda = nomeAziendaaux;
            PartitaIvaAzienda = PartitaIvaAzienda.trim();
            success = true;
        } else{

            String title = getText(R.string.aziendaNonValida).toString();
            String text = getText(R.string.aziendaNonValidaText).toString();
            mostraMessaggioErrore(title, text, SceltaAzienda.this);
            Log.d("verificaAzienda", "true");

            success = false;
        }

        Log.d("PartitaIva", PartitaIvaAzienda);

        if (success == true) {


            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response.equals("1")) {
                                verificaAzienda = false;
                                Log.d("Response:", response);
                                Log.d("verificaAzienda", "false");
                            } else {
                                String title = getText(R.string.aziendaNonValida).toString();
                                String text = getText(R.string.aziendaNonValidaText).toString();
                                mostraMessaggioErrore(title, text, SceltaAzienda.this);
                                Log.d("Response:", response);
                                Log.d("verificaAzienda", "true");
                                verificaAzienda = true;


                            }

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
                    params.put("PartitaIvaAzienda", PartitaIvaAzienda);
                    return params;
                }
            };


            MySingleton.getmInstance(this).addTorequestque(stringRequest);
            control = verificaAzienda;
        }
            //se tutto è andato bene
            if ((controlNumero == false) && (control == false) && (success == true)) return false;
            else return true;

    }

    private void fromIntent(){

        final Intent intent = getIntent();

        if(intent != null){

            mNome = intent.getStringExtra("nome");
            mCognome = intent.getStringExtra("cognome");
            mCodiceFiscale = intent.getStringExtra("codiceFiscale");
            mResidenza = intent.getStringExtra("residenza");
        }
    }

    private void toIntent(String numeroTelefono){

        final Intent intent = new Intent(this, completaRegistrazione.class);
        intent.putExtra("nome",mNome);
        intent.putExtra("cognome", mCognome);
        intent.putExtra("codiceFiscale", mCodiceFiscale);
        intent.putExtra("residenza", mResidenza);
        intent.putExtra("azienda",PartitaIvaAzienda);
        intent.putExtra("numeroTelefono",numeroTelefono);
        startActivity(intent);

    }



}
