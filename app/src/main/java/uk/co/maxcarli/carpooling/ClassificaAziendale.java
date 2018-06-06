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


public class ClassificaAziendale extends Fragment {

    String[] ClassificaAziendaleHeaders={"Posizione","Nome","Cognome","Punti"};
    String[][] cl_azienda ;
    List<Class_Azienda> classifica_aziendale=new ArrayList<>();

    View class_aziendale;
    TableView<String[]> tabella;

    public ClassificaAziendale(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       class_aziendale= inflater.inflate(R.layout.fragment_classifica_aziendale,null);
       tabella=(TableView<String[]>)class_aziendale.findViewById(R.id.TabellaClassificaAziendale);
       tabella.setColumnCount(4);
       tabella.setHeaderBackgroundColor(Color.parseColor("#2ecc71"));

        String url= "http://carpoolingsms.altervista.org/PHP/LeggiClassificaAziendale.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {

                            JSONArray jsonarray = new JSONArray(response);

                            for(int i=0; i < jsonarray.length(); i++) {

                                JSONObject jsonobject = jsonarray.getJSONObject(i);

                                int posizione= jsonobject.getInt("Posizione");
                                String nome=jsonobject.getString("Nome");
                                String cognome=jsonobject.getString("Cognome");
                                int punti= jsonobject.getInt("Punti");


                                Log.i("Dati", posizione+" "+nome+" "+cognome+" "+ punti );
                                classifica_aziendale.add(new Class_Azienda(posizione,nome,cognome,punti));
                                //Toast.makeText(context, viaggio+" "+data+" "+ora+" "+status+" "+postiOccupati,Toast.LENGTH_SHORT).show();

                            }
                            populateData(classifica_aziendale);
                            tabella.setHeaderAdapter(new SimpleTableHeaderAdapter(getActivity(),ClassificaAziendaleHeaders));
                            tabella.setDataAdapter(new SimpleTableDataAdapter(getActivity(), cl_azienda));


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

         /*   protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Azienda", azienda);
                return params;
            }
        //
        */

        };

        MySingleton.getmInstance(getContext()).addTorequestque(stringRequest);

        Toast.makeText(getContext(),classifica_aziendale.size()+" ",Toast.LENGTH_SHORT).show();



        // Inflate the layout for this fragment
        return class_aziendale;


    }


    public void populateData(List<Class_Azienda> passaggiRichiesti){

        this.cl_azienda=new String[classifica_aziendale.size()][4];


        for(int i=0;i<passaggiRichiesti.size();i++){

            Class_Azienda cl= classifica_aziendale.get(i);

            this.cl_azienda[i][0]= String.valueOf(cl.getPosizione());
            this.cl_azienda[i][1]=cl.getNome();
            this.cl_azienda[i][2]=cl.getCognome();
            this.cl_azienda[i][3]= String.valueOf(cl.getPunti());


        }
    }


}
