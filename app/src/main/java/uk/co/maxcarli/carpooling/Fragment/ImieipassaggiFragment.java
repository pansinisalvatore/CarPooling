package uk.co.maxcarli.carpooling.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import uk.co.maxcarli.carpooling.R;
import uk.co.maxcarli.carpooling.menu;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImieipassaggiFragment extends Fragment implements TabLayout.OnTabSelectedListener{

    private menu menuActivity;




    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;


    public ImieipassaggiFragment() {
        // Required empty public constructor
    }

    public static ImieipassaggiFragment newInstance(String param1, String param2) {
        ImieipassaggiFragment fragment = new ImieipassaggiFragment();
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //


    }


    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View viewRoot= inflater.inflate(R.layout.fragment_imieipassaggi, container,false);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) viewRoot.findViewById(R.id.viewPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) viewRoot.findViewById(R.id.tab);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        return viewRoot;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    public  class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Fragment fragment=null;

            switch(position){
                case 0:
                    Toast.makeText(getContext(),"CREATA",Toast.LENGTH_LONG).show();
                    fragment= new PassaggiRichiesti();


                    break;

                case 1:
                    fragment=new PassaggiOfferti();


                    break;
//
            }
            return fragment;
        }




        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;

         }
    }




}