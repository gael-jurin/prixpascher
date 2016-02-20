package org.nuvola.mobile.prixpascher;

/*import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;*/
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
/*import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;*/

import org.nuvola.mobile.prixpascher.business.Utils;
import org.nuvola.mobile.prixpascher.business.UserSessionManager;
import org.nuvola.mobile.prixpascher.models.User;
import com.gc.materialdesign.views.ButtonRectangle;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends ActionBarParentActivity {
	ProgressDialog dialogPrg;
	String TAG = "LoginActivity";
	TextView email;
	TextView pwd;
	ButtonRectangle btnLogin;
    Toolbar toolbar;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

		btnLogin = (ButtonRectangle) findViewById(R.id.btn_login);
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
					new Login().start();
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

	private class Login extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			try {
				String handlerUrl = getResources().getString(
						R.string.users_json_url)
						+ "login";
				//HttpClient client = new DefaultHttpClient();
				//HttpPost post = new HttpPost(handlerUrl);
				MultipartEntity reqEntity = new MultipartEntity();
				String emailText = email.getText().toString();
				String pwdText = pwd.getText().toString();
				// reqEntity.addPart("email", new StringBody(emailText));
				// reqEntity.addPart("pwd", new StringBody(pwdText));
				//post.setEntity(reqEntity);
				//HttpResponse res = client.execute(post);
				//HttpEntity resEntity = res.getEntity();
				final String response_str = "connected"; // EntityUtils.toString(resEntity);
				if (1 == 1) {
					Log.i(TAG, response_str);
					runOnUiThread(new Runnable() {
						public void run() {
							dialogPrg.dismiss();
							try {
								//JSONArray jsonArray = new JSONArray(
								//		response_str);
								/*if (jsonArray.length() == 1) {
									JSONObject obj = jsonArray.getJSONObject(0);
									User user = Utils.parseUser(obj);
									UserSessionManager userSession = new UserSessionManager(
											LoginActivity.this);
									userSession.storeUserSession(user);
								}*/
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
				// TODO: handle exception
				// showDialogFailedLogin();
			}
		}
	}
}
