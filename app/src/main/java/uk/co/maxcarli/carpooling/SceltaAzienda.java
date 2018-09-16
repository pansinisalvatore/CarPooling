package uk.co.maxcarli.carpooling;


import android.app.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.CompletionInfo;
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

import uk.co.maxcarli.carpooling.model.Cittadino;

import static uk.co.maxcarli.carpooling.Control.Controlli.*;

/**
 * La classe sceltaAzienda permette all'utente di scegliere l'azienda di appartenenza
 */
public class SceltaAzienda extends Activity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {


    // Initialize variables

    TextInputEditText telefonoText;
    AutoCompleteTextView textView=null;
    AutoCompleteTextView textSedi;
    private ArrayAdapter<String> adapter;
    private boolean verificaAzienda;
    private boolean verificaSede;
    String PartitaIvaAzienda = " ";

    private Cittadino cittadino;

    private String mNome = " ";
    private String mCognome = " ";
    private String mCodiceFiscale = " ";
    private String mResidenza = " ";

    List<String> responseList = new ArrayList<String>();
    List<String> responseListSedi=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedIstanceState) {
        super.onCreate(savedIstanceState);
        setContentView(R.layout.activity_scelta_azienda);

        if(savedIstanceState!=null){
            textView=(AutoCompleteTextView) findViewById(R.id.autocompleteId);
            textSedi=(AutoCompleteTextView)findViewById(R.id.autocompletesedeId);
            telefonoText=(TextInputEditText)findViewById(R.id.numeroTelefonicoCittadino);
            textView.setText(savedIstanceState.getString("azienda"));
            textSedi.setText(savedIstanceState.getString("sede"));
            telefonoText.setText(savedIstanceState.getString("telefono"));
            cittadino=savedIstanceState.getParcelable(Cittadino.Keys.IDCITTADINO);
        }

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

        textSedi=findViewById(R.id.autocompletesedeId);
        textSedi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                setTextViewSede();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



    }

    /**
     * setTextViewSede setta le textView in base ai valori presi dal database.
     */
    public void setTextViewSede(){


        String completoAzienda=textView.getText().toString();
        if(completoAzienda.equals("")){
            return;
        }

        String partitaIva=completoAzienda.substring(completoAzienda.indexOf(",")+1);
        partitaIva=partitaIva.trim();
        String url= "http://carpoolingsms.altervista.org/PHP/prendiSediAzienda.php";

        final String finalPartitaIva = partitaIva;

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(!response.equals("Something went wrong")){
                            try {

                                JSONArray jsonarray = new JSONArray(response);

                                // vettore = new String[jsonarray.length()];

                                for(int i=0; i < jsonarray.length(); i++) {

                                    final JSONObject jsonobject = jsonarray.getJSONObject(i);


                                    String indirizzoSede= jsonobject.getString("IndirizzoSede");
                                    if(!responseListSedi.contains(indirizzoSede)){
                                        responseListSedi.add(indirizzoSede);

                                    }

                                }




                            } catch (JSONException e) {
                                e.printStackTrace();

                            }
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

        ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String,String> params = new HashMap<String, String>();
                params.put("PartitaIva", finalPartitaIva);
                return params;
            }
        };

        MySingleton.getmInstance(getApplicationContext()).addTorequestque(stringRequest);


        //Create adapter
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, responseListSedi);
        textSedi.setThreshold(1);

        //Set adapter to AutoCompleteTextView
        textSedi.setAdapter(adapter);
        textSedi.setOnItemSelectedListener(this);
        textSedi.setOnItemClickListener(this);
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

        telefonoText =  this.findViewById(R.id.numeroTelefonicoCittadino);
        AutoCompleteTextView aux2 = (AutoCompleteTextView) this.findViewById(R.id.autocompleteId);
        String numeroTelefono = " ";

        if (errorControl(telefonoText, aux2) == false && errorControlSede(textSedi)){

            fromIntent();
            Log.d("Sono entrato nell'if","ciao");
            Log.d("stringhe", mNome+mCognome+mResidenza);

            numeroTelefono = telefonoText.getText().toString();
            cittadino.setNumeroTelefono(numeroTelefono);

            toIntent();


        }

    }

    public boolean errorControl(TextInputEditText textInputEditText, AutoCompleteTextView aux2){
        boolean control = false;
        boolean controlNumero = false;
        boolean controlEditText = false;

        control = controlloEditTextVuoto(textInputEditText,this);
        if (verificaNumeroTelefonico(textInputEditText) == true)
        {
            String title = getText(R.string.numeroTelefonoNonValido).toString();
            String text = getText(R.string.numeroTelefonoNonValidoText).toString();
            mostraMessaggioErrore(title, text, SceltaAzienda.this);
            controlNumero = true;
        }
        controlEditText = controlloEditTextVuoto(aux2,this);

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

            if(responseList.contains(aux2.getText().toString())){
                control=false;
            }else{
                control=true;
                String title = getText(R.string.aziendaNonValida).toString();
                String text = getText(R.string.aziendaNonValidaText).toString();
                mostraMessaggioErrore(title, text, SceltaAzienda.this);
            }
        }
            //se tutto è andato bene
            if ((controlNumero == false) && (control == false) && (success == true)) return false;
            else return true;

    }

    /**
     * Controlla se la sede inserita è valida.
     * @param view
     * @return
     */
    public boolean errorControlSede(final AutoCompleteTextView view){
        boolean controlEditText = controlloEditTextVuoto(view,this);
        boolean control=false;


        if(responseListSedi.contains(view.getText().toString())) {
            return true;
        }else{
            String title = getText(R.string.sedeNonValida).toString();
            String text = getText(R.string.sedeNonValidaText).toString();
            mostraMessaggioErrore(title, text, SceltaAzienda.this);
            return false;
        }
    }

    /**
     * Riceve il cittadino dall'activity precedente
     */
    private void fromIntent(){

        final Intent intent = getIntent();

        if(intent != null){

            cittadino=intent.getParcelableExtra(Cittadino.Keys.IDCITTADINO);

        }
    }

    /**
     * Avvia l'intent per passare all'activity successiva
     */
    private void toIntent(){

        final Intent intent = new Intent(this, completaRegistrazione.class);
        intent.putExtra("partitaIva",PartitaIvaAzienda);
        intent.putExtra("sede",textSedi.getText().toString());
        intent.putExtra(Cittadino.Keys.IDCITTADINO,cittadino);
        startActivity(intent);
        finish();
    }

    public void onSaveInstanceSTate(Bundle bundle){
        super.onSaveInstanceState(bundle);
        bundle.putString("telefono",telefonoText.getText().toString());
        bundle.putString("azienda",textView.getText().toString());
        bundle.putString("sede",textSedi.getText().toString());
        bundle.putParcelable(Cittadino.Keys.IDCITTADINO,cittadino);
    }


}
