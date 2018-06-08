package uk.co.maxcarli.carpooling;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;


public class ClassificaNazionale extends Fragment {

    View class_nazionale;
    String azienda;
    String[] ClassificaNazionaleHeaders={"Nome","Cognome","Azienda","Punti"};
    String[][] cl_nazione;
    List<Class_Nazione> classifica_nazionale=new ArrayList<>();
    TableView<String[]> tabel;

    public ClassificaNazionale(){

    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        class_nazionale= inflater.inflate(R.layout.fragment_classifica_nazionale,null);
        tabel=(TableView<String[]>) class_nazionale.findViewById(R.id.TabellaClassificaNazionale);
        tabel.setColumnCount(4);
        tabel.setHeaderBackgroundColor(Color.parseColor("#2ecc71"));


        String url= "http://carpoolingsms.altervista.org/PHP/LeggiClassificaNazionale.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {

                            JSONArray jsonarray = new JSONArray(response);

                            for(int i=0; i < jsonarray.length(); i++) {

                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                String nome=jsonobject.getString("Nome");
                                String cognome=jsonobject.getString("Cognome");
                                azienda=jsonobject.getString("Azienda");
                                int punti= jsonobject.getInt("Punti");


                                Log.i("Dati", " "+nome+" "+cognome+" "+azienda +" "+punti );
                                classifica_nazionale.add(new Class_Nazione(nome,cognome,azienda,punti));
                            }
                            populateData(classifica_nazionale);
                            tabel.setHeaderAdapter(new SimpleTableHeaderAdapter(getActivity(),ClassificaNazionaleHeaders));
                            tabel.setDataAdapter(new SimpleTableDataAdapter(getActivity(), cl_nazione));


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
                }){

        };

        MySingleton.getmInstance(getContext()).addTorequestque(stringRequest);

        Toast.makeText(getContext(),classifica_nazionale.size()+" ",Toast.LENGTH_SHORT).show();



        // Inflate the layout for this fragment
        return class_nazionale;


    }


    public void populateData(List<Class_Nazione> passaggiRichiesti){

        this.cl_nazione=new String[classifica_nazionale.size()][4];


        for(int i=0;i<classifica_nazionale.size();i++){

            Class_Nazione cl= classifica_nazionale.get(i);

            this.cl_nazione[i][0]=cl.getNome();
            this.cl_nazione[i][1]=cl.getCognome();
            this.cl_nazione[i][2]=cl.getAzienda();
            this.cl_nazione[i][3]= String.valueOf(cl.getPunti());


        }
    }


}
