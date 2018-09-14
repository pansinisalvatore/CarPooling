package uk.co.maxcarli.carpooling;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Questa classe implementa la splash activity, contenente un immagine con un'animazione. Essa controlla se è stabilita una connessione a Internet
 *
 *
 */

public class Splash_activity extends Activity  implements Animation.AnimationListener{
    private static final String TAG_LOG = Splash_activity.class.getName();
    private static final long MIN_WAIT_INTERVAL= 1500L;
    private static final long MAX_WAIT_INTERVAL= 3000L;
    private static final int GO_AHEAD_WHAT=1;
    private long mStartTime;
    private boolean mIsDone;


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case GO_AHEAD_WHAT:
                    long elapsedTime = SystemClock.uptimeMillis()- mStartTime;
                    if (elapsedTime >= MIN_WAIT_INTERVAL && !mIsDone){
                        mIsDone= true;
                        goAhead();
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_activity);
        View decorView = getWindow().getDecorView();
        ImageView splash=findViewById(R.id.splash_image);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.rotate);
        splash.startAnimation(animation);
        int uiOptions= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
    }
    @Override
    protected void onStart(){
        super.onStart();
        mStartTime= SystemClock.uptimeMillis();
        final Message goAheadMessage = mHandler.obtainMessage(GO_AHEAD_WHAT);
        mHandler.sendMessageAtTime(goAheadMessage, mStartTime + MAX_WAIT_INTERVAL);
        Log.d(TAG_LOG, "Handler message sent!");
    }

    /**
     * Se è stabilita una connessione, avvia l'actrivity successiva, altrimenti visualizza un ALertDialog che avvisa
     * l'utente sulla necessità di una connessione a Internet.
     */
    private void goAhead(){
        final Intent intent = new Intent(this, LoginActivity.class);
        if(haveInternetConnection(Splash_activity.this)){
            startActivity(intent);
            finish();
        }else{
            AlertDialog.Builder miaAlert = new AlertDialog.Builder(this);
            miaAlert.setTitle("Connessione assente");
            miaAlert.setMessage("Devi attivare la connessione per utilizzare l'applicazione");
            AlertDialog alert = miaAlert.create();
            alert.show();
        }

        while(true){
            if(haveInternetConnection(Splash_activity.this)){
                startActivity(intent);
                finish();
            }else{
                AlertDialog.Builder miaAlert = new AlertDialog.Builder(this);
                miaAlert.setTitle(getString(R.string.ConnectionRequired));
                miaAlert.setMessage(getString(R.string.ConnectionRequiredText));
                AlertDialog alert = miaAlert.create();
                alert.show();
            }
        }

    }


    /**
     *
     * @param contesto
     * Questa funzione prende come parametro il context in quanto ne ha bisogno per il controllo della connessione.
     *
     * @return boolean
     * Ritorna true se è stabilita una connessione, altrimenti false.
     */

    public static boolean haveInternetConnection(Context contesto) {
            boolean haveConnectedWifi = false;
            boolean haveConnectedMobile = false;

            ConnectivityManager cm = (ConnectivityManager) contesto.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();
            for (NetworkInfo ni : netInfo) {
                if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                    if (ni.isConnected())
                        haveConnectedWifi = true;
                if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                    if (ni.isConnected())
                        haveConnectedMobile = true;
            }
            return haveConnectedWifi || haveConnectedMobile;
        }


    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
