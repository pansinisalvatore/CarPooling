package uk.co.maxcarli.carpooling;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class OffriPassaggi extends AppCompatActivity {
    Button imp_data;
    Button imp_ora;
    TextView set_data;
    TextView set_ora;
    EditText set_auto;
    EditText set_posti;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offri_passaggi);
        imp_data= findViewById(R.id.btnData);
        imp_ora= findViewById(R.id.btnOra);
        set_data= findViewById(R.id.txtData);
        set_ora=findViewById(R.id.txtOra);
        set_auto= findViewById(R.id.edtInserisciAuto);
        set_posti= findViewById(R.id.edtPostiDisponibili);

    }


    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimeFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DataFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }


}


