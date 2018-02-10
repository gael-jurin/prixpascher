package org.nuvola.mobile.prixpascher;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.github.florent37.tutoshowcase.TutoShowcase;

import org.nuvola.mobile.prixpascher.business.Utils;
import org.nuvola.mobile.prixpascher.fragments.NotifDevisFragment;
import org.nuvola.mobile.prixpascher.fragments.NotifOffersFragment;
import org.nuvola.mobile.prixpascher.fragments.NotifPromosFragment;
import org.nuvola.mobile.prixpascher.receivers.NetworkStateReceiver.ConnectivityReceiverListener;

import java.util.ArrayList;
import java.util.List;

public class InNotificationActivity extends ActionBarParentActivity implements ConnectivityReceiverListener {
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabs;
    private Fragment mCurrentFragment;
    private CoordinatorLayout coordinator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.notifs_tab_layout);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.news_label);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.pager);
        coordinator = findViewById(R.id.coordinator);
        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        setupViewPager(viewPager);

        TutoShowcase.from(this)
                .setContentView(R.layout.notifs_tab_layout)
                .on(coordinator)
                .displaySwipableLeft()
                .delayed(500)
                .animated(true)
                .showOnce("NotifActivityDemo");

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

    @Override
    protected void onResume() {
        super.onResume();

        Utils.registerNetworkContext(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Utils.unregisterNetworkContext(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Utils.showSnack(coordinator, isConnected);
        if (!isConnected) {
            mCurrentFragment.onStop();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(NotifPromosFragment.newInstance(), "Promos-Deals");
        adapter.addFrag(NotifDevisFragment.newInstance(), "Devis-Achat");
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
