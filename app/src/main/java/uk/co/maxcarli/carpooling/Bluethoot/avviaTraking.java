package uk.co.maxcarli.carpooling.Bluethoot;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import uk.co.maxcarli.carpooling.R;
import static uk.co.maxcarli.carpooling.Control.ControlBluetooth.*;

public class avviaTraking extends AppCompatActivity {
    public static final int REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avvia_traking);
    }

    public void avviaClient(View view){
        final Intent intent = new Intent(this, ClientConnect.class);
        startActivity(intent);
    }

    public void avviaServer(View view){
        if(avviaBluetooth() == true) {

            final Intent intent = new Intent(this, Tracking.class);
            startActivity(intent);
        }
    }

    public boolean avviaBluetooth(){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(verificaSupportoB(mBluetoothAdapter)== true){
            if(bluetoothIsActive(mBluetoothAdapter) == false){
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
        if(bluetoothIsActive(mBluetoothAdapter) == true) return true;
        else return false;
    }



}
