package org.nuvola.mobile.prixpascher;

import android.os.Bundle;

import org.nuvola.mobile.prixpascher.fragments.AuthenticationFragment;

public class AuthenticationActivity extends ActionBarParentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.authentication_frame_layout);
		AuthenticationFragment fragment = AuthenticationFragment.newInstance();

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, fragment).commit();
	}
}
