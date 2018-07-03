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
import android.widget.ImageView;

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

        cittadino=menuActivity.getCittadino();

        TextInputEditText cognome=(TextInputEditText)root.findViewById(R.id.edtselezionacognome);
        cognome.setText(cittadino.getCognome());
        TextInputEditText nome=(TextInputEditText)root.findViewById(R.id.edtselezionanome);
        nome.setText(cittadino.getNome());
        TextInputEditText telefono=(TextInputEditText)root.findViewById(R.id.edtselezionatelefono);
        telefono.setText(cittadino.getNumeroTelefono());
        TextInputEditText CF=(TextInputEditText)root.findViewById(R.id.edtselezionaCF);
        CF.setText(cittadino.getCodiceFiscale());
        TextInputEditText email= (TextInputEditText)root.findViewById(R.id.edtselezionaemail);
        email.setText(cittadino.getEmail());
        residenza= (TextInputEditText)root.findViewById(R.id.edtselezionaresidenza);
        residenza.setText(cittadino.getResidenza());
       password=(TextInputEditText)root.findViewById(R.id.edtModificaPass);
        password.setText(cittadino.getPassword());

        /*azienda=(TextInputEditText)root.findViewById(R.id.edtselezionaAzienda);
        azienda.setText(cittadino.getSede().getAzienda().getNome());*/

        ImageView modResidenza=(ImageView)root.findViewById(R.id.modResidenza);

        modResidenza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ModificaResidenzaActivity.class);
                intent.putExtra(Cittadino.Keys.RESIDENZA,residenza.getText());
                startActivityForResult(intent,0);

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



}
