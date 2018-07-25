package uk.co.maxcarli.carpooling;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import uk.co.maxcarli.carpooling.model.Cittadino;
import uk.co.maxcarli.carpooling.model.Passaggio;

public class TrackingOfferente extends AppCompatActivity {
    private Passaggio passaggio;
    private Cittadino cittadino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_offerente);
        passaggio = getIntent().getParcelableExtra(Passaggio.Keys.IDPASSAGGIO);
        cittadino = getIntent().getParcelableExtra(Cittadino.Keys.IDCITTADINO);
        Log.d("TrackingOfferente", cittadino.getCognome().toString());
        rotate(true);

    }

    public void rotate(boolean start){
        if (start == true) {

            View decorView = getWindow().getDecorView();
            ImageView splash = findViewById(R.id.splash_imageT);
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.rotate);
            splash.startAnimation(animation);
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}
