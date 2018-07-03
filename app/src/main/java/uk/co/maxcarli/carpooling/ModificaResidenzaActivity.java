package uk.co.maxcarli.carpooling;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import uk.co.maxcarli.carpooling.model.Cittadino;

public class ModificaResidenzaActivity extends AppCompatActivity {


    static final private String TAG = "ERRORE";
    private String residenza;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_residenza);
        final Intent intent = getIntent();
        residenza=intent.getStringExtra(Cittadino.Keys.RESIDENZA);

        dammiPosto();

    }

    public void dammiPosto (){

        final PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .setCountry("IT")
                .build();

        autocompleteFragment.setFilter(typeFilter);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.

                final String residenza = (String) place.getAddress();
                findViewById(R.id.buttonConferma).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        inviaIndirizzo(residenza);
                        finish();



                    }
                });


                Log.i(TAG, "Place: " + residenza);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

/*

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE)
                .build();

        autocompleteFragment.setFilter(typeFilter);
        */


    }

    public void inviaIndirizzo( String residenza){
        final Intent returnIntent = new Intent();
        returnIntent.putExtra(Cittadino.Keys.RESIDENZA,residenza);
        setResult(0,returnIntent);
        finish();
    }


}
