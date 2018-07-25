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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
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

import uk.co.maxcarli.carpooling.Control.ControlBluetooth;
import uk.co.maxcarli.carpooling.Control.Controlli;
import uk.co.maxcarli.carpooling.model.Cittadino;
import uk.co.maxcarli.carpooling.model.Passaggio;

public class MappaCercaPassaggi extends AppCompatActivity  implements OnMapReadyCallback {


    private GoogleMap mMap;
    private Cittadino cittadino;
    private Passaggio passaggio;


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

        mMap.setInfoWindowAdapter(new CustomMarkerOfferenti(this));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(home.getLatitude(),home.getLongitude()),18.0f));

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if(!marker.getTitle().equals(getString(R.string.la_tua_casa)) || !marker.getTitle().equals(getString(R.string.lavoro))){

                    String indirizzo=getAddressFromLatLng(marker.getPosition());
                    String totOfferente=marker.getTitle();
                    String nome=totOfferente.substring(totOfferente.indexOf(" "));
                    String cognome=totOfferente.substring(0, totOfferente.indexOf(" "));
                    nome=nome.trim();
                    cognome=cognome.trim();
                    String telefono= marker.getSnippet().substring(0,marker.getSnippet().indexOf("-"));


                    Cittadino offerente=new Cittadino();
                    offerente.setNome(nome);
                    offerente.setCognome(cognome);
                    offerente.setResidenza(indirizzo);
                    offerente.setNumeroTelefono(telefono);


                    prenotaPassaggio(MappaCercaPassaggi.this, Integer.parseInt((String)marker.getTag()), offerente);

                }

            }
        });

    }

    public boolean controlloArcoOrario(String orarioRicercato, String orarioPresente){
        if(Math.abs(Controlli.oreInMinuti(orarioRicercato)-Controlli.oreInMinuti(orarioPresente))<=15){
            return true;
        }else{
            return false;
        }
    }


    public boolean controllo(Address centro, Address pos, double raggio){


        float[] results=new float[1];
        Location.distanceBetween(centro.getLatitude(),centro.getLongitude(),pos.getLatitude(),pos.getLongitude(),results);
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


                        if(!response.equals("Something went wrong")){
                            try {

                                JSONArray jsonarray = new JSONArray(response);

                                for (int i = 0; i < jsonarray.length(); i++) {

                                    JSONObject jsonobject = jsonarray.getJSONObject(i);

                                    int idPassaggio=jsonobject.getInt("IdPassaggio");
                                    String nome=jsonobject.getString("NomeCittadino");
                                    String cognome=jsonobject.getString("CognomeCittadino");
                                    String indirizzo = jsonobject.getString("ResidenzaCittadino");
                                    String cell=jsonobject.getString("TelefonoCittadino");
                                    String ora=jsonobject.getString("OraPassaggio");
                                    int j=0;

                                    Address pos=getLocationFromAddress(indirizzo,context);

                                    if(controllo(home, pos, 500) && controlloArcoOrario(passaggio.getOra(),ora)){
                                        j++;
                                        mMap.addMarker(new MarkerOptions().
                                                position(new LatLng(pos.getLatitude(),pos.getLongitude())
                                                ).title(cognome+" "+nome).snippet(cell+"-"+idPassaggio).
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
                params.put("tipo",passaggio.getTipoPassaggio());
                params.put("nome",passaggio.getCittadinoOfferente().getNome());
                params.put("cognome",passaggio.getCittadinoOfferente().getCognome());
                params.put("sede",cittadino.getSede().getIdSede()+"");
                return params;
            }
        };

        MySingleton.getmInstance(context.getApplicationContext()).addTorequestque(stringRequest);
    }







    public void prenotaPassaggio(final Context context, final int idPassaggio, final Cittadino offerente){
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

                                    String dataPassaggio=jsonobject.getString("DataPassaggio");
                                    String oraPassaggio=jsonobject.getString("OraPassaggio");
                                    passaggio.setData(dataPassaggio);
                                    passaggio.setOra(oraPassaggio);
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

                params.put("idCittadino",cittadino.getIdCittadino()+"");
                params.put("idPassaggio",idPassaggio+"");
                params.put("macAddress", ControlBluetooth.getBluetoothMacAddress());
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

               // Toast.makeText(this, address, Toast.LENGTH_LONG).show();
               return address;
           }
           return null;

       }catch(IOException e){
            return null;
       }

    }


    private class CustomMarkerOfferenti implements GoogleMap.InfoWindowAdapter{

        Context c;
        View view;

        TextView driver;
        TextView residence;
        TextView cell;


        public CustomMarkerOfferenti(Context c){
            this.c=c;
        }
        @Override
        public View getInfoWindow(Marker marker) {
            if(marker.getTitle().equals(c.getString(R.string.la_tua_casa)) || marker.getTitle().equals(c.getString(R.string.lavoro))){
                return null;
            }
            view=LayoutInflater.from(c).inflate(R.layout.marker_cerca_passaggi,null);
            driver=view.findViewById(R.id.textDriver);
            residence=view.findViewById(R.id.textResidence);
            cell=view.findViewById(R.id.textCell);

            String autista=marker.getTitle();

            driver.setText(autista);

            String indirizzo=getAddressFromLatLng(marker.getPosition());
            residence.setText(indirizzo);

            String telefono=marker.getSnippet().substring(0,marker.getSnippet().indexOf("-"));
            cell.setText(telefono);

            String idPassaggio=marker.getSnippet().substring(marker.getSnippet().indexOf("-"));
            idPassaggio=idPassaggio.substring(1);
            marker.setTag(idPassaggio);

            return view;
        }

        @Override
        public View getInfoContents(Marker marker) {
            if(marker.getTitle().equals(c.getString(R.string.la_tua_casa)) || marker.getTitle().equals(c.getString(R.string.lavoro))){
                return null;
            }
            view=LayoutInflater.from(c).inflate(R.layout.marker_cerca_passaggi,null);
            driver=view.findViewById(R.id.textDriver);
            residence=view.findViewById(R.id.textResidence);
            cell=view.findViewById(R.id.textCell);

            String autista=marker.getTitle();

            driver.setText(autista);

            String indirizzo=getAddressFromLatLng(marker.getPosition());
            residence.setText(indirizzo);

            String telefono=marker.getSnippet().substring(0,marker.getSnippet().indexOf("-"));
            cell.setText(telefono);

            String idPassaggio=marker.getSnippet().substring(marker.getSnippet().indexOf("-"));
            idPassaggio=idPassaggio.substring(1);
            marker.setTag(idPassaggio);

            return view;
        }
    }
}
