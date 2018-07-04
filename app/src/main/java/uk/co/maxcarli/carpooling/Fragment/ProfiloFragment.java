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
import android.widget.Toast;

import uk.co.maxcarli.carpooling.Control.Controlli;
import uk.co.maxcarli.carpooling.Database;
import uk.co.maxcarli.carpooling.ModificaResidenzaActivity;
import uk.co.maxcarli.carpooling.R;
import uk.co.maxcarli.carpooling.RicercaIndirizzo;
import uk.co.maxcarli.carpooling.model.Cittadino;
import uk.co.maxcarli.carpooling.menu;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfiloFragment extends Fragment {


    private  menu menuActivity;
    private Cittadino cittadino;

    private TextInputEditText cognome;
    private TextInputEditText nome;
    private TextInputEditText telefono;
    private TextInputEditText CF;
    private TextInputEditText email;

    private TextInputEditText residenza;
    private TextInputEditText password;
    private TextInputEditText azienda;



    public ProfiloFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_profilo, container, false);

        if(savedInstanceState!=null){
            cittadino=savedInstanceState.getParcelable(Cittadino.Keys.IDCITTADINO);
        }else{
            cittadino=menuActivity.getCittadino();
        }


        cognome=(TextInputEditText)root.findViewById(R.id.edtselezionacognome);
        cognome.setText(cittadino.getCognome());
        cognome.setKeyListener(null);
        nome=(TextInputEditText)root.findViewById(R.id.edtselezionanome);
        nome.setText(cittadino.getNome());
        nome.setKeyListener(null);
        telefono=(TextInputEditText)root.findViewById(R.id.edtselezionatelefono);
        telefono.setText(cittadino.getNumeroTelefono());
        CF=(TextInputEditText)root.findViewById(R.id.edtselezionaCF);
        CF.setText(cittadino.getCodiceFiscale());
        CF.setKeyListener(null);
        email= (TextInputEditText)root.findViewById(R.id.edtselezionaemail);
        email.setText(cittadino.getEmail());
        email.setKeyListener(null);
        residenza= (TextInputEditText)root.findViewById(R.id.edtselezionaresidenza);
        residenza.setText(cittadino.getResidenza());

        password=(TextInputEditText)root.findViewById(R.id.edtModificaPass);
        password.setText(cittadino.getPassword());
        password.setKeyListener(null);
        azienda=(TextInputEditText)root.findViewById(R.id.edtselezionaAzienda);
        azienda.setText(cittadino.getSede().getAzienda().getNome());

        ImageView modResidenza=(ImageView)root.findViewById(R.id.modResidenza);

        modResidenza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ModificaResidenzaActivity.class);
                intent.putExtra(Cittadino.Keys.RESIDENZA,residenza.getText());
                startActivityForResult(intent,0);

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

            }
        });

        return root;

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        String indirizzo=(String)data.getStringExtra(Cittadino.Keys.RESIDENZA);
        residenza.setText(indirizzo);
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
