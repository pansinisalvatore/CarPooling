package uk.co.maxcarli.carpooling.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import uk.co.maxcarli.carpooling.R;
import uk.co.maxcarli.carpooling.menu;
import uk.co.maxcarli.carpooling.model.Cittadino;
import uk.co.maxcarli.carpooling.model.Passaggio;


public class PassaggiOfferti extends Fragment {

    private Cittadino cittadino;
    private menu menuActivity;

    private String[] PassaggiOffertiHeaders={"Trip","Date&Hour","Status","Occupied"};
    String[][] passaggi;

    View view;
    TableView<String[]> tb;


    public PassaggiOfferti(){

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cittadino=menuActivity.getCittadino();
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

        SimpleTableHeaderAdapter headerAdapter=new SimpleTableHeaderAdapter(getActivity(),PassaggiOffertiHeaders);

        headerAdapter.setTextSize(15);

        tb.setHeaderAdapter(headerAdapter);


        populateData(cittadino.passaggiOfferti);

        SimpleTableDataAdapter dataAdapter=new SimpleTableDataAdapter(getActivity(),passaggi);


        dataAdapter.setTextSize(10);


        tb.setDataAdapter(dataAdapter);

        tb.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);


        return root;
    }


    public void populateData(List<Passaggio> passaggiRichiesti){

        this.passaggi=new String[passaggiRichiesti.size()][4];


        for(int i=0;i<passaggiRichiesti.size();i++){

            Passaggio p= passaggiRichiesti.get(i);

            this.passaggi[i][0]=controlloTipoPassagio(p.getTipoPassaggio(),getContext());
            this.passaggi[i][1]=p.getData()+" "+p.getOra();
            this.passaggi[i][2]=p.getRichieste()+" msg";
            this.passaggi[i][3]=p.getPostiOccupati()+"";

        }
    }


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
}
