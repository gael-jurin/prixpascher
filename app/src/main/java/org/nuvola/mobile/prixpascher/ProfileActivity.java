package org.nuvola.mobile.prixpascher;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.nuvola.mobile.prixpascher.business.JSONFetchTask;
import org.nuvola.mobile.prixpascher.business.RoundedAvatarDrawable;
import org.nuvola.mobile.prixpascher.business.Utils;
import org.nuvola.mobile.prixpascher.confs.constants;
import org.nuvola.mobile.prixpascher.models.User;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class ProfileActivity extends ActionBarParentActivity {
	ProgressDialog dialogPrg;
	public static String TAG = "ProfileActivity";
	// TextView displayName,
	TextView userName, website, address, phone;
	ImageView avt;
	Button btnShowItem;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_layout);
		dialogPrg = new ProgressDialog(this);
		dialogPrg.setCanceledOnTouchOutside(false);
		String user_id = getIntent().getStringExtra(constants.COMMON_KEY);
		Log.i(TAG, "user id la" + user_id);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			new JSONFetchTask(getResources().getString(R.string.users_json_url)
					+ "user/id/" + user_id, handler)
					.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			new JSONFetchTask(getResources().getString(R.string.users_json_url)
					+ "user/id/" + user_id, handler).execute();
		}
		dialogPrg.show();
		// displayName = (TextView) findViewById(R.id.display_name);
		userName = (TextView) findViewById(R.id.user_name);
		address = (TextView) findViewById(R.id.address);
		phone = (TextView) findViewById(R.id.phone);
		website = (TextView) findViewById(R.id.websites);
		avt = (ImageView) findViewById(R.id.avt);
		btnShowItem = (Button) findViewById(R.id.btn_show_my_item);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		changeActionBarTitle(getResources().getString(R.string.profile_label));
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Bundle bundle = msg.getData();
			if (bundle.containsKey(JSONFetchTask.KEY_RESPONSE)) {
				String jsonString = bundle
						.getString(JSONFetchTask.KEY_RESPONSE);
				Log.i(TAG, "skadj");
				try {
					JSONArray jsonArray = new JSONArray(jsonString);
					if (jsonArray.length() == 1) {
						JSONObject obj = jsonArray.getJSONObject(0);
						final User user = Utils.parseUser(obj);
						if (user != null) {
							// displayName.setText("aksldjl");
							userName.setText(user.getUserName());
							address.setText(user.getAddress());
							phone.setText(user.getPhone());
							website.setText(user.getWebsite());
							btnShowItem
									.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub
											Intent intent = new Intent(
													ProfileActivity.this,
													ProductsActivity.class);
											intent.putExtra(
													constants.USER_POST_KEY,
													user.getId());
											startActivity(intent);
										}
									});
							if (user.getAvt() != null
									&& !user.getAvt().equalsIgnoreCase("")) {
								Log.i(TAG, "Khac null");
								String avtString = "";
								if (Utils.checkFacebookAvt(user.getAvt())) {
									avtString = user.getAvt();
								} else {
									avtString = getResources().getString(
											R.string.domain_url)
											+ user.getAvt();
								}
								Ion.with(ProfileActivity.this, avtString)
										.withBitmap()
										.resize(200, 200)
										.centerCrop()
										.placeholder(R.drawable.ic_small_avatar)
										.error(R.drawable.ic_small_avatar)
										.asBitmap()
										.setCallback(
												new FutureCallback<Bitmap>() {

													@Override
													public void onCompleted(
															Exception arg0,
															Bitmap bitmap) {
														// TODO Auto-generated
														// method stub
														if (bitmap != null) {
															RoundedAvatarDrawable avtDrawable = new RoundedAvatarDrawable(
																	bitmap);
															avt.setImageDrawable(avtDrawable);
														}
													}

												});

							}
						}
					}
					dialogPrg.cancel();
				} catch (Exception e) {
					Log.e(TAG, "error parse");
					// TODO: handle exception
				}
			}
		};
	};
}
