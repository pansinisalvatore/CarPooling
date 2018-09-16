package uk.co.maxcarli.carpooling;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uk.co.maxcarli.carpooling.Control.Controlli;
import uk.co.maxcarli.carpooling.model.Cittadino;
import uk.co.maxcarli.carpooling.model.Passaggio;

/**
 * Questa activity visualizza la mappa con tutte le richieste ricevute da altri utenti, permettendo di accettare o rfiutare una richiesta.
 */
public class MappaOffertePassaggiActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;

    Button accetta;
    Button rifiuta;


    private Cittadino cittadino;
    private Passaggio passaggio;
    private Address indirizzoCasa;
    private Address indirizzoLavoro;
    private CustomInfoWindow infoMarker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mappa_offerte_passaggi);
        passaggio=getIntent().getParcelableExtra(Passaggio.Keys.IDPASSAGGIO);
        cittadino=getIntent().getParcelableExtra(Cittadino.Keys.IDCITTADINO);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapOfferte);
        mapFragment.getMapAsync(this);

        accetta=findViewById(R.id.accetta);
        rifiuta=findViewById(R.id.rifiuta);
        accetta.setVisibility(View.INVISIBLE);
        rifiuta.setVisibility(View.INVISIBLE);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        accetta.setVisibility(View.INVISIBLE);
        rifiuta.setVisibility(View.INVISIBLE);
        return false;
    }


    /**
     * Viene creata la mappa con i marcatori che identificano la posizione dei richiedenti, quella dell'offerente e quella del luogo di lavoro
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        infoMarker=new CustomInfoWindow(this);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                accetta.setVisibility(View.INVISIBLE);
                rifiuta.setVisibility(View.INVISIBLE);
            }
        });
        mMap.setInfoWindowAdapter(infoMarker);
        double raggio=500;
        indirizzoCasa=MappaCercaPassaggi.getLocationFromAddress(cittadino.getResidenza(),this);
        indirizzoLavoro=MappaCercaPassaggi.getLocationFromAddress(cittadino.getSede().getIndirizzoSede(),this);
        getIndirizziRichiedenti();
        mMap.setOnInfoWindowClickListener(this);


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

    /**
     * Vengono presi gli indirizzi dei richiedenti utilizzando i dati gia presi precedentemente dal database
     */
    private void getIndirizziRichiedenti(){


      ArrayList<Cittadino> richiedenti=passaggio.cittadiniRichiedenti;

      for(int i=0;i<richiedenti.size();i++){
          Cittadino c=richiedenti.get(i);
          String indirizzo = c.getResidenza();
          String cell=c.getNumeroTelefono();
          Address pos = MappaCercaPassaggi.getLocationFromAddress(indirizzo, this);
          if(passaggio.cittadinoStatus.get(i).equals("sospeso") || passaggio.cittadinoStatus.get(i).equals("accettato")){
              mMap.addMarker(new MarkerOptions().
                      position(new LatLng(pos.getLatitude(), pos.getLongitude())
                      ).title(cell).snippet(Controlli.controllaStringaStatus(passaggio.cittadinoStatus.get(i), MappaOffertePassaggiActivity.this)).
                      icon(BitmapDescriptorFactory
                              .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
          }

      }

    }


    /**
     * La funzione implementa il click della finestra del marcatore.Se lo stato è "sospeso" vengono poi visualizzati due bottoni
     * "accetta" e "rifiuta" che, se cliccati, cambiano lo stato del richiedente.
     * @param marker
     */

    @Override
    public void onInfoWindowClick(final Marker marker) {
        if(marker.getSnippet().equals(getString(R.string.Sospeso))){
            accetta.setVisibility(View.VISIBLE);
            rifiuta.setVisibility(View.VISIBLE);

            accetta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(passaggio.getPostiOccupati()<passaggio.getPostiDisponibili()){
                        String cell=marker.getTitle();
                        for(int i=0;i<passaggio.cittadiniRichiedenti.size();i++){
                            if(cell.equals(passaggio.cittadiniRichiedenti.get(i).getNumeroTelefono())){
                                //passaggio.setPostiOccupati(passaggio.getPostiOccupati()+1);
                                //passaggio.setRichieste(passaggio.getRichieste()-1);

                                passaggio.cittadinoStatus.set(i,"accettato");
                                if(passaggio.getPostiDisponibili()==passaggio.getPostiOccupati()){
                                    Controlli.mostraMessaggioSuccesso(getString(R.string.PostiOccupatiTitolo),getString(R.string.PostiOccupatiTesto),MappaOffertePassaggiActivity.this);
                                    for(int j=0;j<passaggio.cittadiniRichiedenti.size();j++){


                                        if(passaggio.cittadinoStatus.get(j).equals("sospeso")){
                                            String cellRichiedenteSospeso=passaggio.cittadiniRichiedenti.get(j).getNumeroTelefono();
                                            Database.modificaStatus("rifiutato",cellRichiedenteSospeso,MappaOffertePassaggiActivity.this,passaggio );
                                            //passaggio.cittadinoStatus.remove(j);
                                            //passaggio.cittadiniRichiedenti.remove(j);
                                        }
                                    }

                                }else{
                                    Controlli.mostraMessaggioSuccesso(getString(R.string.PassaggioAccettatoSuccessoTitolo),getString(R.string.PassaggioAccettatoSuccessoTesto),MappaOffertePassaggiActivity.this);
                                }


                                marker.setSnippet(Controlli.controllaStringaStatus("accettato",MappaOffertePassaggiActivity.this));
                                mMap.setInfoWindowAdapter(new CustomInfoWindow(MappaOffertePassaggiActivity.this));
                                break;
                            }
                        }
                        Database.modificaStatus("accettato",cell,MappaOffertePassaggiActivity.this,passaggio);

                    }else{
                        Controlli.mostraMessaggioErrore(getString(R.string.PostiOccupatiTitolo),getString(R.string.PostiOccupatiTesto),MappaOffertePassaggiActivity.this);


                    }

                    accetta.setVisibility(View.INVISIBLE);
                    rifiuta.setVisibility(View.INVISIBLE);


                }
            });

            rifiuta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String cell=marker.getTitle();
                    for(int i=0;i<passaggio.cittadiniRichiedenti.size();i++){
                        if(cell.equals(passaggio.cittadiniRichiedenti.get(i).getNumeroTelefono())){
                            //passaggio.cittadinoStatus.remove(i);
                            //passaggio.cittadiniRichiedenti.remove(i);

                            marker.remove();

                            break;
                        }
                    }
                    Controlli.mostraMessaggioSuccesso(getString(R.string.PassaggioRifiutatoSuccessoTitolo),getString(R.string.PassaggioRifiutatoSuccessoTesto),MappaOffertePassaggiActivity.this);
                    Database.modificaStatus("rifiutato",cell,MappaOffertePassaggiActivity.this,passaggio);
                    accetta.setVisibility(View.INVISIBLE);
                    rifiuta.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    /**
     * Questa classe implementa la finestra che viene visualizzata al click di ogni marcatore
     */
    public  class CustomInfoWindow implements GoogleMap.InfoWindowAdapter{

        private final View view;
        private final Context context;
        private String cognomeDriver;
        private  String nomeDriver;
        private String residence;
        private  String cell;


        TextView drivertext;
        TextView residencetext;
        TextView celltext;
        TextView statustext;


        public CustomInfoWindow(Context context){
            this.context=context;
            view= LayoutInflater.from(context).inflate(R.layout.marker_offerte_passaggi,null);

        }




        @Override
        public View getInfoWindow(Marker marker) {


            if(marker.getTitle().equals(getString(R.string.la_tua_casa)) || marker.getTitle().equals(getString(R.string.lavoro))){
                return null;
            }
            ArrayList<Cittadino> richiedenti=passaggio.cittadiniRichiedenti;
            for(int i=0;i<passaggio.cittadiniRichiedenti.size();i++){
                if(marker.getTitle().equals(richiedenti.get(i).getNumeroTelefono())){
                    drivertext=view.findViewById(R.id.textDriver);
                    drivertext.setText(richiedenti.get(i).getCognome()+" "+richiedenti.get(i).getNome());
                    residencetext=view.findViewById(R.id.textResidence);
                    residencetext.setText(richiedenti.get(i).getResidenza());
                    celltext=view.findViewById(R.id.textCell);
                    celltext.setText(marker.getTitle());
                    statustext=view.findViewById(R.id.textStatus);
                    statustext.setText(marker.getSnippet());
                }
            }





            return view;
        }

        @Override
        public View getInfoContents(Marker marker) {
            if(marker.getTitle().equals(getString(R.string.la_tua_casa)) || marker.getTitle().equals(R.string.lavoro)){
                return null;
            }
            ArrayList<Cittadino> richiedenti=passaggio.cittadiniRichiedenti;
            for(int i=0;i<passaggio.cittadiniRichiedenti.size();i++){
                if(marker.getTitle().equals(richiedenti.get(i).getNumeroTelefono())){
                    drivertext=view.findViewById(R.id.textDriver);
                    drivertext.setText(richiedenti.get(i).getCognome()+" "+richiedenti.get(i).getNome());
                    residencetext=view.findViewById(R.id.textResidence);
                    residencetext.setText(richiedenti.get(i).getResidenza());
                    celltext=view.findViewById(R.id.textCell);
                    celltext.setText(marker.getTitle());
                    statustext=view.findViewById(R.id.textStatus);
                    statustext.setText(marker.getSnippet());
                }
            }

            return view;
        }



    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
         cittadino.passaggiOfferti.clear();
         cittadino.passaggiRichiesti.clear();

         Database.getPassaggiRichiestiFromCittadino(cittadino,this,Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //returnIntent.putExtra(Passaggio.Keys.IDPASSAGGIO,passaggio);
        //setResult(cittadino.passaggiOfferti.indexOf(passaggio),returnIntent);
    }


}
