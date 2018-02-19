package org.nuvola.mobile.prixpascher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

import org.nuvola.mobile.prixpascher.confs.constants;

import io.cloudboost.CloudApp;

public class SplashActivity extends Activity {
	private TextView mInformationTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_layout);
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SplashActivity.this,
						HomeActivity.class);
				startActivity(intent);
				finish();
			}
		}, constants.SPLASH_TIME_OUT);

		CloudApp.init("wpcjpusfglac", "72940f58-6bd9-48ef-af68-bdb9a7f7d060");
		mInformationTextView = findViewById(R.id.informationTextView);

		TwitterConfig config = new TwitterConfig.Builder(this)
				.logger(new DefaultLogger(Log.DEBUG))
				.twitterAuthConfig(new TwitterAuthConfig("4wxTqQnKOp8FYVfCJn9xHAXeD",
						"3bmVARzU0pw5TUwk6C1is3G7EcxJl68yeQrluPfUl1xpld9ZLp"))
				.debug(true)
				.build();
		Twitter.initialize(config);

		// TODO: The badge will be cleared once the notifications has expired
		// SharedPreferences sharePre = getContext().getSharedPreferences(
		//		SHARED_PREF_DATA, PRIVATE_MODE);
	}
}
