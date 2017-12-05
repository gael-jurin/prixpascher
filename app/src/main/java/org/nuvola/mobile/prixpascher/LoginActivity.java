package org.nuvola.mobile.prixpascher;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFlat;

import org.nuvola.mobile.prixpascher.business.Utils;

public class LoginActivity extends ActionBarParentActivity {
	ProgressDialog dialogPrg;
	String TAG = "LoginActivity";
	TextView email;
	TextView pwd;
	ButtonFlat btnLogin;
    Toolbar toolbar;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

		btnLogin = (ButtonFlat) findViewById(R.id.btn_login);
		email = (TextView) findViewById(R.id.email);
		pwd = (TextView) findViewById(R.id.pwd);
		dialogPrg = new ProgressDialog(this);
		dialogPrg.setCanceledOnTouchOutside(false);
		btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!Utils.isConnectingToInternet(LoginActivity.this)) {
					Toast ts = Toast.makeText(LoginActivity.this,
							getResources().getString(R.string.open_network),
							Toast.LENGTH_LONG);
					ts.show();
				} else {
					dialogPrg.setMessage(getResources().getString(
							R.string.loging));
					dialogPrg.show();
					// new Login().start();
				}
			}
		});
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	public void showDialogFailedLogin() {
		Utils.logout(LoginActivity.this);
        showDialog(getResources().getString(R.string.login_failed));
	}

	/*private class Login extends Thread {
		@Override
		public void run() {
			super.run();
			try {
				String handlerUrl = getResources().getString(
						R.string.users_json_url);

				final String response_str = "connected"; // EntityUtils.toString(resEntity);
				if (1 == 1) {
					Log.i(TAG, response_str);
					runOnUiThread(new Runnable() {
						public void run() {
							dialogPrg.dismiss();
							try {
								JSONArray jsonArray = new JSONArray(
										response_str);
								if (jsonArray.length() == 1) {
									JSONObject obj = jsonArray.getJSONObject(0);
									User user = Utils.parseUser(obj);
									UserSessionManager userSession = new UserSessionManager(
											LoginActivity.this);
									userSession.storeUserSession(user);

                                    // Register merchant on cloud
                                    registerForNotification(user.getFbId());
								}


								Intent intent = new Intent(LoginActivity.this,
										HomeActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
										| Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(intent);
							} catch (Exception e) {
								showDialogFailedLogin();
							}
						}
					});
				}
			} catch (Exception e) {
				showDialogFailedLogin();
			}
		}
	}*/
}
