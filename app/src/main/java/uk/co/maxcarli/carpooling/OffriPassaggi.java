package uk.co.maxcarli.carpooling;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import uk.co.maxcarli.carpooling.Fragment.DataFragment;
import uk.co.maxcarli.carpooling.Fragment.TimeFragment;
import uk.co.maxcarli.carpooling.model.Cittadino;

//ciao
public class OffriPassaggi extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {
    EditText imp_data;
    EditText imp_ora;
    EditText set_auto;
    Spinner set_posti;
    static String urlOffriPassaggio ="http://carpoolingsms.altervista.org/PHP/OffriPassaggi.php";
    static AlertDialog.Builder builder;
    Button conferma;
    String currant_date;
    String currant_time;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offri_passaggi);
        imp_data= findViewById(R.id.edtData);
        imp_ora= findViewById(R.id.edtOra);
        set_auto= findViewById(R.id.edtInserisciAuto);
        set_posti= findViewById(R.id.spnPosti);
        conferma= findViewById(R.id.btnConfirm);
        ArrayAdapter<String> posti= new ArrayAdapter<String>(OffriPassaggi.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.names));
        posti.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        set_posti.setAdapter(posti);


        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String auto= set_auto.getText().toString();
                String posti= set_posti.getSelectedItem().toString();
                Toast.makeText(OffriPassaggi.this,currant_date,Toast.LENGTH_LONG);
                Toast.makeText(OffriPassaggi.this,currant_time,Toast.LENGTH_LONG);

                OffriPassaggi(currant_date,currant_time,auto,posti,OffriPassaggi.this);
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


    public static void OffriPassaggi(final String date, final String time,final String car,final String place_avaiable,final Context context){
        builder = new AlertDialog.Builder(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlOffriPassaggio,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        builder.setTitle("Server Response");
                        builder.setMessage("Response"+ response);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        AlertDialog alertDialog= builder.create();
                        alertDialog.show();
                    }
                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context, "Error...", Toast.LENGTH_SHORT).show();
                error.printStackTrace();

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String,String> params = new HashMap<String, String>();
                params.put("date",date);
                params.put("time", time);
                params.put("car",car);
                params.put("place_avaible",place_avaiable);

                return params;
            }
        };


        MySingleton.getmInstance(context).addTorequestque(stringRequest);



    }

}