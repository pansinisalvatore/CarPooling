package uk.co.maxcarli.carpooling.Bluethoot;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import uk.co.maxcarli.carpooling.R;

import static uk.co.maxcarli.carpooling.Control.ControlBluetooth.*;

public class ClientConnect extends AppCompatActivity {
    public static final int REQUEST_ENABLE_BT = 1;
//mm
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_connect);
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(verificaSupportoB(mBluetoothAdapter) == true) accendiBluetooth(mBluetoothAdapter);
        String macAddress = "vuoto";
        if(bluetoothIsActive(mBluetoothAdapter) == true) macAddress = getBluetoothMacAddress();
        Log.d("Adress",macAddress);

    }
    @Override
    protected void onResume()
    {
        super.onResume();
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String macAddress = "vuoto";
        if(bluetoothIsActive(mBluetoothAdapter) == true) macAddress = getBluetoothMacAddress();

        Log.d("Adress",macAddress);

    }
        public void accendiBluetooth( BluetoothAdapter mBluetoothAdapter){

        if (bluetoothIsActive(mBluetoothAdapter) == false){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        }

}
