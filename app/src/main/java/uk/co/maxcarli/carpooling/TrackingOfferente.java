package uk.co.maxcarli.carpooling;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import uk.co.maxcarli.carpooling.Bluethoot.Tracking;
import uk.co.maxcarli.carpooling.model.Cittadino;
import uk.co.maxcarli.carpooling.model.Passaggio;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static uk.co.maxcarli.carpooling.Control.ControlBluetooth.*;

public class TrackingOfferente extends AppCompatActivity {
    private Passaggio passaggio;
    private Cittadino cittadino;
    private FusedLocationProviderClient client;
    public String posizione;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_offerente);
        String pos;
        requestPermission();
        visibilita(this);
        passaggio = getIntent().getParcelableExtra(Passaggio.Keys.IDPASSAGGIO);
        cittadino = getIntent().getParcelableExtra(Cittadino.Keys.IDCITTADINO);
        Log.d("TrackingOfferente", cittadino.getCognome().toString());
        rotate(true);
        client = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        client.getLastLocation().addOnSuccessListener(TrackingOfferente.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                Log.d("Successo","si");
                if(location != null){
                    Log.d("posizioneIF",location.toString());
                    posizione = location.toString();

                }

            }
        });

    }

    public void rotate(boolean start) {
        if (start == true) {

            View decorView = getWindow().getDecorView();
            ImageView splash = findViewById(R.id.splash_imageT);
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.rotate);
            splash.startAnimation(animation);
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }


    private void requestPermission(){
        ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION},1);
    }
}
