package org.nuvola.mobile.prixpascher;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFlat;

import org.nuvola.mobile.prixpascher.business.Utils;

@SuppressLint({ "NewApi", "ShowToast" })
public class ChangePassActivity extends ActionBarParentActivity {
	ButtonFlat btnUpdate;
	TextView oldPass, newPass, cfmPass;
	ProgressDialog dialogPrg;
    Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_pass_layout);

		oldPass = (TextView) findViewById(R.id.old_pass);
		newPass = (TextView) findViewById(R.id.new_pass);
		cfmPass = (TextView) findViewById(R.id.cfm_pass);

		btnUpdate = (ButtonFlat) findViewById(R.id.btn_update);
		btnUpdate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!Utils.isConnectingToInternet(ChangePassActivity.this)) {
					Toast ts = Toast.makeText(ChangePassActivity.this,
							getResources().getString(R.string.open_network),
							Toast.LENGTH_LONG);
					ts.show();
				} else {
					if (newPass.getText().toString()
							.equals(cfmPass.getText().toString())) {
						/*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
							new UploadTask(UploadType.CHANGE_PASS)
									.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
						} else {
							new UploadTask(UploadType.CHANGE_PASS).execute();
						}*/
						Utils.showCommingSoon(ChangePassActivity.this);
					} else {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								ChangePassActivity.this);
						builder.setMessage(
								getResources().getString(
										R.string.confirm_pass_alert))
								.setPositiveButton(
										getResources().getString(
												R.string.ok_label),
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												// TODO Auto-generated method
												// stub

											}
										});
						AlertDialog dialog = builder.create();
						dialog.show();
						showDialog(getResources().getString(R.string.confirm_pass_alert));
					}
				}
			}
		});
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.change_pass));
        setSupportActionBar(toolbar);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
}
