package uk.co.maxcarli.carpooling.Fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import uk.co.maxcarli.carpooling.Control.Controlli;
import uk.co.maxcarli.carpooling.Database;
import uk.co.maxcarli.carpooling.ModificaResidenzaActivity;
import uk.co.maxcarli.carpooling.R;
import uk.co.maxcarli.carpooling.RicercaIndirizzo;
import uk.co.maxcarli.carpooling.model.Cittadino;
import uk.co.maxcarli.carpooling.menu;


/**
 * Questo fragment gestisce i dati personali dell'utente e permette di modificarne alcuni (password, telefono, residenza).
 */
public class ProfiloFragment extends Fragment {


    private  menu menuActivity;
    private Cittadino cittadino;

    private TextView cognomeNome;
    private TextInputEditText telefono;
    private TextView CF;
    private TextInputEditText email;

    private TextInputEditText residenza;
    private TextInputEditText password;
    private TextView azienda;



    public ProfiloFragment() {

    }


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            cittadino=savedInstanceState.getParcelable(Cittadino.Keys.IDCITTADINO);
        }else{
            cittadino=menuActivity.getCittadino();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_profilo, container, false);

        cognomeNome=(TextView)root.findViewById(R.id.nomeCognomeProfilo);
        cognomeNome.setText(cittadino.getCognome()+" "+cittadino.getNome());
        cognomeNome.setKeyListener(null);

        CF=(TextView) root.findViewById(R.id.CFProfilo);
        CF.setText(cittadino.getCodiceFiscale());
        CF.setKeyListener(null);

        azienda=(TextView)root.findViewById(R.id.AziendaProfilo);
        azienda.setText(cittadino.getSede().getAzienda().getNome());
        azienda.setKeyListener(null);

        telefono=(TextInputEditText)root.findViewById(R.id.telefonoProfilo);
        telefono.setText(cittadino.getNumeroTelefono());

        email= (TextInputEditText)root.findViewById(R.id.emailProfilo);
        email.setText(cittadino.getEmail());
        email.setKeyListener(null);
        residenza= (TextInputEditText)root.findViewById(R.id.residenzaProfilo);
        residenza.setText(cittadino.getResidenza());
        residenza.setKeyListener(null);

        residenza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ModificaResidenzaActivity.class);
                intent.putExtra(Cittadino.Keys.RESIDENZA,residenza.getText());
                startActivityForResult(intent,0);
            }
        });

        password=(TextInputEditText)root.findViewById(R.id.passwordProfilo);

        password.setText(cittadino.getPassword());
        password.setKeyListener(null);
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), uk.co.maxcarli.carpooling.CambiaPassword.class);
                intent.putExtra(Cittadino.Keys.IDCITTADINO,cittadino.getPassword());
                startActivityForResult(intent,1);
            }
        });


        Button conferma=root.findViewById(R.id.confermaModifiche);
        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cittadino.setPassword(password.getText().toString());
                cittadino.setResidenza(residenza.getText().toString());
                if(!Controlli.verificaNumeroTelefonico(telefono)){
                    cittadino.setNumeroTelefono(telefono.getText().toString());
                }


                Database.updateCittadino(cittadino,getContext());
                menuActivity.setCittadino(cittadino);

            }
        });

        return root;

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(data!=null){
            if(resultCode==0){
                String indirizzo=(String)data.getStringExtra(Cittadino.Keys.RESIDENZA);
                residenza.setText(indirizzo);
            }else{
                String pass=(String)data.getStringExtra(Cittadino.Keys.IDCITTADINO);
                password.setText(pass);
            }
        }



    }


    public void onAttach(Activity activity){
        super.onAttach(activity);
        if(activity instanceof menu){
            menuActivity=(menu)activity;
        }
    }

    public void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        bundle.putParcelable(Cittadino.Keys.IDCITTADINO,cittadino);
    }



}


