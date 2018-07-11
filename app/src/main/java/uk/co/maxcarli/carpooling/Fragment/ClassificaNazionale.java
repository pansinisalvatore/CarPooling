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


public class ClassificaNazionale extends Fragment {


    menu menuActivity;
    String[] ClassificaNazionaleHeaders={"Posizione","Dipendente","Azienda","Punti"};
    String[][] cl_nazione;

    TableView<String[]> tabel;

    public ClassificaNazionale(){

    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View class_nazionale= inflater.inflate(R.layout.fragment_classifica_nazionale,null);
        tabel=(TableView<String[]>) class_nazionale.findViewById(R.id.TabellaClassificaNazionale);
        tabel.setColumnCount(4);
        tabel.setHeaderBackgroundColor(Color.parseColor("#2ecc71"));
        tabel.setHeaderAdapter(new SimpleTableHeaderAdapter(getActivity(),ClassificaNazionaleHeaders));

        String url= "http://carpoolingsms.altervista.org/PHP/LeggiClassificaNazionale.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {

                            JSONArray jsonarray = new JSONArray(response);
                            cl_nazione=new String[jsonarray.length()][4];
                            for(int i=0; i < jsonarray.length(); i++) {

                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                String nome=jsonobject.getString("NomeCittadino");
                                String cognome=jsonobject.getString("CognomeCittadino");
                                String azienda=jsonobject.getString("NomeAzienda");
                                int punti= jsonobject.getInt("Punteggio");


                                cl_nazione[i][0]=(i+1)+"";
                                cl_nazione[i][1]=cognome+" "+nome;
                                cl_nazione[i][2]=azienda;
                                cl_nazione[i][3]= punti+"";

                                Log.i("Dati", " "+nome+" "+cognome+" "+azienda +" "+punti );

                            }


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





        // Inflate the layout for this fragment
        return class_nazionale;


    }




    public void onAttach(Activity activity) {

        super.onAttach(activity);
        menuActivity=(menu)activity;
    }

}
