package org.nuvola.mobile.prixpascher;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import org.nuvola.mobile.prixpascher.fragments.AuthenticationFragment;

public class AuthenticationActivity extends ActionBarParentActivity {
   // Toolbar toolbar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.authentication_frame_layout);
		AuthenticationFragment fragment = AuthenticationFragment.newInstance();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, fragment).commit();

/*        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);*/
/*        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);*/
	}
}
