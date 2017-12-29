package org.nuvola.mobile.prixpascher;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(NotifPromosFragment.newInstance(), "Promos-Deals");
        adapter.addFrag(NotifDevisFragment.newInstance(), "Devis-Achats");
        adapter.addFrag(NotifOffersFragment.newInstance(), "Offres-Devis");
        viewPager.setAdapter(adapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
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

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
