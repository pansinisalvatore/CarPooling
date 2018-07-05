package uk.co.maxcarli.carpooling.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
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

    private String[] PassaggiOffertiHeaders={"Trip","Date&Hour","Status","Occupied places"};
    String[][] passaggi;

    View view;
    TableView<String[]> tb;


    public PassaggiOfferti(){

    }

    @SuppressLint("ValidFragment")
    public PassaggiOfferti(Cittadino cittadino) {
        this.cittadino=cittadino;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cittadino=menuActivity.getCittadino();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_passaggi_offerti, container, false);

        tb=(TableView<String[]>) root.findViewById(R.id.TabellaPassaggiOfferti);


        tb.setColumnCount(4);
        tb.setHeaderBackgroundColor(Color.parseColor("#2ecc71"));
        tb.setHeaderAdapter(new SimpleTableHeaderAdapter(getActivity(), PassaggiOffertiHeaders));

        populateData(cittadino.passaggiOfferti);

        tb.setDataAdapter(new SimpleTableDataAdapter(getActivity(), passaggi));



        return root;
    }


    public void populateData(List<Passaggio> passaggiRichiesti){

        this.passaggi=new String[passaggiRichiesti.size()][4];


        for(int i=0;i<passaggiRichiesti.size();i++){

            Passaggio p= passaggiRichiesti.get(i);

            this.passaggi[i][0]=p.getTipoPassaggio();
            this.passaggi[i][1]=p.getData()+" "+p.getOra();
            this.passaggi[i][2]=p.getRichieste()+" msg in sospeso";
            this.passaggi[i][3]=p.getPostiOccupati()+"";

        }
    }


    public void onAttach(Activity activity) {

        super.onAttach(activity);
        menuActivity=(menu)activity;
    }
}
