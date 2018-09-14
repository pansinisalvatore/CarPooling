package uk.co.maxcarli.carpooling.Fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.co.maxcarli.carpooling.R;


/**
 * ClassificaFragment contiene un tab menù che consente di passare dalla classifica aziendale a quella nazionale e viceversa
 */
public class ClassificaFragment extends Fragment {

    private ViewPager cViewPager;
    private SectionsPagerAdapter SectionsPagerAdapter;


    public ClassificaFragment() {
        // Required empty public constructor
    }

    /**
     * contiene un tab menù che consente di passare dalla classifica aziendale a quella nazionale e vicevers
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        SectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());

        View RootView = inflater.inflate(R.layout.fragment_classifica, container, false);
        // Set up the ViewPager with the sections adapter.
        cViewPager = (ViewPager) RootView.findViewById(R.id.viewPage);
        cViewPager.setAdapter(SectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) RootView.findViewById(R.id.tabClassifica);

        cViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(cViewPager));
        return RootView;
    }


    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

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
