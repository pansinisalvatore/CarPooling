package uk.co.maxcarli.carpooling.Fragment;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import uk.co.maxcarli.carpooling.Control.Controlli;
import uk.co.maxcarli.carpooling.Database;
import uk.co.maxcarli.carpooling.R;
import uk.co.maxcarli.carpooling.TrackingOfferente;
import uk.co.maxcarli.carpooling.TrackingRichiedente;
import uk.co.maxcarli.carpooling.menu;
import uk.co.maxcarli.carpooling.model.Cittadino;
import uk.co.maxcarli.carpooling.model.Passaggio;


import static uk.co.maxcarli.carpooling.Control.ControlBluetooth.verificaSupportoB;
import static uk.co.maxcarli.carpooling.Fragment.PassaggiOfferti.controlloTipoPassagio;
import static uk.co.maxcarli.carpooling.Control.Controlli.*;

public class TrackingFragment extends Fragment {

    private Cittadino cittadino;
    private Passaggio passaggio;

    public static final int REQUEST_ENABLE_BT = 1;

    private menu menuActivity;






    String[][] passaggi;

    View view;
    TableView<String[]> tb;

    public TrackingFragment() {
        // Required empty public constructor
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
        String dataCorrente = getCurrentData();
        //dataCorrente=Controlli.impostaFormatoData(dataCorrente);
        Log.d("dataCorrente", dataCorrente);
        int vista = 0;
        View rootServer = null;

        final Passaggio p = controlOfferer(cittadino.passaggiOfferti, dataCorrente);

        String[] PassaggiOffertiHeaders={getString(R.string.Nome),getString(R.string.Cognome),getString(R.string.residenza),getString(R.string.Telefono)};




        if(p!=null) {
           rootServer = inflater.inflate(R.layout.fragment_tracking, container, false);
            tb=(TableView<String[]>) rootServer.findViewById(R.id.Tabellatracking);
            populateData(p.cittadiniRichiedenti);

            tb.setVisibility(View.VISIBLE);

            tb.setColumnCount(4);
            tb.setColumnWeight(0,10);
            tb.setColumnWeight(1,13);
            tb.setColumnWeight(2,7);
            tb.setColumnWeight(3,10);

            tb.setHeaderBackgroundColor(Color.parseColor("#2ecc71"));

            SimpleTableHeaderAdapter headerAdapter=new SimpleTableHeaderAdapter(getActivity(),PassaggiOffertiHeaders);

            headerAdapter.setTextSize(15);

            tb.setHeaderAdapter(headerAdapter);
            SimpleTableDataAdapter dataAdapter=new SimpleTableDataAdapter(getActivity(),passaggi);


            dataAdapter.setTextSize(10);


            tb.setDataAdapter(dataAdapter);

            tb.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            passaggio = p;
            Button button=(Button)rootServer.findViewById(R.id.buttonTracking);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                   if(avviaBluetooth(mBluetoothAdapter) == true) {
                       Intent intent = new Intent(getActivity(), TrackingOfferente.class);
                       intent.putExtra(Cittadino.Keys.IDCITTADINO, cittadino);
                       intent.putExtra(Passaggio.Keys.IDPASSAGGIO, passaggio);
                       startActivityForResult(intent, 1);
                   }

                }
            });

        } else {
            rootServer = inflater.inflate(R.layout.fragment_tracking_richiesta, container, false);
            int trovato = cittadinoPassaggiRichiesti(cittadino.passaggiRichiesti,dataCorrente, rootServer);

            if (trovato == 0){
                rootServer = inflater.inflate(R.layout.fragment_tracking_vuoto, container, false);
            } else{

                Button button=(Button)rootServer.findViewById(R.id.buttonTrackingRichiesta);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                       if(avviaBluetooth(mBluetoothAdapter) == true) {
                           Intent intent = new Intent(getActivity(), TrackingRichiedente.class);
                           intent.putExtra(Cittadino.Keys.IDCITTADINO, cittadino);
                           intent.putExtra(Passaggio.Keys.IDPASSAGGIO, passaggio);
                           startActivity(intent);
                       }

                    }
                });

            }

        }



        return rootServer;
    }

    /**
     * se il cittadino è un offerente ritorna il passaggio, altrimenti null
     * @param passaggiOfferti
     * @param dataCorrente
     * @return p
     */

    public Passaggio controlOfferer(List<Passaggio> passaggiOfferti, String dataCorrente){

        String stringaOrario = ""; //serve solo per il log
        String dataPassaggio = "";
        String stringaOrarioC = ""; //serve solo per il log
        String sTrovato = ""; // serve solo per il log
        int trovato = 0;
        int orarioConvertito;
        for(int i=0;i<passaggiOfferti.size();i++){
//
            Passaggio p= passaggiOfferti.get(i);
            Log.d("passaggioOfferto",p.getData());
            dataPassaggio = p.getData();

            Log.d("dataPassaggio", dataPassaggio);
            Log.d("dataCorrente",dataCorrente);

            if(dataPassaggio.equalsIgnoreCase(dataCorrente)) {
                orarioConvertito = oreInMinuti(p.getOra());
                stringaOrario = Integer.toString(orarioConvertito);
                Log.d("oreInMinuti",stringaOrario);
                stringaOrarioC = Integer.toString(getOraCorrente());
                Log.d("OraCorrente",stringaOrarioC);
                if(getOraCorrente() >= orarioConvertito - 10 && orarioConvertito + 30 >= getOraCorrente() ){
                    trovato = 1;
                    sTrovato = Integer.toString(trovato);
                    Log.d("trovato",sTrovato);
                    Database.getTrackingConvalidato(p.getIdPassaggiOfferti(), getContext(),1,null);
                    if(Database.getCompletato() != 1)
                    return p;
                }



            }
        }
            return null;
    }

    public void populateData(List<Cittadino> cittadiniRichiedenti){

        this.passaggi=new String[cittadiniRichiedenti.size()][4];


        for(int i=0;i<cittadiniRichiedenti.size();i++){

            Cittadino c = cittadiniRichiedenti.get(i);

            this.passaggi[i][0]=c.getNome();
            this.passaggi[i][1]=c.getCognome();
            this.passaggi[i][2]=c.getResidenza();
            this.passaggi[i][3]=c.getNumeroTelefono();

        }
    }


    public void onAttach(Activity activity) {

        super.onAttach(activity);
        menuActivity=(menu)activity;
    }

    public int cittadinoPassaggiRichiesti(List<Passaggio> passaggiRichiesti,String dataCorrente, View root) {


        String stringaOrario = ""; //serve solo per il log
        String stringaOrarioC = ""; //serve solo per il log
        int trovato = 0;
        int orarioConvertito;
        for (int i = 0; i < passaggiRichiesti.size(); i++) {

            Passaggio p = passaggiRichiesti.get(i);
            Log.d("Data", p.getData());
            Log.d("DataCorrente", dataCorrente);

            if (p.getData().equalsIgnoreCase(dataCorrente)) {
                orarioConvertito = oreInMinuti(p.getOra());
                stringaOrario = Integer.toString(orarioConvertito);
                Log.d("oraRichiesta", stringaOrario);
                stringaOrarioC = Integer.toString(getOraCorrente());
                Log.d("OraCorrente", stringaOrarioC);
                if (getOraCorrente() >= orarioConvertito - 10 && orarioConvertito + 30 >= getOraCorrente()) {
                    Database.getTrackingConvalidato(p.getIdPassaggiOfferti(), getContext(), 1,null);
                    if (Database.getCompletato() != 1) {

//

                        trovato = 1;
                        passaggio = p;
                        Log.d("CittadinoOfferente", p.getCittadinoOfferente().getCognome().toString());
                        TextInputEditText textCognome = (TextInputEditText) root.findViewById(R.id.cognomeOfferente);
                        textCognome.setKeyListener(null);
                        TextInputEditText textNome = (TextInputEditText) root.findViewById(R.id.nomeOfferente);
                        textNome.setKeyListener(null);
                        TextInputEditText textCellulare = (TextInputEditText) root.findViewById(R.id.telefonoOfferente);
                        textCellulare.setKeyListener(null);
                        TextInputEditText textAuto = (TextInputEditText) root.findViewById(R.id.autoOfferente);
                        textAuto.setKeyListener(null);
                        textCognome.setText(p.getCittadinoOfferente().getCognome().toString());
                        textNome.setText(p.getCittadinoOfferente().getNome().toString());
                        textCellulare.setText(p.getCittadinoOfferente().getNumeroTelefono().toString());
                        textAuto.setText(p.getAuto().toString());
                    }
                }

            }
        }
        return trovato;

    }


    public  boolean avviaBluetooth(BluetoothAdapter mBluetoothAdapter){
        Log.d("sono entrato","avviaBluetooth()");
        if(verificaSupportoB(mBluetoothAdapter)== true){
            if(!(mBluetoothAdapter.isEnabled()) ){
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
        if(mBluetoothAdapter.isEnabled()) return true;
        else return false;
    }



}
