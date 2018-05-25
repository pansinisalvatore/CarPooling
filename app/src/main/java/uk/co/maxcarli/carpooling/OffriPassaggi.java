package uk.co.maxcarli.carpooling;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TimeFormatException;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import java.text.DateFormat;
import java.util.Calendar;

public class OffriPassaggi extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {
    Button imp_data;
    Button imp_ora;
    TextView set_data;
    TextView set_ora;
    EditText set_auto;
    Spinner set_posti;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offri_passaggi);
        imp_data= findViewById(R.id.btnData);
        imp_ora= findViewById(R.id.btnOra);
        set_auto= findViewById(R.id.edtInserisciAuto);
        set_posti= findViewById(R.id.spnPosti);
        ArrayAdapter<String> posti= new ArrayAdapter<String>(OffriPassaggi.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.names));
        posti.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        set_posti.setAdapter(posti);
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
        String currant_date= DateFormat.getDateInstance().format(c.getTime());
        TextView t= findViewById(R.id.txtData);
        set_data= findViewById(R.id.txtData);
        set_data.setText(currant_date);
    }
    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfday, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,hourOfday);
        c.set(Calendar.MINUTE,minute);
        set_ora=findViewById(R.id.txtOra);
        set_ora.setText("  "+hourOfday+ " : "+ minute);
    }
}