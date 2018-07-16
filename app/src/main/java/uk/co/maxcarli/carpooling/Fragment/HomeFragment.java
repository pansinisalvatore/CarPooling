package uk.co.maxcarli.carpooling.Fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import uk.co.maxcarli.carpooling.Filtro;
import uk.co.maxcarli.carpooling.OffriPassaggi;
import uk.co.maxcarli.carpooling.R;
import uk.co.maxcarli.carpooling.menu;
import uk.co.maxcarli.carpooling.model.Cittadino;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private  menu menuActivity;
    private Cittadino cittadino;

    public HomeFragment() {
        // Required empty public constructor
    }



    public void onAttach(Activity activity){
        super.onAttach(activity);
        if(activity instanceof menu){
            menuActivity=(menu)activity;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_home, container, false);
        final Button offriPassaggio=(Button)root.findViewById(R.id.offriPassaggio);
        Button cercaPassaggio=(Button)root.findViewById(R.id.TrovaPassaggio);
        cittadino=menuActivity.getCittadino();

        offriPassaggio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getActivity(), OffriPassaggi.class);
                intent.putExtra(Cittadino.Keys.IDCITTADINO,cittadino);
                startActivityForResult(intent,1);

            }
        });

        cercaPassaggio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Filtro.class);
                intent.putExtra(Cittadino.Keys.IDCITTADINO,cittadino);
                startActivityForResult(intent,2);
            }
        });


        return root;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent databack){
        if(databack!=null){
            Cittadino c=(Cittadino) databack.getParcelableExtra(Cittadino.Keys.IDCITTADINO);

            if(c!=null){
                cittadino=c;
                menuActivity.setCittadino(c);
            }
        }

    }

}
