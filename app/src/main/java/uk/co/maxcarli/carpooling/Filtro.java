package uk.co.maxcarli.carpooling;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.maxcarli.carpooling.Control.Controlli;
import uk.co.maxcarli.carpooling.Fragment.DataFragment;
import uk.co.maxcarli.carpooling.Fragment.TimeFragment;
import uk.co.maxcarli.carpooling.model.Cittadino;
import uk.co.maxcarli.carpooling.model.Passaggio;

public class Filtro extends AppCompatActivity implements  AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener,DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    AutoCompleteTextView driver;
    EditText sel_data;
    EditText sel_ora;
    RadioGroup groupTypeTrip;
    RadioButton home_work;
    RadioButton work_home;
    Button ricerca;
    Spinner spnposti;
    Cittadino cittadino;

    private ArrayAdapter<String> adapter;
    private List<String> responseList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro);

        if(savedInstanceState==null){
            cittadino=getIntent().getParcelableExtra(Cittadino.Keys.IDCITTADINO);
        }else{
            cittadino=savedInstanceState.getParcelable(Cittadino.Keys.IDCITTADINO);
        }

        driver=findViewById(R.id.driverSearch);



        setAutoCompleteViewDriver();



        //Create adapter
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, responseList);

        driver.setThreshold(1);


        //Set adapter to AutoCompleteTextView
        driver.setAdapter(adapter);
        driver.setOnItemSelectedListener(this);
        driver.setOnItemClickListener(this);



        sel_data=  findViewById(R.id.edtselezionadata);
        sel_ora=  findViewById(R.id.edtselezionaora);

        groupTypeTrip=(RadioGroup)findViewById(R.id.groupTypeRequest);

        home_work=(RadioButton)findViewById(R.id.casaLavoroRicerca);
        home_work.setChecked(true);
        work_home=(RadioButton)findViewById(R.id.LavoroCasaRicerca);

        ricerca=findViewById(R.id.btnricerca);
        ArrayAdapter<String> posti= new ArrayAdapter<String>(Filtro.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.names));
        posti.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sel_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datepicker= new DataFragment();
                datepicker.show(getFragmentManager(), "datapicker");
            }
        });
        sel_ora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timepicker= new TimeFragment();
                timepicker.show(getFragmentManager(), "timepicker");
            }
        });
        ricerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data;
                String ora;
                Passaggio p=new Passaggio();

                String automobilista=driver.getText().toString();



                if(!Controlli.controlloEditTextVuoto(sel_data,Filtro.this) && !Controlli.controlloEditTextVuoto(sel_ora,Filtro.this)){
                    data=sel_data.getText().toString();
                    ora=sel_ora.getText().toString();
                    p.setData(data);
                    p.setOra(ora);
                    String nomeOfferente="";
                    String cognomeOfferente="";
                    if(!automobilista.equals("")){
                        nomeOfferente=automobilista.substring(automobilista.indexOf(" "));
                        cognomeOfferente=automobilista.substring(0,automobilista.indexOf(" "));
                        nomeOfferente=nomeOfferente.trim();
                        cognomeOfferente=cognomeOfferente.trim();
                    }
                    p.setCittadinoOfferente(new Cittadino());
                    p.getCittadinoOfferente().setNome(nomeOfferente);
                    p.getCittadinoOfferente().setCognome(cognomeOfferente);

                    if(groupTypeTrip.getCheckedRadioButtonId()==home_work.getId()){
                        p.setTipoPassaggio("Casa-Lavoro");
                    }else{
                        p.setTipoPassaggio("Lavoro-Casa");
                    }
                    if (cittadino.passaggiRichiesti.contains(p) || cittadino.passaggiOfferti.contains(p)){
                        Controlli.mostraMessaggioErrore(getString(R.string.ErrorePassaggioRichiestoPresenteTitolo),getString(R.string.ErrorePassaggioRichiestoPresenteTesto),Filtro.this);
                    }
                    else{


                        Intent intent= new Intent(Filtro.this,MappaCercaPassaggi.class);

                        intent.putExtra(Cittadino.Keys.IDCITTADINO,cittadino);
                        intent.putExtra(Passaggio.Keys.IDPASSAGGIO,p);
                        startActivityForResult(intent,0);
                    }
                }


            }
        });
    }




    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c= Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String currant_date= DateFormat.getDateInstance().format(c.getTime());
        sel_data.setText(currant_date);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
        c.set(Calendar.MINUTE,minute);
        sel_ora.setText("  "+hourOfDay+ " : "+ minute);
    }


    public void setAutoCompleteViewDriver(){
        String url= "http://carpoolingsms.altervista.org/PHP/getAutomobilisti.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONArray jsonarray = new JSONArray(response);



                            for(int i=0; i < jsonarray.length(); i++) {

                                final JSONObject jsonobject = jsonarray.getJSONObject(i);


                                String  nome = jsonobject.getString("NomeCittadino");
                                String  cognome = jsonobject.getString("CognomeCittadino");
                                String completa = cognome + " " + nome;

                                responseList.add(completa);

                            }



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

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("sede", cittadino.getSede().getIdSede()+"");
                return params;
            }
        };

        MySingleton.getmInstance(getApplicationContext()).addTorequestque(stringRequest);


    }


    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable(Cittadino.Keys.IDCITTADINO,cittadino);
    }

    @Override
    public void onItemClick(AdapterView<?>  arg0, View arg1, int arg2, long arg3) {


        Log.d("AutocompleteContacts", "Position:"+arg2+" Month:"+arg0.getItemAtPosition(arg2));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent databack) {

        if (databack != null) {
            Passaggio passaggioPrenotato = (Passaggio) databack.getParcelableExtra(Passaggio.Keys.IDPASSAGGIO);

            if (passaggioPrenotato != null) {
                cittadino.addPassaggioRichiesto(passaggioPrenotato);
                final Intent returnIntent = new Intent();
                returnIntent.putExtra(Cittadino.Keys.IDCITTADINO, cittadino);
                setResult(2, returnIntent);
            }
        }

    }

}
