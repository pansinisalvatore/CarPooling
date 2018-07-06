package uk.co.maxcarli.carpooling;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.Calendar;

import uk.co.maxcarli.carpooling.Control.Controlli;
import uk.co.maxcarli.carpooling.Fragment.DataFragment;
import uk.co.maxcarli.carpooling.Fragment.TimeFragment;
import uk.co.maxcarli.carpooling.model.Cittadino;
import uk.co.maxcarli.carpooling.model.Passaggio;

public class Filtro extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    TextInputEditText sel_data;
    TextInputEditText sel_ora;
    RadioGroup groupTypeTrip;
    RadioButton home_work;
    RadioButton work_home;
    Button ricerca;
    Spinner spnposti;
    Cittadino cittadino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro);

        if(savedInstanceState==null){
            cittadino=getIntent().getParcelableExtra(Cittadino.Keys.IDCITTADINO);
        }else{
            cittadino=savedInstanceState.getParcelable(Cittadino.Keys.IDCITTADINO);
        }

        sel_data=  findViewById(R.id.edtselezionadata);
        sel_ora=  findViewById(R.id.edtselezionaora);

        home_work=findViewById(R.id.casaLavoroRicerca);
        home_work.setChecked(true);
        work_home=findViewById(R.id.LavoroCasaRicerca);

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


                if(!Controlli.controlloEditTextVuoto(sel_data)&& !Controlli.controlloEditTextVuoto(sel_ora)){
                    data=sel_data.getText().toString();
                    ora=sel_ora.getText().toString();
                    p.setData(data);
                    p.setOra(ora);
                    groupTypeTrip=findViewById(R.id.groupTypePassage);
                    if(groupTypeTrip.getCheckedRadioButtonId()==home_work.getId()){
                        p.setTipoPassaggio("Casa-Lavoro");
                    }else{
                        p.setTipoPassaggio("Lavoro-Casa");
                    }

                    if (cittadino.passaggiRichiesti.contains(p)){
                        Controlli.mostraMessaggioErrore(getString(R.string.ErrorePassaggioPresenteTitolo),getString(R.string.ErrorePassaggioPresenteTesto),Filtro.this);
                    }else{
                        Intent intent= new Intent(Filtro.this,MappaCercaPassaggi.class);

                        intent.putExtra(Cittadino.Keys.IDCITTADINO,cittadino);
                        intent.putExtra(Passaggio.Keys.IDPASSAGGIO,p);
                        startActivity(intent);
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


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        savedInstanceState.putParcelable(Cittadino.Keys.IDCITTADINO,cittadino);
    }
}
