package uk.co.maxcarli.carpooling;


        import android.content.Intent;
        import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.design.widget.BottomNavigationView;
        import android.support.design.widget.CoordinatorLayout;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentTransaction;
        import android.support.v7.app.ActionBar;
        import android.support.v7.app.AppCompatActivity;
        import android.view.MenuItem;
        import android.widget.Toast;

        import uk.co.maxcarli.carpooling.Fragment.ClassificaFragment;
        import uk.co.maxcarli.carpooling.Fragment.HomeFragment;
        import uk.co.maxcarli.carpooling.Fragment.ImieipassaggiFragment;
        import uk.co.maxcarli.carpooling.Fragment.NotificheFragment;
        import uk.co.maxcarli.carpooling.Fragment.ProfiloFragment;
        import uk.co.maxcarli.carpooling.model.Cittadino;


public class menu extends AppCompatActivity {

    private ActionBar toolbar;
    private Cittadino cittadino;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        cittadino=(Cittadino)getIntent().getParcelableExtra(Cittadino.Keys.IDCITTADINO);
        toolbar = getSupportActionBar();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        // attaching bottom sheet behaviour - hide / show on scroll
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        Toast.makeText(this,cittadino.getNome(),Toast.LENGTH_SHORT).show();
        // load the store fragment by default
        toolbar.setTitle("Home");
        loadFragment(new HomeFragment());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.Home:
                    toolbar.setTitle("Home");
                    fragment = new HomeFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.imieipassaggi:
                    toolbar.setTitle("I miei passaggi");
                    fragment = new ImieipassaggiFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.Classifica:
                    toolbar.setTitle("Classifica");
                    fragment = new ClassificaFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.Notification:
                    toolbar.setTitle("Notifiche");
                    fragment = new NotificheFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.IlMioProfilo:
                    toolbar.setTitle("Il mio profilo");
                    fragment = new ProfiloFragment();
                    loadFragment(fragment);
                    return true;
            }

            return false;
        }
    };

    /**
     * loading fragment into FrameLayout
     *
     * @param fragment
     */
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
