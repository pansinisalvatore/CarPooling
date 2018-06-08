package uk.co.maxcarli.carpooling;

import android.content.Intent;
import android.opengl.GLException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class Classifica extends  AppCompatActivity {

    private DrawerLayout cDrawerLayout;
    private ActionBarDrawerToggle cToggle;
    private ViewPager cViewPager;
    private SectionsPagerAdapter SectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classifica);
        cDrawerLayout=(DrawerLayout)findViewById(R.id.Classifica);
        cToggle=new ActionBarDrawerToggle(this, cDrawerLayout,  R.string.open, R.string.close);
        cDrawerLayout.addDrawerListener(cToggle);
        cToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        cViewPager = (ViewPager) findViewById(R.id.viewPage);
        cViewPager.setAdapter(SectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabClassifica);

        cViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(cViewPager));

    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if(cToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch(position){
                case 0:

                    return new ClassificaAziendale();
                case 1:
                    return new ClassificaNazionale();
//
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }


}
