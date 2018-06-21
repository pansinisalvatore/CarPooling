package uk.co.maxcarli.carpooling;


import android.arch.core.executor.DefaultTaskExecutor;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static uk.co.maxcarli.carpooling.Database.*;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class PassaggiRichiesti extends Fragment {


    String[] PassaggiRichiestiHeaders={"Viaggio","Data&Ora","Stato","Posti disponibili"};
    String[][] passaggi;
    List<Passaggio> passaggiRichiesti=new ArrayList<Passaggio>();

    View view;
    TableView<String[]> tb;

    public PassaggiRichiesti() {
        // Required empty public constructor
    }

    public static PassaggiRichiesti newInstance(String param1, String param2) {
        PassaggiRichiesti fragment = new PassaggiRichiesti();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view=inflater.inflate(R.layout.fragment_passaggi_richiesti,null);

        tb=(TableView<String[]>) view.findViewById(R.id.TabellaPassaggiRichiesti);


        tb.setColumnCount(4);
        tb.setHeaderBackgroundColor(Color.parseColor("#2ecc71"));


        String url= "http://carpoolingsms.altervista.org/PHP/LeggiPassaggiRichiesti.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {

                            JSONArray jsonarray = new JSONArray(response);

                            for(int i=0; i < jsonarray.length(); i++) {

                                JSONObject jsonobject = jsonarray.getJSONObject(i);

                                String viaggio=jsonobject.getString("TipoViaggioPassaggioRichiesto");

                                String data=jsonobject.getString("DataPassaggioRichiesto");

                                String ora=jsonobject.getString("OraPassaggioRichiesto");

                                String status=jsonobject.getString("StatusPassaggioRichiesto");


                                int postiOccupati=jsonobject.getInt("PostiOccupatiPassaggioRichiesto");
                                int Idc=jsonobject.getInt("IdCittadinoPassaggiRichiesti");


                                Log.i("Dati", viaggio+" "+data+" "+ora+" "+ status+" "+postiOccupati);
                                passaggiRichiesti.add(new Passaggio(viaggio,data, ora, status, postiOccupati, Idc));
                                //Toast.makeText(context, viaggio+" "+data+" "+ora+" "+status+" "+postiOccupati,Toast.LENGTH_SHORT).show();

                            }
                            populateData(passaggiRichiesti);
                            tb.setHeaderAdapter(new SimpleTableHeaderAdapter(getActivity(), PassaggiRichiestiHeaders));
                            tb.setDataAdapter(new SimpleTableDataAdapter(getActivity(), passaggi));


                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error != null){

                            Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("idCittadino", Integer.toString(3));


                return params;
            }
        };

        MySingleton.getmInstance(getContext()).addTorequestque(stringRequest);

        Toast.makeText(getContext(),passaggiRichiesti.size()+" ",Toast.LENGTH_SHORT).show();



        // Inflate the layout for this fragment
        return view;


    }


   public void populateData(List<Passaggio> passaggiRichiesti){

        this.passaggi=new String[passaggiRichiesti.size()][4];


        for(int i=0;i<passaggiRichiesti.size();i++){

            Passaggio p= passaggiRichiesti.get(i);

            this.passaggi[i][0]=p.getViaggio();
            this.passaggi[i][1]=p.getData()+" "+p.getOra();
            this.passaggi[i][2]=p.getStatus();
            this.passaggi[i][3]=Integer.toString(p.postiOccupati);


        }
    }




}
