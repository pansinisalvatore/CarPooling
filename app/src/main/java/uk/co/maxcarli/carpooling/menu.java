package uk.co.maxcarli.carpooling;


        import android.annotation.SuppressLint;
        import android.content.Intent;
        import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.design.internal.BottomNavigationItemView;
        import android.support.design.internal.BottomNavigationMenuView;
        import android.support.design.widget.BottomNavigationView;
        import android.support.design.widget.CoordinatorLayout;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentTransaction;
        import android.support.v7.app.ActionBar;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.MenuItem;
        import android.widget.Toast;

        import java.lang.reflect.Field;

        import uk.co.maxcarli.carpooling.Fragment.ClassificaFragment;
        import uk.co.maxcarli.carpooling.Fragment.HomeFragment;
        import uk.co.maxcarli.carpooling.Fragment.ImieipassaggiFragment;
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

        removeShiftMode(navigation);

        // load the store fragment by default
        toolbar.setTitle("Home");
        loadFragment(new HomeFragment());
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Cittadino.Keys.IDCITTADINO,cittadino);
    }
}
