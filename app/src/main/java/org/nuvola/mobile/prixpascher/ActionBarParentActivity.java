package org.nuvola.mobile.prixpascher;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.gc.materialdesign.widgets.Dialog;
import org.nuvola.mobile.prixpascher.business.UserSessionManager;
import org.nuvola.mobile.prixpascher.business.Utils;

import java.util.Iterator;
import java.util.LinkedHashSet;

public class ActionBarParentActivity extends AppCompatActivity {
	private LinkedHashSet<Integer> enableItems = new LinkedHashSet<Integer>();
	private LinkedHashSet<Integer> disableItems = new LinkedHashSet<Integer>();
	private Iterator<Integer> iter;

	public void setEnableItem(LinkedHashSet<Integer> items) {
		this.enableItems = items;
	}

	public void setDisableItem(LinkedHashSet<Integer> items) {
		this.disableItems = items;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu
				.findItem(R.id.btn_action_search));
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
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
}
