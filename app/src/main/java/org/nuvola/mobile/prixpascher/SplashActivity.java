package org.nuvola.mobile.prixpascher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

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
		mInformationTextView = (TextView) findViewById(R.id.informationTextView);
	}
}
