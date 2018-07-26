package uk.co.maxcarli.carpooling;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.*;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import static uk.co.maxcarli.carpooling.Control.ControlBluetooth.*;


public class TrackingRichiedente extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_richiedente);


            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter.isDiscovering()) {
                Log.d("startDiscovery", "true");
                mBluetoothAdapter.cancelDiscovery();
            }
                mBluetoothAdapter.startDiscovery();
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(mReceiver, filter);

    }
      BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
           final String action = intent.getAction();
            Log.d("action","ciao come stai");
            Toast.makeText(TrackingRichiedente.this, "ciaoComeStai", Toast.LENGTH_LONG).show();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                Log.d("deviceName", deviceName);
                Log.d("deviceHardwareAddress", deviceHardwareAddress);
                Toast.makeText(TrackingRichiedente.this, deviceName, Toast.LENGTH_LONG).show();
            }
        }
    };

    public void listaDispositiviAccoppiati(BluetoothAdapter mBluetoothAdapter){

        if (bluetoothIsActive(mBluetoothAdapter) == true){
            dispositiviAccoppiati(mBluetoothAdapter);
        }
    }
/*
    @Override
    protected void onDestroy() {
        super.onDestroy();


        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(mReceiver);
    }

*/
}
