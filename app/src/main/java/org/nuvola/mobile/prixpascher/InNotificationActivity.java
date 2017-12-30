package org.nuvola.mobile.prixpascher;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import org.nuvola.mobile.prixpascher.fragments.NotifDevisFragment;
import org.nuvola.mobile.prixpascher.fragments.NotifOffersFragment;
import org.nuvola.mobile.prixpascher.fragments.NotifPromosFragment;

import java.util.ArrayList;
import java.util.List;

public class InNotificationActivity extends ActionBarParentActivity {
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabs;
    private Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.notifs_tab_layout);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.news_label);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.pager);
        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        setupViewPager(viewPager);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(NotifPromosFragment.newInstance(), "Promos-Deals");
        adapter.addFrag(NotifDevisFragment.newInstance(), "Devis-Client");
        adapter.addFrag(NotifOffersFragment.newInstance(), "Offres-Vente");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            mCurrentFragment = mFragmentList.get(position);
            return mCurrentFragment;
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        public Fragment getCurrentFragment() {
            return mCurrentFragment;
        }
    }
}
