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

public class RicercaIndirizzo extends AppCompatActivity {


    static final private String TAG = "ERRORE";
    private Cittadino cittadino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ricerca_indirizzo);
        final Intent intent = getIntent();
        cittadino=(Cittadino) intent.getSerializableExtra("cittadino");

        dammiPosto(cittadino.getNome(),cittadino.getCognome(),cittadino.getCodiceFiscale());

    }

    public void dammiPosto (final String nome, final String cognome, final String codiceFiscale){

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
                        inviaIndirizzo(nome,cognome, codiceFiscale, residenza);
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

    public void inviaIndirizzo(String nome,String cognome, String codiceFiscale, String residenza){
        final Intent intent = new Intent(this, RegistrazioneCittadino.class);
        intent.putExtra("nome",nome);
        intent.putExtra("cognome",cognome);
        intent.putExtra("codiceFiscale",codiceFiscale);
        intent.putExtra("residenza",residenza);
        startActivity(intent);
    }

}
