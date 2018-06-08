package uk.co.maxcarli.carpooling;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class ReimpostaPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reimposta_password);

        String url = "http://carpoolingsms.altervista.org/PHP/apriApp.php";
        Intent i = new Intent(Intent.ACTION_VIEW , Uri.parse("www.annaritascema.com/test"));
        startActivity(i);
        Log.d("ci sei", "sei entrato in reimposta password");

    }
}
