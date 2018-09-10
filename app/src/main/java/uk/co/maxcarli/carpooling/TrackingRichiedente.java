package uk.co.maxcarli.carpooling;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

import uk.co.maxcarli.carpooling.Control.ControlBluetooth;

import static uk.co.maxcarli.carpooling.Control.ControlBluetooth.*;

/**
 *Questa classe non fa altro che mantenere il bluetooth attivo e stabilisce una connessione con il database per
 verificare se il tracking è avvennuto con successo
 */
public class TrackingRichiedente extends Activity {


    private BluetoothAdapter mBluetoothAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_richiedente);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (accendiBluetooth(mBluetoothAdapter)) {

            if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();
            }

            ControlBluetooth.visibilita(TrackingRichiedente.this);

        }
        ImageView splash=findViewById(R.id.splash_image);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.rotate);
        splash.startAnimation(animation);

    }

    /**
     * Verifica che il bluetooth sia acceso, se non lo è chiede all'utente di accenderlo
     * @param mBluetoothAdapter
     * @return
     */
    public boolean accendiBluetooth( BluetoothAdapter mBluetoothAdapter) {

        boolean acceso = false;
        do {


            if (ControlBluetooth.verificaSupportoB(mBluetoothAdapter)) {


                if (!(mBluetoothAdapter.isEnabled())) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                } else acceso = true;
            } else acceso = false;

            if (mBluetoothAdapter.isEnabled()) acceso = true;
        }while (acceso == false);
        return acceso;
    }

    public void finitoRichiedente(View view){

    }






}

