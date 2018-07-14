package uk.co.maxcarli.carpooling;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import uk.co.maxcarli.carpooling.menu;
import uk.co.maxcarli.carpooling.model.Cittadino;
import uk.co.maxcarli.carpooling.model.Passaggio;

import static uk.co.maxcarli.carpooling.Fragment.PassaggiOfferti.controlloTipoPassagio;
import static uk.co.maxcarli.carpooling.Control.Controlli.*;

public class TrackingFragment extends Fragment {

    private Cittadino cittadino;
    private menu menuActivity;

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
        int vista = 0;
        View rootServer = inflater.inflate(R.layout.fragment_tracking, container, false);

        vista = controlOfferer(cittadino.passaggiOfferti, dataCorrente);



        return rootServer;
    }






    public int controlOfferer(List<Passaggio> passaggiOfferti, String dataCorrente){

        String stringaOrario = ""; //serve solo per il log
        String stringaOrarioC = ""; //serve solo per il log
        String sTrovato = ""; // serve solo per il log
        int trovato = 0;
        int orarioConvertito;
        for(int i=0;i<passaggiOfferti.size();i++){

            Passaggio p= passaggiOfferti.get(i);
            if(p.getData().equals(dataCorrente)) {
                orarioConvertito = oreInMinuti(p.getOra());
                stringaOrario = Integer.toString(orarioConvertito);
                Log.d("oreInMinuti",stringaOrario);
                stringaOrarioC = Integer.toString(getOraCorrente());
                Log.d("OraCorrente",stringaOrarioC);
                if(orarioConvertito > getOraCorrente() - 10 && orarioConvertito < getOraCorrente() + 30 ){
                    trovato = 1;
                    sTrovato = Integer.toString(trovato);
                    Log.d("trovato",sTrovato);
                }



            }
        }
            return trovato;
    }

    public void personeDaPrendere(String dataCorrente){
        String string = "";

    }


    public void onAttach(Activity activity) {

        super.onAttach(activity);
        menuActivity=(menu)activity;
    }



}
