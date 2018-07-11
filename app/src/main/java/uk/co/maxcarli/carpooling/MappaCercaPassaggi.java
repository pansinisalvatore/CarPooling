package uk.co.maxcarli.carpooling;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.maxcarli.carpooling.Control.Controlli;
import uk.co.maxcarli.carpooling.model.Cittadino;
import uk.co.maxcarli.carpooling.model.Passaggio;

public class MappaCercaPassaggi extends AppCompatActivity  implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;

    private Cittadino cittadino;
    private Passaggio passaggio;

    private String indirizzoCittadino;
    private final String data="21/3/2013" ;
    private final String ora="21.10";
    private static final ArrayList<String> indirizzi=new ArrayList<String>();
    private static final ArrayList<String> automobilisti=new ArrayList<String>();
    private Address home;
    private Address work;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mappa_cerca_passaggi);
        cittadino=getIntent().getParcelableExtra(Cittadino.Keys.IDCITTADINO);
        passaggio=getIntent().getParcelableExtra(Passaggio.Keys.IDPASSAGGIO);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);

        double raggio=500;
        home=getLocationFromAddress(cittadino.getResidenza());
        getIndirizziPassaggiOfferti(this);

        work=getLocationFromAddress(cittadino.getSede().getIndirizzoSede());


        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(home.getLatitude(),home.getLongitude()))
                .title(getString(R.string.la_tua_casa))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(work.getLatitude(),work.getLongitude()))
                .title(getString(R.string.lavoro))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));


        mMap.addCircle(new com.google.android.gms.maps.model.CircleOptions()
                .center(new LatLng(home.getLatitude(),home.getLongitude()))
                .radius(raggio)
        );

        for(int i=0;i<indirizzi.size();i++){
            Address pos=getLocationFromAddress(indirizzi.get(i));
            if(controllo(home, pos, 500)){
                mMap.addMarker(new MarkerOptions().
                        position(new LatLng(pos.getLatitude(),pos.getLongitude())
                        ).title(getString(R.string.automobilista)+": "+automobilisti.get(i)).
                        icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            }
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(home.getLatitude(),home.getLongitude()),18.0f));


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

    private void getIndirizziPassaggiOfferti( final Context context) {
        String url = "http://carpoolingsms.altervista.org/PHP/PrendiIndirizzi.php";


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(context.getApplicationContext(),response,Toast.LENGTH_LONG).show();

                        if(!response.equals("Something went wrong")){
                            try {

                                JSONArray jsonarray = new JSONArray(response);

                                for (int i = 0; i < jsonarray.length(); i++) {

                                    JSONObject jsonobject = jsonarray.getJSONObject(i);

                                    String indirizzo = jsonobject.getString("ResidenzaCittadino");
                                    String cognome=jsonobject.getString("CognomeCittadino");
                                    String cell=jsonobject.getString("TelefonoCittadino");
                                    String nome=jsonobject.getString("NomeCittadino");
                                    int j=0;

                                    Address pos=getLocationFromAddress(indirizzo);

                                    if(controllo(home, pos, 500)){
                                        j++;
                                        mMap.addMarker(new MarkerOptions().
                                                position(new LatLng(pos.getLatitude(),pos.getLongitude())
                                                ).title(getString(R.string.automobilista)+": "+ cognome+" "+nome).snippet(getString(R.string.Prenotazione_passaggio)).
                                                icon(BitmapDescriptorFactory
                                                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                                    }
                                    if(j==0){
                                        Toast.makeText(context.getApplicationContext(),getString(R.string.OfferteNonPresenti),Toast.LENGTH_LONG).show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();

                            }
                        }else{
                            Toast.makeText(context.getApplicationContext(),getString(R.string.OfferteNonPresenti),Toast.LENGTH_LONG).show();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null) {

                            Toast.makeText(context.getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("data", passaggio.getData());
                params.put("ora", passaggio.getOra());
                params.put("tipo",passaggio.getTipoPassaggio());
                params.put("automobilista",passaggio.getAutomobilista());
                params.put("sede",cittadino.getSede().getIdSede()+"");
                return params;
            }
        };

        MySingleton.getmInstance(context.getApplicationContext()).addTorequestque(stringRequest);
    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        if(!marker.getTitle().equals("La tua casa")){

            String indirizzo=getAddressFromLatLng(marker.getPosition());
            //Toast.makeText(this, indirizzo, Toast.LENGTH_LONG).show();
           prenotaPassaggio(this, indirizzo);

        }


        return false;
    }




    public void prenotaPassaggio(final Context context, final String indirizzo){
        String url = "http://carpoolingsms.altervista.org/PHP/ScriviPassaggioRichiesto.php";


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equals("success")){

                            Toast.makeText(context,"Scrittura riuscita",Toast.LENGTH_SHORT).show();

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null) {

                            Toast.makeText(context.getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("address", indirizzo);
                params.put("idCittadino",cittadino.getIdCittadino()+"");
                params.put("data",passaggio.getData());
                params.put("ora",passaggio.getOra());
                params.put("tipo",passaggio.getTipoPassaggio());
                return params;
            }
        };

        MySingleton.getmInstance(context.getApplicationContext()).addTorequestque(stringRequest);
    }

    public String getAddressFromLatLng(LatLng pos){
        Geocoder coder=new Geocoder(this);
       try{
           List<Address> addr= coder.getFromLocation(pos.latitude,pos.longitude,5);
           if(addr!=null){

               String address=addr.get(0).getAddressLine(0);

               Toast.makeText(this, address, Toast.LENGTH_LONG).show();
               return address;
           }
           return null;

       }catch(IOException e){
            return null;
       }

    }
}
