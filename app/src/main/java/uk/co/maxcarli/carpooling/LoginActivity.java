package uk.co.maxcarli.carpooling;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import uk.co.maxcarli.carpooling.model.Azienda;
import uk.co.maxcarli.carpooling.model.Cittadino;
import uk.co.maxcarli.carpooling.model.Sede;

import static uk.co.maxcarli.carpooling.Database.accedi;
import static uk.co.maxcarli.carpooling.Control.Controlli.*;

/**
 * Questa activity viene mostrata dopo la SplashActivity e gestisce le operazioni di accesso e di registrazione
 */

public class LoginActivity extends AppCompatActivity {



    private TextInputEditText loginEmail;
    private TextInputEditText loginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmail= (TextInputEditText)findViewById(R.id.emailLogin);
        loginPassword= (TextInputEditText)findViewById(R.id.passwordLogin);
        if(savedInstanceState!=null){
            loginEmail.setText(savedInstanceState.getString("email"));
            loginPassword.setText(savedInstanceState.getString("password"));
        }
        ImageView loading=findViewById(R.id.loading);
        loading.setVisibility(View.GONE);

    }

    protected void onStart(){
        super.onStart();
    }

    /**
     *
     * @param view
     * Al click del bottone, viene raccolta l'email e la password inserita dall'utente e inizia le procedure di accesso. Viene chiamato
     * il metodo statico accedi(String email, String password, Cittadino cittadino, Context c) della classe Database.
     */
    public void Login(View view) {
        String email=loginEmail.getText().toString();
        String password=loginPassword.getText().toString();
        if(!controlloEditTextVuoto(loginEmail,this) && !controlloEditTextVuoto(loginPassword,this)){
            Cittadino cittadino=new Cittadino();
            Azienda cAzienda=new Azienda();
            Sede cSede=new Sede();
            cSede.setAzienda(cAzienda);
            cittadino.setSede(cSede);
            ImageView loading=findViewById(R.id.loading);
            loading.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.rotate);
            loading.startAnimation(animation);
            accedi(email,password,cittadino,this);


        }


    }


    public void apriRegisterActivity(View view) {
        Intent Registrazione=  new Intent(this,RegistrazioneCittadino.class);
        startActivity(Registrazione);
    }



    public void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        String email=loginEmail.getText().toString();
        String password=loginPassword.getText().toString();
        bundle.putString("email",email);
        bundle.putString("password",password);
    }

}
