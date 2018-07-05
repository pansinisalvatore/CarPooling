package uk.co.maxcarli.carpooling.Fragment;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import uk.co.maxcarli.carpooling.R;
import uk.co.maxcarli.carpooling.model.Cittadino;
import uk.co.maxcarli.carpooling.model.Passaggio;
import uk.co.maxcarli.carpooling.menu;

/**
 * A simple {@link Fragment} subclass.
 */
public class PassaggiRichiesti extends Fragment {






    private menu menuActivity;
    private Cittadino cittadino;
   /* private  final String VIAGGIO=menuActivity.getString(R.string.viaggio);
    private  final String DATAEORA=menuActivity.getString(R.string.data_e_ora);
    private final String STATO=menuActivity.getString(R.string.Stato);
    private final String TELEFONO=menuActivity.getString(R.string.Telefono);
    private final String AUTISTA= menuActivity.getString(R.string.automobilista);
    private final String AUTO=menuActivity.getString(R.string.Auto);*/

    private String[] PassaggiRichiestiHeaders={"Trip","Date&Hour","Status","Phone","Driver","Car"};
    String[][] passaggi;

    View view;
    TableView<String[]> tb;

    public PassaggiRichiesti() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cittadino=menuActivity.getCittadino();
        String v = "";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_passaggi_richiesti,null);

        tb=(TableView<String[]>) view.findViewById(R.id.TabellaPassaggiRichiesti);


        tb.setColumnCount(6);
        tb.setHeaderBackgroundColor(Color.parseColor("#2ecc71"));
        tb.setHeaderAdapter(new SimpleTableHeaderAdapter(getActivity(), PassaggiRichiestiHeaders));

        populateData(cittadino.passaggiRichiesti);

        tb.setDataAdapter(new SimpleTableDataAdapter(getActivity(), passaggi));





        // Inflate the layout for this fragment
        return view;


    }


   public void populateData(List<Passaggio> passaggiRichiesti){

        this.passaggi=new String[passaggiRichiesti.size()][6];


        for(int i=0;i<passaggiRichiesti.size();i++){

            Passaggio p= passaggiRichiesti.get(i);

            this.passaggi[i][0]=p.getTipoPassaggio();
            this.passaggi[i][1]=p.getData()+" "+p.getOra();
            this.passaggi[i][2]=p.getStatus();
            this.passaggi[i][3]=p.getCellAutomobilista();
            this.passaggi[i][4]=p.getAutomobilista();
            this.passaggi[i][5]=p.getAuto();

        }
    }



  public void onAttach(Activity activity) {

      super.onAttach(activity);
      menuActivity=(menu)activity;
  }



}
