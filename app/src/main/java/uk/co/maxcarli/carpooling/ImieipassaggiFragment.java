package uk.co.maxcarli.carpooling;


import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImieipassaggiFragment extends Fragment {

    private ViewPager mViewPager;
    private ImieipassaggiFragment.SectionsPagerAdapter mSectionsPagerAdapter;

    public ImieipassaggiFragment() {
        // Required empty public constructor
    }

    public static ImieipassaggiFragment newInstance(String param1, String param2) {
        ImieipassaggiFragment fragment = new ImieipassaggiFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment




        mSectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());

        View RootView = inflater.inflate(R.layout.fragment_imieipassaggi, container, false);
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) RootView.findViewById(R.id.viewPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) RootView.findViewById(R.id.tab);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        return RootView;
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

                    return new PassaggiRichiesti();
                case 1:
                    return new PassaggiOfferti();
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
