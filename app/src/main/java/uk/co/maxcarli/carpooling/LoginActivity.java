package uk.co.maxcarli.carpooling;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static uk.co.maxcarli.carpooling.Database.accedi;
import static uk.co.maxcarli.carpooling.Control.Controlli.*;


public class LoginActivity extends AppCompatActivity {



    private EditText loginEmail;
    private EditText loginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginEmail=findViewById(R.id.LoginEmail);
        loginPassword=findViewById(R.id.LoginPassword);


    }

    protected void onStart(){
        super.onStart();
    }

    public void Login(View view) {
        String email=loginEmail.getText().toString();
        String password=loginPassword.getText().toString();
        if(controlloEditTextVuoto(loginEmail) && controlloEditTextVuoto(loginPassword)){
            accedi(email,password,this);
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
