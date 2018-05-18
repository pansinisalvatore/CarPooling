package uk.co.maxcarli.carpooling;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class RegistrazioneCittadino extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione_cittadino);
    }


    public void editTextRegistrazione(View view) {
        EditText aux = (EditText) findViewById(R.id.NomeCittadino);
        String nome = aux.getText().toString();
        EditText aux1 = (EditText) findViewById(R.id.CognomeCittadino);
        String cognome = aux.getText().toString();
        Cittadino cittadino = new Cittadino(nome,cognome);
        cittadino.registraCittadino(nome,cognome);
    }
}
