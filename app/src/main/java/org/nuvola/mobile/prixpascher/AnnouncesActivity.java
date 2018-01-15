package org.nuvola.mobile.prixpascher;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import org.nuvola.mobile.prixpascher.business.Utils;
import org.nuvola.mobile.prixpascher.fragments.AnnouncesFragment;
import org.nuvola.mobile.prixpascher.receivers.NetworkStateReceiver.ConnectivityReceiverListener;

public class AnnouncesActivity extends ActionBarParentActivity implements ConnectivityReceiverListener {
    private Toolbar toolbar;
    private LinearLayout coordinator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.frame_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        coordinator = findViewById(R.id.coordinator);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            AnnouncesFragment fragment = AnnouncesFragment.newInstance(toolbar);
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
