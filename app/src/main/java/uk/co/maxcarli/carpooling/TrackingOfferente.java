package uk.co.maxcarli.carpooling;

import android.bluetooth.BluetoothAdapter;
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
import android.widget.ImageView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import uk.co.maxcarli.carpooling.Control.ControlBluetooth;
import uk.co.maxcarli.carpooling.Control.Controlli;
import uk.co.maxcarli.carpooling.model.Cittadino;
import uk.co.maxcarli.carpooling.model.Passaggio;
import static uk.co.maxcarli.carpooling.Control.ControlBluetooth.*;
import static uk.co.maxcarli.carpooling.Control.Controlli.getOraCorrente;
import static uk.co.maxcarli.carpooling.Control.Controlli.oreInMinuti;

public class TrackingOfferente extends AppCompatActivity {

    public static final int REQUEST_ENABLE_BT = 1;

    private BluetoothAdapter mBluetoothAdapter;
    private Cittadino cittadino;
    private Passaggio passaggio;
    private ArrayList <String> macAddressTrovati;
    private int finito;
    private ArrayList <Cittadino> cittadiniTrovati;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_offerente);
        rotate(true);
        macAddressTrovati = new ArrayList<String>();
        cittadiniTrovati = new ArrayList<Cittadino>();
        final Intent srcIntent = getIntent();
        cittadino = (Cittadino) srcIntent.getParcelableExtra(Cittadino.Keys.IDCITTADINO);
        passaggio = (Passaggio) srcIntent.getParcelableExtra(Passaggio.Keys.IDPASSAGGIO);

        mBluetoothAdapter	= BluetoothAdapter.getDefaultAdapter();
        finito = 0;
        int aux = 0;
        avviaRicerca();
        Log.d("stoNel", "onCreate");



    }

    @Override
    public void onPause() {
        if (mBluetoothAdapter != null) {
            if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();
            }
        }
        Log.d("StoNel","onPause");
        super.onPause();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        Log.d("StoNel","onDestroy");
        super.onDestroy();
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();


            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                if (state == BluetoothAdapter.STATE_ON) {
                    showToast("Enabled");

                }

            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                showToast("ACTION_DISCOVERY_STARTED");
                showLog("ACTION_DISCOVERY_STARTED", action);


            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                showToast("ACTION_DISCOVERY_FINISHED");
                showLog("ACTION_DISCOVERY_FINISHED", action);
                addCittadiniTrovati();
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                macAddressTrovati.add(device.getAddress());
                showToast("Found device " + device.getName());
                showLog("ACTION_FOUND", action);
                showLog("dispositivo",device.getAddress());
                showLog("stoNel", "actionFound");


            }
        }
    };

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void showLog(String messaggio1, String messaggio2){
        Log.d(messaggio1,messaggio2);
    }


    public void rotate(boolean start) {
        if (start == true) {

            View decorView = getWindow().getDecorView();
            ImageView splash = findViewById(R.id.splash_image);
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.rotate);
            splash.startAnimation(animation);
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

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

    /**
     * Confronta i mac address dei cittadini richiedenti con quelli che il bluetooth ha rilevato.
     Quelli uguali li aggiunge nell'arrayList cittadiniTrovati
     */
    public void addCittadiniTrovati(){
        showLog("stoNel","Visualizza");
        Cittadino c;
        for (int i = 0; i < passaggio.cittadiniRichiedenti.size(); i++) {
            showLog("Status", passaggio.cittadinoStatus.get(i));
            if (passaggio.cittadinoStatus.get(i).equals("accettato")) {
                c = passaggio.cittadiniRichiedenti.get(i);
                for (int j = 0; j < macAddressTrovati.size(); j++) {
                    if (c.getMacAddress().equalsIgnoreCase(macAddressTrovati.get(j))) {
                        String mac = c.getMacAddress();
                        if (!macExists(mac))
                            cittadiniTrovati.add(c);
                    }
                }

            }
        }
        for (int i = 0; i < macAddressTrovati.size(); i++) showLog("macAddressTrov", macAddressTrovati.get(i));//showLog("macAddressTrov",macAddressTrovati.get(i));
        for (int i = 0; i < cittadiniTrovati.size(); i++) showLog("Trovati", cittadiniTrovati.get(i).getMacAddress() + " " + cittadiniTrovati.size());

        avviaRicerca();

    }

    public boolean macExists(String string){

        for (int i = 0; i < cittadiniTrovati.size(); i++) {
            if (cittadiniTrovati.get(i).getMacAddress().equalsIgnoreCase(string)) return true;
        }
        return false;
    }

    public void visualizzaMacAddressTrovati(){

        for (int i = 0; i < macAddressTrovati.size(); i++) showLog("macAddressTrov", macAddressTrovati.get(i));//showLog("macAddressTrov",macAddressTrovati.get(i));
        for (int i = 0; i < cittadiniTrovati.size(); i++) showLog("Trovati", cittadiniTrovati.get(i).getMacAddress());
    }

    public void finito(View view){

        cittadino.setPunteggio(cittadiniTrovati.size());
        for (int i = 0; i < cittadiniTrovati.size(); i++)
            cittadiniTrovati.get(i).setPunteggio();
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        int idPassaggio = passaggio.getIdPassaggiOfferti();
        Database.trackingEffettuato(1,idPassaggio,this);
        Controlli.mostraMessaggioConChiusura("Successo","avvenuto con successo", this);
    }

    public void avviaRicerca(){


        if (accendiBluetooth(mBluetoothAdapter)) {


            if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();
            }
            mBluetoothAdapter.startDiscovery();

            IntentFilter filter = new IntentFilter();

            filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
            filter.addAction(BluetoothDevice.ACTION_FOUND);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

            registerReceiver(mReceiver, filter);
        }
    }


}
