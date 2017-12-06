package org.nuvola.mobile.prixpascher;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.nuvola.mobile.prixpascher.business.RoundedAvatarDrawable;
import org.nuvola.mobile.prixpascher.business.Utils;
import org.nuvola.mobile.prixpascher.confs.constants;
import org.nuvola.mobile.prixpascher.dto.MerchantVO;
import org.nuvola.mobile.prixpascher.dto.UserVO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class ProfileActivity extends ActionBarParentActivity {
	ProgressDialog dialogPrg;
	public static String TAG = "ProfileActivity";
	// TextView displayName,
	TextView userName, website, address, productFeed;
	ImageView avt;
	Button btnShowItem;
	private MerchantVO merchant;
	private String user_id;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_layout);
		dialogPrg = new ProgressDialog(this);
		dialogPrg.setCanceledOnTouchOutside(false);
		user_id = getIntent().getStringExtra(constants.COMMON_KEY);
		Log.i(TAG, "user id la" + user_id);

		dialogPrg.show();
		// displayName = (TextView) findViewById(R.id.display_name);
		userName = (TextView) findViewById(R.id.user_name);
		address = (TextView) findViewById(R.id.address);
		productFeed = (TextView) findViewById(R.id.phone);
		website = (TextView) findViewById(R.id.websites);
		avt = (ImageView) findViewById(R.id.avt);
		btnShowItem = (Button) findViewById(R.id.btn_show_my_item);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		changeActionBarTitle(getResources().getString(R.string.profile_label));

		new LoadUserDataTask().execute();
	}

	private class LoadUserDataTask extends AsyncTask<String, Void, MerchantVO> {

		@Override
		protected MerchantVO doInBackground(String... params) {
			if (isCancelled()) {
				return null;
			}
			return getMerchant();
		}

		@Override
		protected void onPostExecute(MerchantVO result) {
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
		}

		@Override
		protected void onCancelled() {

		}
	}

	private MerchantVO getMerchant() {
		try {
			HttpHeaders requestHeaders = new HttpHeaders();
			requestHeaders.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity requestEntity = new HttpEntity(requestHeaders);

			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			final ResponseEntity<MerchantVO> merchantInfos = restTemplate.exchange(
					getResources().getString(R.string.merchant_json_url) + "/name/" + user_id,
					HttpMethod.GET,
					requestEntity, MerchantVO.class);
			if (merchantInfos.getStatusCode().equals(HttpStatus.OK)) {
                merchant = merchantInfos.getBody();
				// displayName.setText("aksldjl");
				userName.setText(merchant.getShopInfoVO().getPrimaryAccountUserName());
				address.setText("");
				productFeed.setText(merchant.getShopInfoVO().getFeed());
				website.setText(merchant.getShopInfoVO().getWebsite());

				final ResponseEntity<UserVO> user = restTemplate.exchange(
						getResources().getString(R.string.users_json_url) + "/" +
                                merchant.getShopInfoVO().getPrimaryAccountEmail(),
						HttpMethod.GET,
						requestEntity, UserVO.class);

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
										user.getBody().getPrimaryShopClasse().name());
								startActivity(intent);
							}
						});

				if (user.getStatusCode().equals(HttpStatus.OK)
						&& !user.getBody().getPhoto().equalsIgnoreCase("")) {
					String avtString = "";
					if (Utils.checkFacebookAvt(user.getBody().getPhoto())) {
						avtString = user.getBody().getPhoto();
					} else {
						avtString = user.getBody().getPhoto();
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
			dialogPrg.cancel();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return merchant;
	}
}
