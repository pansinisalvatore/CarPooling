package uk.co.maxcarli.carpooling.Bluethoot;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import uk.co.maxcarli.carpooling.R;
import static uk.co.maxcarli.carpooling.Control.ControlBluetooth.*;

public class avviaTraking extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avvia_traking);
    }

    public void avviaClient(View view){
        final Intent intent = new Intent(this, ClientConnect.class);
        startActivity(intent);
    }


    }






