package com.rider.myride.Myrides;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.rider.myride.R;
import com.rider.myride.model.RidePOJO;

import java.util.ArrayList;
import java.util.List;

public class MyRideTabFragmnt extends Fragment {

    RidePOJO ridePOJOArrayList;


    public MyRideTabFragmnt(RidePOJO ridePOJOArrayList) {
        this.ridePOJOArrayList = ridePOJOArrayList;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.my_ride_tab, container, false);

        ViewPager viewPager = rootView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        TabLayout tabs = rootView.findViewById(R.id.result_tabs);
        tabs.setupWithViewPager(viewPager);


        return rootView;

    }

    private void setupViewPager(ViewPager viewPager) {


        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new DetailsTab(ridePOJOArrayList), "Sheduled");
        adapter.addFragment(new Future(ridePOJOArrayList), "Upcoming");
        adapter.addFragment(new Past(ridePOJOArrayList), "Past");

        viewPager.setAdapter(adapter);


    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
