package uk.co.maxcarli.carpooling.Fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import uk.co.maxcarli.carpooling.Control.Controlli;
import uk.co.maxcarli.carpooling.Database;
import uk.co.maxcarli.carpooling.MappaPassaggiRichiesti;
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

    private String[] PassaggiRichiestiHeaders={"Trip","Date&Hour","Status","Driver"};
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


        tb.setColumnCount(4);

        tb.setColumnWeight(0,7);
        tb.setColumnWeight(1,10);
        tb.setColumnWeight(2,7);
        tb.setColumnWeight(3,10);
        tb.setHeaderBackgroundColor(Color.parseColor("#2ecc71"));

        SimpleTableHeaderAdapter headerAdapter=new SimpleTableHeaderAdapter(getActivity(),PassaggiRichiestiHeaders);

        headerAdapter.setTextSize(15);


        tb.setHeaderAdapter(headerAdapter);

        populateData(cittadino.passaggiRichiesti);

        SimpleTableDataAdapter dataAdapter=new SimpleTableDataAdapter(getActivity(),passaggi);


        dataAdapter.setTextSize(10);


        tb.setDataAdapter(dataAdapter);


        tb.addDataClickListener(new TableDataClickListener<String[]>() {
            @Override
            public void onDataClicked(final int rowIndex, final String[] clickedData) {
                String status=clickedData[2];
                if(status.equals(getContext().getString(R.string.Rifiutato))){
                   finestraEliminazione(rowIndex,getContext().getString(R.string.PassaggioRifiutatoTitolo),getContext().getString(R.string.PassaggioRifiutatoTesto));
                }else{
                    Intent intent=new Intent(menuActivity, MappaPassaggiRichiesti.class);
                    intent.putExtra(Passaggio.Keys.IDPASSAGGIO,cittadino.passaggiRichiesti.get(rowIndex));
                    intent.putExtra(Cittadino.Keys.IDCITTADINO,cittadino);
                    menuActivity.startActivity(intent);
                }

            }
        });



        // Inflate the layout for this fragment
        return view;


    }


    public void finestraEliminazione(final int rowIndex,String title, String text){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setMessage(text);
        builder.setPositiveButton(getContext().getString(R.string.si), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Database.modificaStatus("eliminato",cittadino.getNumeroTelefono(),getContext(),cittadino.passaggiRichiesti.get(rowIndex));
                cittadino.passaggiRichiesti.remove(rowIndex);
                populateData(cittadino.passaggiRichiesti);
                SimpleTableDataAdapter dataAdapter=new SimpleTableDataAdapter(getActivity(),passaggi);
                tb.setDataAdapter(dataAdapter);
                menuActivity.setCittadino(cittadino);

            }
        });
        builder.setNegativeButton(getContext().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog= builder.create();
        alertDialog.show();
    }


   public void populateData(List<Passaggio> passaggiRichiesti){

        this.passaggi=new String[passaggiRichiesti.size()][4];


        for(int i=0;i<passaggiRichiesti.size();i++){

            Passaggio p= passaggiRichiesti.get(i);

            this.passaggi[i][0]=PassaggiOfferti.controlloTipoPassagio(p.getTipoPassaggio(),getContext());
            this.passaggi[i][1]=p.getData()+" "+p.getOra();
            this.passaggi[i][2]= Controlli.controllaStringaStatus(p.getStatus(), getContext());
            this.passaggi[i][3]=p.getCittadinoOfferente().toString();

        }
    }



  public void onAttach(Activity activity) {

      super.onAttach(activity);
      menuActivity=(menu)activity;
  }



}
