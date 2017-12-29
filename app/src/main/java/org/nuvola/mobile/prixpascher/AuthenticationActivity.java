package org.nuvola.mobile.prixpascher;

import android.content.Intent;
import android.os.Bundle;

import com.twitter.sdk.android.core.identity.TwitterAuthClient;

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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

        final TwitterAuthClient twitterAuthClient = new TwitterAuthClient();
		if(twitterAuthClient.getRequestCode()==requestCode) {
			twitterAuthClient.onActivityResult(requestCode, resultCode, data);
		}
	}
}
