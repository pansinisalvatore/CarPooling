package uk.co.maxcarli.carpooling.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.ResourceBundle;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import uk.co.maxcarli.carpooling.Control.Controlli;
import uk.co.maxcarli.carpooling.MappaOffertePassaggiActivity;
import uk.co.maxcarli.carpooling.R;
import uk.co.maxcarli.carpooling.menu;
import uk.co.maxcarli.carpooling.model.Cittadino;
import uk.co.maxcarli.carpooling.model.Passaggio;
import uk.co.maxcarli.carpooling.model.Sede;


/**
 * Questo fragment visualizza la tabella dei passaggi offerti dal cittadino.
 */
public class PassaggiOfferti extends Fragment {

    private Cittadino cittadino;
    private menu menuActivity;

    private String[] PassaggiOffertiHeaders;
    String[][] passaggi;

    View view;
    TableView<String[]> tb;


    public PassaggiOfferti(){

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            cittadino=savedInstanceState.getParcelable(Cittadino.Keys.IDCITTADINO);
        }else{
            cittadino=menuActivity.getCittadino();
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_passaggi_offerti, container, false);

        tb=(TableView<String[]>) root.findViewById(R.id.TabellaPassaggiOfferti);

        tb.setColumnCount(4);
        tb.setColumnWeight(0,10);
        tb.setColumnWeight(1,13);
        tb.setColumnWeight(2,7);
        tb.setColumnWeight(3,10);

        tb.setHeaderBackgroundColor(Color.parseColor("#2ecc71"));

        PassaggiOffertiHeaders=new String[4];
        PassaggiOffertiHeaders[0]=getString(R.string.viaggio);
        PassaggiOffertiHeaders[1]=getString(R.string.data_e_ora);
        PassaggiOffertiHeaders[2]=getString(R.string.Stato);
        PassaggiOffertiHeaders[3]=getString(R.string.occupati);

        SimpleTableHeaderAdapter headerAdapter=new SimpleTableHeaderAdapter(getActivity(),PassaggiOffertiHeaders);

        headerAdapter.setTextSize(15);

        tb.setHeaderAdapter(headerAdapter);


        populateData(cittadino.passaggiOfferti);

        SimpleTableDataAdapter dataAdapter=new SimpleTableDataAdapter(getActivity(),passaggi);


        dataAdapter.setTextSize(10);


        tb.setDataAdapter(dataAdapter);

        tb.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        tb.addDataClickListener(new TableDataClickListener<String[]>() {
            @Override
            public void onDataClicked(int rowIndex, String[] clickedData) {
                if(!clickedData[2].equals(getContext().getString(R.string.completato))){
                    Intent intent=new Intent(getActivity(), MappaOffertePassaggiActivity.class);
                    intent.putExtra(Cittadino.Keys.IDCITTADINO,cittadino);

                    intent.putExtra(Passaggio.Keys.IDPASSAGGIO, cittadino.passaggiOfferti.get(rowIndex));

                    startActivityForResult(intent,rowIndex);
                }

            }
        });


        return root;
    }


    /**
     *
     * @param passaggiRichiesti
     * Popola un array si stringhe che servirà all'adapter per popolare la tabella.
     */
    public void populateData(List<Passaggio> passaggiRichiesti){

        this.passaggi=new String[passaggiRichiesti.size()][4];


        for(int i=0;i<passaggiRichiesti.size();i++){

            Passaggio p= passaggiRichiesti.get(i);

            this.passaggi[i][0]=controlloTipoPassagio(p.getTipoPassaggio(),getContext());
            this.passaggi[i][1]=p.getData()+" "+p.getOra();
            if(p.getRichieste()>=0){
                this.passaggi[i][2]=p.getRichieste()+" msg";
            }else{
                this.passaggi[i][2]= Controlli.controllaStringaStatus("completato",getContext());
            }


            this.passaggi[i][3]=p.getPostiOccupati()+"";

        }
    }


    /**
     *
     * @param tipo
     * @param context
     * Controlla il tipo di passaggio e restituisce la stringa corrispondente contenuta nelle risorse string.xml.
     * @return
     */

    public static String controlloTipoPassagio(String tipo, Context context){
        if(tipo.equals("Casa-Lavoro")){
            return context.getString(R.string.casa_lavoro);
        }else{
            return context.getString(R.string.lavoro_casa);
        }
    }


    public void onAttach(Activity activity) {

        super.onAttach(activity);
        menuActivity=(menu)activity;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent databack){
        if(databack!=null){

            cittadino=(Cittadino)databack.getParcelableExtra(Cittadino.Keys.IDCITTADINO);
            menuActivity.setCittadino(cittadino);
        }

        /*Passaggio passaggioSelezionato=null;
        if(databack!=null){
            passaggioSelezionato=(Passaggio)databack.getParcelableExtra(Passaggio.Keys.IDPASSAGGIO);
        }


        if(passaggioSelezionato!=null){
            cittadino.passaggiOfferti.set(requestCode, passaggioSelezionato);


            menuActivity.setCittadino(cittadino);
        }*/
    }

    public void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        bundle.putParcelable(Cittadino.Keys.IDCITTADINO,cittadino);
    }


}
