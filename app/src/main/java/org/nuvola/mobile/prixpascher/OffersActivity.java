package org.nuvola.mobile.prixpascher;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import org.nuvola.mobile.prixpascher.business.Utils;
import org.nuvola.mobile.prixpascher.fragments.OffersFragment;
import org.nuvola.mobile.prixpascher.receivers.NetworkStateReceiver.ConnectivityReceiverListener;

public class OffersActivity extends ActionBarParentActivity implements ConnectivityReceiverListener {
    private Toolbar toolbar;
    private LinearLayout coordinator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.frame_layout);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.Oventes);
        setSupportActionBar(toolbar);

        coordinator = findViewById(R.id.coordinator);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            OffersFragment fragment = OffersFragment.newInstance(toolbar);
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment).commit();
        }

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
    }
}
