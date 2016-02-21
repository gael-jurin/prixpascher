package org.nuvola.mobile.prixpascher;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.gc.materialdesign.views.ButtonRectangle;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.nuvola.mobile.prixpascher.business.UserSessionManager;
import org.nuvola.mobile.prixpascher.business.Utils;
import org.nuvola.mobile.prixpascher.models.User;

@SuppressLint({ "NewApi", "ShowToast" })
public class ChangePassActivity extends ActionBarParentActivity {
	ButtonRectangle btnUpdate;
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

		btnUpdate = (ButtonRectangle) findViewById(R.id.btn_update);
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
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
							new Upload()
									.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
						} else {
							new Upload().execute();
						}
					} else {
					/*	AlertDialog.Builder builder = new AlertDialog.Builder(
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
						dialog.show();*/
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

	private class Upload extends AsyncTask<Void, Void, Boolean> {
		public Upload() {
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			dialogPrg.dismiss();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialogPrg = new ProgressDialog(ChangePassActivity.this);
			dialogPrg.setCanceledOnTouchOutside(false);
			dialogPrg.setMessage(getResources().getString(R.string.loading));
			dialogPrg.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String handleInserUrl = getResources().getString(
					R.string.users_json_url)
					+ "pwd";
			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(handleInserUrl);
				MultipartEntity reqEntity = new MultipartEntity();

				UserSessionManager userSessionManager = new UserSessionManager(
						ChangePassActivity.this);
				User user = userSessionManager.getUserSession();
				String oldPassText = oldPass.getText().toString();
				String newPassText = newPass.getText().toString();
				// String cfmPassText=cfmPass.getText().toString();
				/*reqEntity.addPart("id", new StringBody(user.getId() + ""));
				reqEntity.addPart("old_pass", new StringBody(oldPassText));
				reqEntity.addPart("new_pass", new StringBody(newPassText));*/
				post.setEntity(reqEntity);
				HttpResponse response = client.execute(post);
				HttpEntity resEntity = response.getEntity();
				final String jsonString = EntityUtils.toString(resEntity);

				final AlertDialog.Builder builder = new AlertDialog.Builder(
						ChangePassActivity.this);
				builder.setPositiveButton(
						getResources().getString(R.string.ok_label),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

							}
						});
				if (resEntity != null) {
					runOnUiThread(new Runnable() {
						public void run() {
							try {
								dialogPrg.dismiss();
								JSONObject jsonObj = new JSONObject(jsonString);
								if (jsonObj.getString("ok").equals("1")) {
						/*			builder.setMessage(getResources()
											.getString(
													R.string.update_success_label));
									AlertDialog dialog = builder.create();
									dialog.show();*/

                                    showDialog(	getResources()
                                            .getString(
                                                    R.string.update_success_label));
								} else {
								/*	builder.setMessage(getResources()
											.getString(
													R.string.update_failed_label));
									AlertDialog dialog = builder.create();
									dialog.show();*/

                                    showDialog(getResources()
                                            .getString(
                                                    R.string.update_failed_label));
								}

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}
			} catch (Exception ex) {
				Log.e("Debug", "error: " + ex.getMessage(), ex);
			}
			return null;
		}
	}
}
