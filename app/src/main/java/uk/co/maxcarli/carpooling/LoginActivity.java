package uk.co.maxcarli.carpooling;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import uk.co.maxcarli.carpooling.model.Cittadino;

import static uk.co.maxcarli.carpooling.Database.accedi;
import static uk.co.maxcarli.carpooling.Control.Controlli.*;


public class LoginActivity extends AppCompatActivity {



    private TextInputEditText loginEmail;
    private TextInputEditText loginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginEmail= (TextInputEditText)findViewById(R.id.emailLogin);
        loginPassword= (TextInputEditText)findViewById(R.id.passwordLogin);


    }

    protected void onStart(){
        super.onStart();
    }

    public void Login(View view) {
        String email=loginEmail.getText().toString();
        String password=loginPassword.getText().toString();
        if(!controlloEditTextVuoto(loginEmail) && !controlloEditTextVuoto(loginPassword)){
            Cittadino cittadino=new Cittadino();
            accedi(email,password,cittadino,this);
        }

    }


    public void apriRegisterActivity(View view) {
        Intent Registrazione=  new Intent(this,RegistrazioneCittadino.class);
        startActivity(Registrazione);
    }

    public void passwordDimenticata(View view){
        Intent intent = new Intent(this,RecuperaPassword.class);
        startActivity(intent);
    }


}
