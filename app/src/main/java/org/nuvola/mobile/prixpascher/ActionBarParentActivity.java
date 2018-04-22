package org.nuvola.mobile.prixpascher;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.widgets.Dialog;
import com.mikepenz.iconics.context.IconicsLayoutInflater2;

import org.nuvola.mobile.prixpascher.business.BadgeUtils;
import org.nuvola.mobile.prixpascher.receivers.NotificationFiredReceiver;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.nuvola.mobile.prixpascher.business.UserSessionManager.PRIVATE_MODE;
import static org.nuvola.mobile.prixpascher.business.UserSessionManager.SHARED_PREF_DATA;

public class ActionBarParentActivity extends AppCompatActivity implements NotificationFiredReceiver.NotificationFiredListener {
	private LinkedHashSet<Integer> enableItems = new LinkedHashSet<>();
	private LinkedHashSet<Integer> disableItems = new LinkedHashSet<>();
	private Iterator<Integer> iter;
    private TextView alertCount;
    private SharedPreferences sharePre;

    public void setEnableItem(LinkedHashSet<Integer> items) {
		this.enableItems = items;
	}

	public void setDisableItem(LinkedHashSet<Integer> items) {
		this.disableItems = items;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));

        sharePre = getApplicationContext().getSharedPreferences(
                SHARED_PREF_DATA, PRIVATE_MODE);
		super.onCreate(savedInstanceState);

		/*TutoShowcase.from(this)
				.setFitsSystemWindows(true)
				.setContentView(R.layout.action_bar_notification)

                .on(R.id.hotlist_bell)
                .addCircle()
                .withBorder()
                .onClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                })
				//.showOnce("SearchFilterDemo")
				.show();*/
	}

	@Override
	protected void onResume() {
		super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(new NotificationFiredReceiver(),
                new IntentFilter("inbox"));

		MarketApp.getInstance().setNotificationFiredListener(this);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (!disableItems.isEmpty()) {
			iter = disableItems.iterator();
			while (iter.hasNext()) {
				MenuItem item = menu.findItem(iter.next());
				item.setVisible(false);
			}
		}

		if (!enableItems.isEmpty()) {
			iter = enableItems.iterator();
			while (iter.hasNext()) {
				MenuItem item = menu.findItem(iter.next());
				item.setVisible(true);
			}
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.btn_action_search).getActionView();
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        final MenuItem menuItem = menu.findItem(R.id.action_alert);
        menuItem.setActionView(R.layout.action_bar_notification);

        final View alertBadge = menuItem.getActionView();
        ImageView alertIcon = alertBadge.findViewById(R.id.hotlist_bell);

        alertCount = alertBadge.findViewById(R.id.badge_textView);

        Set<String> notifs = sharePre.getStringSet("PROMOS", new HashSet<String>());
        Set<String> devis = sharePre.getStringSet("DEVIS", new HashSet<String>());
        Set<String> offers = sharePre.getStringSet("OFFERS", new HashSet<String>());

        updateHotCount(notifs.size() + devis.size() + offers.size());

		alertCount.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getApplicationContext(), InNotificationActivity.class);
				startActivity(intent);
			}
		});

		alertIcon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (BadgeUtils.counter > 0) {
					Intent intent = new Intent(getApplicationContext(), InNotificationActivity.class);
					startActivity(intent);
				}
			}
		});

        return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
            case android.R.id.home:
                // NavUtils.navigateUpTo(this, new Intent(this,
                // HomeActivity.class));
                finish();
                break;

		/*case R.id.btn_action_upload:
			if (!Utils.isConnectingToInternet(this)) {
				showMsg(getResources().getString(R.string.open_network));
				return false;
			}
			UserSessionManager userSession = new UserSessionManager(this);
			if (userSession.getUserSession() != null) {
				Intent intent = new Intent(this, UploadActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent(this, AuthenticationActivity.class);
				startActivity(intent);
			}
			break; */

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void refreshActionBarMenu() {
		this.supportInvalidateOptionsMenu();
	}

	public void changeActionBarTitle(String title) {
		getSupportActionBar().setTitle(title);
	}

	public void showMsg(String msg) {
		Toast ts = Toast.makeText(this, msg, Toast.LENGTH_LONG);
		ts.show();
	}

	public void showDialog(String msg) {
        Dialog dialog = new Dialog(ActionBarParentActivity.this,getResources().getString(R.string.alert),msg);
        dialog.show();
	}

	public void loadAd() {
		/*AdView adView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.addTestDevice(
						getResources()
								.getString(R.string.admob_test_device_ids))
				.build();
		adView.loadAd(adRequest);*/
	}

    public void updateHotCount(final int new_hot_number) {
        if (alertCount == null) {
			BadgeUtils.setBadge(getApplicationContext(), 0);
			return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
				BadgeUtils.setBadge(getApplicationContext(), new_hot_number);
				if (new_hot_number == 0) {
					alertCount.setVisibility(View.INVISIBLE);
					alertCount.setText("");
				} else {
                    alertCount.setVisibility(View.VISIBLE);
                    alertCount.setText(Integer.toString(new_hot_number));
                }
            }
        });
    }

    @Override
    public void onNotificationFired() {
        Set<String> notifs = sharePre.getStringSet("PROMOS", new HashSet<String>());
        Set<String> devis = sharePre.getStringSet("DEVIS", new HashSet<String>());
        Set<String> offers = sharePre.getStringSet("OFFERS", new HashSet<String>());

        updateHotCount(notifs.size() + devis.size() + offers.size());
    }
}
