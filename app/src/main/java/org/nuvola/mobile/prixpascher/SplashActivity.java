package org.nuvola.mobile.prixpascher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

import org.nuvola.mobile.prixpascher.business.BadgeUtils;
import org.nuvola.mobile.prixpascher.confs.constants;

import java.util.HashSet;

import io.cloudboost.CloudApp;

import static org.nuvola.mobile.prixpascher.business.UserSessionManager.PRIVATE_MODE;
import static org.nuvola.mobile.prixpascher.business.UserSessionManager.SHARED_PREF_DATA;

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

		/*if (getIntent().getExtras() != null) {
            getIntent().getExtras().clear();
        }*/

		// TODO: The badge will be cleared once the notifications has expired
		SharedPreferences sharePre = getApplicationContext().getSharedPreferences(
				SHARED_PREF_DATA, PRIVATE_MODE);

		try {
			int counter = sharePre.getInt("counter", 0);

			if (counter == 0) {
				//Init Device
				BadgeUtils.clearBadge(this);
				SharedPreferences.Editor editor = sharePre.edit();
				BadgeUtils.promos = new HashSet<>();
				editor.putStringSet("PROMOS", BadgeUtils.promos);
				BadgeUtils.devis = new HashSet<>();
				editor.putStringSet("DEVIS", BadgeUtils.devis);
				BadgeUtils.offers = new HashSet<>();
				editor.putStringSet("OFFERS", BadgeUtils.offers);
				editor.commit();
			}

			int RunEvery = 10;

			if(counter != 0  && counter % RunEvery == 0 )
			{
				AlertDialog.Builder alert = new AlertDialog.Builder(
						SplashActivity.this);
				alert.setTitle("Evaluer PPC !");
				alert.setIcon(R.drawable.ic_logo);
				alert.setMessage("Merci de prendre un moment pour noter PrixPasCher.");

				alert.setPositiveButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
												int whichButton) {
							}
						});

				alert.setNegativeButton("Rate it",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int which) {

								final String appName = getApplicationContext().getPackageName();
								try {
									startActivity(new Intent(Intent.ACTION_VIEW,
											Uri.parse("market://details?id="
													+ appName)));
								} catch (android.content.ActivityNotFoundException anfe) {
									startActivity(new Intent(
											Intent.ACTION_VIEW,
											Uri.parse("http://play.google.com/store/apps/details?id="
													+ appName)));
								}

							}
						});
				alert.show();
			}

			SharedPreferences.Editor editor = sharePre.edit();
			editor.putInt("counter", ++counter);
			editor.commit();

		} catch (Exception e) {
		}
	}
}
