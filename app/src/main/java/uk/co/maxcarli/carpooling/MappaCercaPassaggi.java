package uk.co.maxcarli.carpooling;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class MappaCercaPassaggi extends AppCompatActivity  implements OnMapReadyCallback {


    private GoogleMap mMap;
    private Cittadino cittadino;
    private Passaggio passaggio;

    private String indirizzoCittadino;

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

        double raggio=500;
        home=getLocationFromAddress(cittadino.getResidenza(),this);
        getIndirizziPassaggiOfferti(this);

        work=getLocationFromAddress(cittadino.getSede().getIndirizzoSede(),this);


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



        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(home.getLatitude(),home.getLongitude()),18.0f));

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if(!marker.getTitle().equals(getString(R.string.la_tua_casa)) || !marker.getTitle().equals(getString(R.string.lavoro))){

                    String indirizzo=getAddressFromLatLng(marker.getPosition());
                    //Toast.makeText(this, indirizzo, Toast.LENGTH_LONG).show();
                    prenotaPassaggio(MappaCercaPassaggi.this, indirizzo);

                }

            }
        });

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


    public static Address getLocationFromAddress(String strAddress,Context c){

        Geocoder coder = new Geocoder(c);
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
                        Toast.makeText(context.getApplicationContext(),response,Toast.LENGTH_LONG).show();

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

                                    Address pos=getLocationFromAddress(indirizzo,context);

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
                            //Toast.makeText(context.getApplicationContext(),getString(R.string.OfferteNonPresenti),Toast.LENGTH_LONG).show();
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
                params.put("tipo",passaggio.getTipoPassaggio());
                params.put("nome",passaggio.getCittadinoOfferente().getNome());
                params.put("cognome",passaggio.getCittadinoOfferente().getCognome());
                params.put("sede",cittadino.getSede().getIdSede()+"");
                return params;
            }
        };

        MySingleton.getmInstance(context.getApplicationContext()).addTorequestque(stringRequest);
    }







    public void prenotaPassaggio(final Context context, final String indirizzo){
        String url = "http://carpoolingsms.altervista.org/PHP/ScriviPassaggioRichiesto.php";



        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                        if(!response.equals("Something went wrong") && !response.equals("Error query")){

                            try {

                                JSONArray jsonarray = new JSONArray(response);

                                for (int i = 0; i < jsonarray.length(); i++) {

                                    JSONObject jsonobject = jsonarray.getJSONObject(i);

                                    int  idPassaggio = jsonobject.getInt("IdPassaggio");
                                    String cognome = jsonobject.getString("CognomeCittadino");
                                    String cell = jsonobject.getString("TelefonoCittadino");
                                    String nome = jsonobject.getString("NomeCittadino");
                                    String residenza=jsonobject.getString("ResidenzaCittadino");
                                    Cittadino offerente=new Cittadino();
                                    offerente.setNome(nome);
                                    offerente.setCognome(cognome);
                                    offerente.setNumeroTelefono(cell);
                                    offerente.setResidenza(residenza);
                                    passaggio.setCittadinoOfferente(offerente);
                                    passaggio.setIdPassaggiOfferti(idPassaggio);
                                    passaggio.setStatus("Sospeso");

                                }
                            }catch(JSONException e){
                                e.printStackTrace();
                            }

                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle(R.string.PassaggioPrenotatoTitolo);
                            builder.setMessage(R.string.PassaggioPrenotatoTesto);
                            builder.setCancelable(false);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final Intent returnIntent = new Intent();
                                    returnIntent.putExtra(Passaggio.Keys.IDPASSAGGIO,passaggio);
                                    setResult(0,returnIntent);
                                    finish();
                                }
                            });
                            AlertDialog alertDialog= builder.create();
                            alertDialog.show();



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
