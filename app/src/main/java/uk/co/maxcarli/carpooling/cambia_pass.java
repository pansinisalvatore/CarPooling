package uk.co.maxcarli.carpooling;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import uk.co.maxcarli.carpooling.Control.Controlli;
import uk.co.maxcarli.carpooling.model.Cittadino;

public class cambia_pass extends AppCompatActivity {

    private String srcPassword;

    private TextInputEditText textVecchiaPassword;
    private TextInputEditText  textNuovaPassword;
    private TextInputEditText textConfermaPassword;

    private Button conferma;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambia_pass);

        srcPassword=getIntent().getStringExtra(Cittadino.Keys.IDCITTADINO);

        textVecchiaPassword=(TextInputEditText)findViewById(R.id.editTextVecchiaPass);
        textNuovaPassword=(TextInputEditText)findViewById(R.id.editTextNuovaPass);
        textConfermaPassword=(TextInputEditText)findViewById(R.id.editTextConfermaPass);
        conferma=findViewById(R.id.confermaModifiche);

        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!Controlli.controlloEditTextVuoto(textVecchiaPassword) && !Controlli.controlloEditTextVuoto(textNuovaPassword) && !Controlli.controlloEditTextVuoto(textConfermaPassword)) {
                    String vecchiaPass = textVecchiaPassword.getText().toString();
                    String nuovaPass = textNuovaPassword.getText().toString();
                    String confPass = textConfermaPassword.getText().toString();
                    if (vecchiaPass.equals(srcPassword)) {
                        if (confPass.equals(nuovaPass)) {
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra(Cittadino.Keys.IDCITTADINO, nuovaPass);
                            setResult(1, returnIntent);
                            finish();
                        } else {
                            textConfermaPassword.setError(getString(R.string.TestoConfermaPasswordErrore));
                        }

                    } else {
                        textVecchiaPassword.setError(getString(R.string.erroreVecchiaPAssword));
                    }
                }
            }
        });

    }
}
