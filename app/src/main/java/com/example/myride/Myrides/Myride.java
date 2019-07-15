package com.example.myride.Myrides;

import android.content.Context;

import com.example.myride.R;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class Myride extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myride);


        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        tabLayout.addTab(tabLayout.newTab().setText("Sugested"));
        tabLayout.addTab(tabLayout.newTab().setText("Active"));


        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final MyrideAdapter adapter = new MyrideAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public class MyrideAdapter extends FragmentPagerAdapter {

        private Context myContext;
        int totalTabs;

        public MyrideAdapter(Context context, FragmentManager fm, int totalTabs) {
            super(fm);
            myContext = context;
            this.totalTabs = totalTabs;
        }

        // this is for fragment tabs
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FindRideFragment(Myride.this);
                case 1:
                    return new OfferrideFragment(Myride.this);

                default:
                    return null;
            }
        }

        // this counts total number of tabs
        @Override
        public int getCount() {
            return totalTabs;
        }

    }


    private static final String TAG = "Myride";
}
