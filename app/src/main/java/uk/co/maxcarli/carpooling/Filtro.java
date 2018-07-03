package uk.co.maxcarli.carpooling;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.Calendar;

import uk.co.maxcarli.carpooling.Fragment.DataFragment;
import uk.co.maxcarli.carpooling.Fragment.TimeFragment;

public class Filtro extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    TextInputEditText sel_data;
    TextInputEditText sel_ora;
    Button ricerca;
    Spinner spnposti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro);
        sel_data=  findViewById(R.id.edtselezionadata);
        sel_ora=  findViewById(R.id.edtselezionaora);

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
                String data=sel_data.getText().toString();
                String ora=sel_ora.getText().toString();
                Intent intent= new Intent(Filtro.this,MappaCercaPassaggi.class);
                startActivity(intent);
                intent.putExtra("data",data);
                intent.putExtra("ora",ora);
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
}
