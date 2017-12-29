package org.nuvola.mobile.prixpascher;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import org.nuvola.mobile.prixpascher.fragments.AnnouncesFragment;

public class AnnouncesActivity extends ActionBarParentActivity {
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.frame_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
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
}
