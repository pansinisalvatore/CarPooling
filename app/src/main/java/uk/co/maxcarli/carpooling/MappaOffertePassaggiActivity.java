package uk.co.maxcarli.carpooling;

import android.content.Context;
import android.location.Address;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Map;

import uk.co.maxcarli.carpooling.model.Cittadino;
import uk.co.maxcarli.carpooling.model.Passaggio;
import uk.co.maxcarli.carpooling.model.Sede;

public class MappaOffertePassaggiActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Address indirizzoCasa;
    private Address indirizzoLavoro;
    private String data;
    private String ora;
    private String casa;
    private String lavoro;
    private int IdCittadino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mappa_offerte_passaggi);
        casa=getIntent().getStringExtra(Cittadino.Keys.RESIDENZA);
        lavoro=getIntent().getStringExtra(Sede.Keys.INDIRIZZO);
        data=getIntent().getStringExtra(Passaggio.Keys.DATA);
        ora=getIntent().getStringExtra(Passaggio.Keys.ORA);
        IdCittadino=getIntent().getIntExtra(Cittadino.Keys.IDCITTADINO,0);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapOfferte);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);


        double raggio=500;
        indirizzoCasa=MappaCercaPassaggi.getLocationFromAddress(casa,this);
        indirizzoLavoro=MappaCercaPassaggi.getLocationFromAddress(lavoro,this);
        getIndirizziRichiedenti(this);




        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(indirizzoCasa.getLatitude(),indirizzoCasa.getLongitude()))
                .title(getString(R.string.la_tua_casa))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(indirizzoLavoro.getLatitude(),indirizzoLavoro.getLongitude()))
                .title(getString(R.string.lavoro))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(indirizzoCasa.getLatitude(),indirizzoCasa.getLongitude()),18.0f));
    }

    private void getIndirizziRichiedenti(final Context context){
        String url = "http://carpoolingsms.altervista.org/PHP/PrendiIndirizziDeiRichiedenti.php";


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
                                    String cognome = jsonobject.getString("CognomeCittadino");
                                    String nome = jsonobject.getString("NomeCittadino");
                                    int j = 0;

                                    Address pos = MappaCercaPassaggi.getLocationFromAddress(indirizzo, context);
                                    mMap.addMarker(new MarkerOptions().
                                            position(new LatLng(pos.getLatitude(), pos.getLongitude())
                                            ).title(cognome + " " + nome).
                                            icon(BitmapDescriptorFactory
                                                    .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();

                            }
                        }else{
                            //Toast.makeText(context.getApplicationContext(),getString(R.string.RichiesteNonPresenti),Toast.LENGTH_LONG).show();
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
                params.put("data", data);
                params.put("ora", ora);
                params.put("idCittadino",IdCittadino+"");
                return params;
            }
        };

        MySingleton.getmInstance(context.getApplicationContext()).addTorequestque(stringRequest);
    }
}
