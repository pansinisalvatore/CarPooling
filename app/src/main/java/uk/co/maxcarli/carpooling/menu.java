package uk.co.maxcarli.carpooling;


        import android.annotation.SuppressLint;
        import android.app.AlertDialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.design.internal.BottomNavigationItemView;
        import android.support.design.internal.BottomNavigationMenuView;
        import android.support.design.widget.BottomNavigationView;
        import android.support.design.widget.CoordinatorLayout;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentTransaction;
        import android.support.v4.view.MenuItemCompat;
        import android.support.v7.app.ActionBar;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.ImageView;

        import java.lang.reflect.Field;

        import uk.co.maxcarli.carpooling.Fragment.ClassificaFragment;
        import uk.co.maxcarli.carpooling.Fragment.HomeFragment;
        import uk.co.maxcarli.carpooling.Fragment.ImieipassaggiFragment;
        import uk.co.maxcarli.carpooling.Fragment.ProfiloFragment;
        import uk.co.maxcarli.carpooling.Fragment.TrackingFragment;
        import uk.co.maxcarli.carpooling.model.Cittadino;
        import uk.co.maxcarli.carpooling.model.Sede;


public class menu extends AppCompatActivity {

    private ActionBar toolbar;
    private Cittadino cittadino;
    FragmentTransaction transaction;
    BottomNavigationView navigation;
    private View notificationBadge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);



        if(savedInstanceState!=null){
            cittadino=savedInstanceState.getParcelable(Cittadino.Keys.IDCITTADINO);
        }else {
        cittadino=(Cittadino)getIntent().getParcelableExtra(Cittadino.Keys.IDCITTADINO);
        }


        toolbar = getSupportActionBar();
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        // attaching bottom sheet behaviour - hide / show on scroll
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        removeShiftMode(navigation);
        if(savedInstanceState!=null){
            navigation.setSelectedItemId( savedInstanceState.getInt("SelectedItemId"));
        }else{
            loadFragment(new HomeFragment());
        }
        if(cittadino.getNotificaPassaggio()!=0){
            BottomNavigationMenuView bottomNavigationMenuView =
                    (BottomNavigationMenuView) navigation.getChildAt(0);
            BottomNavigationItemView iMieiPassaggi=(BottomNavigationItemView)bottomNavigationMenuView.getChildAt(3);
            notificationBadge = LayoutInflater.from(this).inflate(R.layout.view_notification_badge, iMieiPassaggi, false);
            iMieiPassaggi.addView(notificationBadge);
        }



        // load the store fragment by default
        toolbar.setTitle(getString(R.string.Home));
    }

    @SuppressLint("RestrictedApi")
    static void removeShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("ERROR NO SUCH FIELD", "Unable to get shift mode field");
        } catch (IllegalAccessException e) {
            Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode");
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment=null;

            switch (item.getItemId()) {
                case R.id.Home:
                    toolbar.setTitle(getString(R.string.Home));
                    fragment = new HomeFragment();
                    break;

                case R.id.imieipassaggi:
                    toolbar.setTitle(getString(R.string.ImieiPassaggi));
                    if(cittadino.getNotificaPassaggio()!=0){
                        BottomNavigationMenuView bottomNavigationMenuView =
                                (BottomNavigationMenuView) navigation.getChildAt(0);
                        BottomNavigationItemView iMieiPassaggi=(BottomNavigationItemView)bottomNavigationMenuView.getChildAt(3);
                        iMieiPassaggi.removeView(notificationBadge);
                        cittadino.setNotificaPassaggio(0);
                        Database.azzeraNotifichePassaggi( cittadino.getIdCittadino(),menu.this);
                    }
                    fragment=new ImieipassaggiFragment();
                    break;

                case R.id.Classifica:
                    toolbar.setTitle(getString(R.string.VisualizzaClassifica));
                    fragment = new ClassificaFragment();
                    break;

                case R.id.IlMioProfilo:
                    toolbar.setTitle(getString(R.string.IlMioProfilo));
                    fragment = new ProfiloFragment();
                    break;
                case R.id.Tracking:
                    toolbar.setTitle("Tracking");
                    fragment = new TrackingFragment();
                    break;

            }

            return loadFragment(fragment);

        }
    };

    /**
     * loading fragment into FrameLayout
     *
     * @param fragment
     */
    public boolean loadFragment(Fragment fragment) {
        // load fragment
        if(fragment!=null){
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_container, fragment);
            transaction.addToBackStack("tag");
            transaction.commit();
            return true;
        }else
            return false;

    }


    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.menu_action_bar,menu);
        if(cittadino.getTipoCittadino().equals("Cittadino")){
            menu.getItem(0).setVisible(false);
        }else{
            final MenuItem item=menu.findItem(R.id.userRequest);
            View actionView = MenuItemCompat.getActionView(item);
            if(cittadino.getNotificaAutorizzazione()==0){

                ImageView notifica=actionView.findViewById(R.id.badge);
                notifica.setVisibility(View.GONE);
            }
            actionView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOptionsItemSelected(item);
                }
            });
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){
            case R.id.logout:
                logout();
                return false;
            case R.id.userRequest:
                if(cittadino.getNotificaAutorizzazione()==1){
                    View actionView = MenuItemCompat.getActionView(item);
                    ImageView notifica=actionView.findViewById(R.id.badge);
                    notifica.setVisibility(View.GONE);
                    cittadino.setNotificaAutorizzazione(0);
                    Database.azzeraNotificheAutorizzazioni(cittadino.getIdCittadino(),this);
                }

                Intent intent=new Intent(menu.this, ListaUtenti.class);
                intent.putExtra(Sede.Keys.IDSEDE,cittadino.getSede().getIdSede());
                startActivity(intent);
                return false;

        }
        return true;

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Cittadino.Keys.IDCITTADINO,cittadino);
        outState.putInt("SelectedItemId",navigation.getSelectedItemId());
    }

    public Cittadino getCittadino() {
        return cittadino;
    }

    public void setCittadino(Cittadino c){
        cittadino=c;
    }

    public void logout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.LogoutTitolo));
        builder.setMessage(getString(R.string.LogoutTesto));
        builder.setPositiveButton(getString(R.string.si), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               Intent i=new Intent(menu.this, LoginActivity.class);
               i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               startActivity(i);


            }
        });
        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog= builder.create();
        alertDialog.show();
    }
    @Override
    public void onBackPressed() {
        logout();
    }




}
