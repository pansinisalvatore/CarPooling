package uk.co.maxcarli.carpooling;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;



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


    }

    public void ApriRegisterActivity(View view) {
        Intent Registrazione=  new Intent(this,RegistrazioneCittadino.class);
        startActivity(Registrazione);

    }


}
