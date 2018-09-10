package uk.co.maxcarli.carpooling;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import uk.co.maxcarli.carpooling.Control.Controlli;
import uk.co.maxcarli.carpooling.Fragment.DataFragment;
import uk.co.maxcarli.carpooling.Fragment.TimeFragment;
import uk.co.maxcarli.carpooling.model.Cittadino;
import uk.co.maxcarli.carpooling.model.Passaggio;

//ciao
public class OffriPassaggi extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {

    private Cittadino cittadino;

    EditText imp_data;
    EditText imp_ora;
    EditText set_auto;
    Spinner set_posti;

    Button conferma;
    String currant_date;
    String currant_time;
    String auto;
    int posti;
    RadioButton buttonYes;
    RadioButton buttonNo;

    RadioGroup group;

    RadioGroup groupType;

    RadioButton casaLavoro;
    RadioButton lavoroCasa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState==null){
            cittadino=(Cittadino)getIntent().getParcelableExtra(Cittadino.Keys.IDCITTADINO);
        }else{
            savedInstanceState.getParcelable(Cittadino.Keys.IDCITTADINO);
        }


        setContentView(R.layout.activity_offri_passaggi);
        imp_data= findViewById(R.id.edtData);
        imp_ora= findViewById(R.id.edtOra);
        set_auto= findViewById(R.id.edtInserisciAuto);
        set_posti= findViewById(R.id.spnPosti);
        conferma= findViewById(R.id.btnConfirm);
        ArrayAdapter<String> posti= new ArrayAdapter<String>(OffriPassaggi.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.names));
        posti.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        set_posti.setAdapter(posti);


        casaLavoro=findViewById(R.id.Home_Work);
        lavoroCasa=findViewById(R.id.Work_Home);

        casaLavoro.setChecked(true);

        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!Controlli.controlloEditTextVuoto(imp_data,OffriPassaggi.this) && !Controlli.controlloEditTextVuoto(imp_ora,OffriPassaggi.this) &&  !Controlli.controlloEditTextVuoto(set_auto,OffriPassaggi.this)
                         ){
                    group=findViewById(R.id.radioGroup);
                    groupType=findViewById(R.id.groupTypePassage);


                    String data=imp_data.getText().toString();
                    String ora=imp_ora.getText().toString();
                    data=Controlli.impostaFormatoData(data);
                    String auto= set_auto.getText().toString();
                    int posti= Integer.parseInt((String)set_posti.getSelectedItem());



                    int idRisultatoTipoViaggio=groupType.getCheckedRadioButtonId();

                    String tipoPassaggio;


                    if(idRisultatoTipoViaggio==casaLavoro.getId()){
                        tipoPassaggio="Casa-Lavoro";
                    }else{
                        tipoPassaggio="Lavoro-Casa";
                    }
                    Passaggio p=new Passaggio();
                    p.setData(data);
                    p.setOra(ora);
                    p.setAuto(auto);
                    p.setPostiDisponibili(posti);

                    p.setTipoPassaggio(tipoPassaggio);

                    if(cittadino.passaggiOfferti.contains(p) || cittadino.passaggiRichiesti.contains(p)){
                        Controlli.mostraMessaggioErrore(getString(R.string.ErrorePassaggioRichiestoPresenteTitolo),getString(R.string.ErrorePassaggioOffertoPresenteTesto),OffriPassaggi.this);
                    }else{
                        cittadino.addPassaggioOfferto(p);
                        final Intent returnIntent = new Intent();
                        returnIntent.putExtra(Cittadino.Keys.IDCITTADINO,cittadino);
                        setResult(1,returnIntent);
                        Database.OffriPassaggi(p, cittadino,OffriPassaggi.this);
                    }

                }





            }
        });

        imp_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datepicker= new DataFragment();
                datepicker.show(getFragmentManager(),"datapicker");
            }
        });
        imp_ora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timepicker= new TimeFragment();
                timepicker.show(getFragmentManager(),"timepicker");
            }
        });


    }
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar c= Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        currant_date= DateFormat.getDateInstance().format(c.getTime());
        imp_data.setText(currant_date);

    }
    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfday, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,hourOfday);
        c.set(Calendar.MINUTE,minute);
        currant_time="  "+hourOfday+ " : "+ minute;
        imp_ora.setText(currant_time);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        savedInstanceState.putParcelable(Cittadino.Keys.IDCITTADINO,cittadino);
    }
}