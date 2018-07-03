package uk.co.maxcarli.carpooling.Fragment;


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
import uk.co.maxcarli.carpooling.model.Cittadino;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private Cittadino cittadino;

    public HomeFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_home, container, false);
        final Button offriPassaggio=(Button)root.findViewById(R.id.offriPassaggio);
        Button cercaPassaggio=(Button)root.findViewById(R.id.TrovaPassaggio);


        offriPassaggio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getActivity(), OffriPassaggi.class);
                startActivity(intent);

            }
        });

        cercaPassaggio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Filtro.class);
                startActivity(intent);
            }
        });


        return root;
    }


}
