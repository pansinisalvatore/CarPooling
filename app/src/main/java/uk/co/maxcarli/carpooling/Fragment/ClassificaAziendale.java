package uk.co.maxcarli.carpooling.Fragment;

import android.app.Activity;
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
import uk.co.maxcarli.carpooling.MySingleton;
import uk.co.maxcarli.carpooling.R;
import uk.co.maxcarli.carpooling.menu;
import uk.co.maxcarli.carpooling.model.Cittadino;


public class ClassificaAziendale extends Fragment {


    menu menuActivity;
    Cittadino cittadino;
    String[] ClassificaAziendaleHeaders={"Posizione","Dipendene","Punti"};
    String[][] cl_azienda ;


    View class_aziendale;
    TableView<String[]> tabella;

    public ClassificaAziendale(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       cittadino=menuActivity.getCittadino();
       class_aziendale= inflater.inflate(R.layout.fragment_classifica_aziendale,null);
       tabella=(TableView<String[]>)class_aziendale.findViewById(R.id.TabellaClassificaAziendale);
       tabella.setColumnCount(3);
       ClassificaAziendaleHeaders=new String[3];
       ClassificaAziendaleHeaders[0]=getString(R.string.posizioneClassifica);
       ClassificaAziendaleHeaders[1]=getString(R.string.Dipendente);
       ClassificaAziendaleHeaders[2]=getString(R.string.punteggio);
        tabella.setColumnWeight(0,10);
        tabella.setColumnWeight(1,13);
        tabella.setColumnWeight(2,7);

       tabella.setHeaderBackgroundColor(Color.parseColor("#2ecc71"));
       tabella.setHeaderAdapter(new SimpleTableHeaderAdapter(getActivity(),ClassificaAziendaleHeaders));
        String url= "http://carpoolingsms.altervista.org/PHP/LeggiClassificaAziendale.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {

                            JSONArray jsonarray = new JSONArray(response);
                            cl_azienda=new String[jsonarray.length()][3];
                            for(int i=0; i < jsonarray.length(); i++) {

                                JSONObject jsonobject = jsonarray.getJSONObject(i);


                                String nome=jsonobject.getString("NomeCittadino");
                                String cognome=jsonobject.getString("CognomeCittadino");
                                int punti= jsonobject.getInt("Punteggio");



                                cl_azienda[i][0]= (i+1)+"";
                                cl_azienda[i][1]=cognome+" "+nome;
                                cl_azienda[i][2]=punti+"";



                                //Log.i("Dati", posizione+" "+nome+" "+cognome+" "+ punti );
                                //Toast.makeText(context, viaggio+" "+data+" "+ora+" "+status+" "+postiOccupati,Toast.LENGTH_SHORT).show();

                            }


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

            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Azienda", cittadino.getSede().getAzienda().getIdAzienda()+"");
                return params;
            }
        //


        };

        MySingleton.getmInstance(getContext()).addTorequestque(stringRequest);




        // Inflate the layout for this fragment
        return class_aziendale;


    }



    public void onAttach(Activity activity) {

        super.onAttach(activity);
        menuActivity=(menu)activity;
    }


}
