package uk.co.maxcarli.carpooling;

import android.location.Address;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import uk.co.maxcarli.carpooling.Control.Controlli;
import uk.co.maxcarli.carpooling.model.Cittadino;
import uk.co.maxcarli.carpooling.model.Passaggio;

public class MappaPassaggiRichiesti extends AppCompatActivity  implements OnMapReadyCallback,GoogleMap.InfoWindowAdapter {

    private GoogleMap mMap;
    private Passaggio p;
    private Cittadino c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mappa_cerca_passaggi);

        if(savedInstanceState==null){
            p=getIntent().getParcelableExtra(Passaggio.Keys.IDPASSAGGIO);
            c=getIntent().getParcelableExtra(Cittadino.Keys.IDCITTADINO);
        }else{
            p=savedInstanceState.getParcelable(Passaggio.Keys.IDPASSAGGIO);
            c=savedInstanceState.getParcelable(Cittadino.Keys.IDCITTADINO);
        }

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;

        Address casa= MappaCercaPassaggi.getLocationFromAddress(c.getResidenza(),this);
        Address lavoro=MappaCercaPassaggi.getLocationFromAddress(c.getSede().getIndirizzoSede(),this);
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(casa.getLatitude(),casa.getLongitude()))
                .title(getString(R.string.la_tua_casa))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lavoro.getLatitude(),lavoro.getLongitude()))
                .title(getString(R.string.lavoro))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        Address offerente=MappaCercaPassaggi.getLocationFromAddress(p.getCittadinoOfferente().getResidenza(),this);
        mMap.addMarker(new MarkerOptions().
                position(new LatLng(offerente.getLatitude(), offerente.getLongitude())
                ).title("offers").
                icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        mMap.setInfoWindowAdapter(this);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(casa.getLatitude(),casa.getLongitude()),18.0f));
    }



    @Override
    public View getInfoWindow(Marker marker) {
        if(marker.getTitle().equals(getString(R.string.la_tua_casa))|| marker.getTitle().equals(getString(R.string.lavoro))){
            return null;
        }
        View view= view= LayoutInflater.from(this).inflate(R.layout.marker_offerte_passaggi,null);
        TextView drivertext=view.findViewById(R.id.textDriver);
        drivertext.setText(p.getCittadinoOfferente().toString());
        TextView residencetext=view.findViewById(R.id.textResidence);
        residencetext.setText(p.getCittadinoOfferente().getResidenza());
        TextView celltext=view.findViewById(R.id.textCell);
        celltext.setText(p.getCittadinoOfferente().getNumeroTelefono());
        TextView statustext=view.findViewById(R.id.textStatus);
        statustext.setText(Controlli.controllaStringaStatus(p.getStatus(),this));

        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        if(marker.getTitle().equals(getString(R.string.la_tua_casa))|| marker.getTitle().equals(getString(R.string.lavoro))){
            return null;
        }
        View view= view= LayoutInflater.from(this).inflate(R.layout.marker_offerte_passaggi,null);
        TextView drivertext=view.findViewById(R.id.textDriver);
        drivertext.setText(p.getCittadinoOfferente().toString());
        TextView residencetext=view.findViewById(R.id.textResidence);
        residencetext.setText(p.getCittadinoOfferente().getResidenza());
        TextView celltext=view.findViewById(R.id.textCell);
        celltext.setText(p.getCittadinoOfferente().getNumeroTelefono());
        TextView statustext=view.findViewById(R.id.textStatus);
        statustext.setText(Controlli.controllaStringaStatus(p.getStatus(),this));
        return view;
    }
}
