package org.nuvola.mobile.prixpascher;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import org.nuvola.mobile.prixpascher.business.Utils;
import org.nuvola.mobile.prixpascher.fragments.NetworkErrorFragment;
import org.nuvola.mobile.prixpascher.fragments.ProductsFragment;

public class ProductsActivity extends ActionBarParentActivity {
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.frame_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && Utils.isConnectingToInternet(this)) {
            ProductsFragment fragment = ProductsFragment.newInstance(toolbar);
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment).commit();
        } else {
            NetworkErrorFragment fragment = NetworkErrorFragment.newInstance(toolbar);
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment).commit();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
}
