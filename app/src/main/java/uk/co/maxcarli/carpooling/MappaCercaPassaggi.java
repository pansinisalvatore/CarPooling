package uk.co.maxcarli.carpooling;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MappaCercaPassaggi extends AppCompatActivity  implements OnMapReadyCallback {

    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mappa_cerca_passaggi);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        double raggio=500;
        Address a=getLocationFromAddress("Via Sparano 10, Bari");
        Address b=getLocationFromAddress("Via Argiro 10, Bari");
        Address c=getLocationFromAddress("Via Sagarriga Visconti 10, Bari");

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(a.getLatitude(),a.getLongitude()))
                .title("La tua casa")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));



        mMap.addCircle(new com.google.android.gms.maps.model.CircleOptions()
                .center(new LatLng(a.getLatitude(),a.getLongitude()))
                .radius(raggio)
        );

        if(controllo(a,b,raggio)){
            mMap.addMarker(new MarkerOptions().
                    position(new LatLng(b.getLatitude(),b.getLongitude())
                    ).title("La casa di un altro").
                    icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        }


        if(controllo(a,c,raggio)){
            mMap.addMarker(new MarkerOptions().
                    position(new LatLng(c.getLatitude(),c.getLongitude())
                    ).title("La casa di un altro").
                    icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(a.getLatitude(),a.getLongitude()),18.0f));


    }


    public boolean controllo(Address centro, Address pos, double raggio){


        float[] results=new float[1];
        Location.distanceBetween(centro.getLatitude(),centro.getLongitude(),pos.getLatitude(),pos.getLongitude(),results);
        Toast.makeText(this,"Distanza "+results[0],Toast.LENGTH_SHORT).show();
        if((double)results[0]<=raggio){
            return true;
        }else{
            return false;
        }
    }


    public Address getLocationFromAddress(String strAddress){

        Geocoder coder = new Geocoder(this);
        List<Address> address;


        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address==null) {
                return null;
            }
            Address location=address.get(0);
            return location;

        }catch(IOException e){
            return null ;
        }
    }


}
