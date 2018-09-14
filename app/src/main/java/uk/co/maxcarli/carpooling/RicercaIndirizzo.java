package uk.co.maxcarli.carpooling;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePredictionBufferResponse;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.tasks.Task;

import uk.co.maxcarli.carpooling.model.Cittadino;

/**
 * Questa classe serve per trovare una qualsiasi via italiana.
 */
public class RicercaIndirizzo extends AppCompatActivity {


    static final private String TAG = "ERRORE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ricerca_indirizzo);

        dammiPosto();

    }

    /**
     * Questa api di google serve per autocompletare una editText con le vie italiane
     */
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

    }

    /**
     * Invia l'intent all'activity precedente
     * @param residenza
     */
    public void inviaIndirizzo(String residenza){
        final Intent returnIntent = new Intent();
        returnIntent.putExtra(Cittadino.Keys.RESIDENZA,residenza);
        setResult(0,returnIntent);
        finish();
    }

}
