package org.nuvola.mobile.prixpascher;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.nuvola.mobile.prixpascher.business.Utils;
import org.nuvola.mobile.prixpascher.confs.constants;
import org.nuvola.mobile.prixpascher.dto.MerchantVO;
import org.nuvola.mobile.prixpascher.dto.UserVO;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends ActionBarParentActivity {
	ProgressDialog dialogPrg;

	public static String TAG = "ProfileActivity";
	// @BindView(R.id.display_name) TextView displayName,
	@BindView(R.id.user_name) TextView userName;
	@BindView(R.id.websites) TextView website;
	@BindView(R.id.address) TextView address;
	@BindView(R.id.product_feed) TextView productFeed;
	@BindView(R.id.avt) ImageView avt;
	@BindView(R.id.btn_show_my_item) Button btnShowItem;
	@BindView(R.id.btn_show_my_item2) Button btnShowItem2;

	private MerchantVO merchant;
	private String user_id;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_layout);
		ButterKnife.bind(this);

		dialogPrg = new ProgressDialog(this);
		dialogPrg.setCanceledOnTouchOutside(false);
		user_id = getIntent().getStringExtra(constants.COMMON_KEY);
		Log.i(TAG, "user id la" + user_id);

		dialogPrg.show();

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

			final ResponseEntity<MerchantVO> merchantInfos = Utils.MyRestemplate.getInstance(this)
                    .exchange(getResources().getString(R.string.merchant_json_url) + user_id,
					HttpMethod.GET, null,
					MerchantVO.class);
			if (merchantInfos.getStatusCode().equals(HttpStatus.OK)) {
                merchant = merchantInfos.getBody();
				// displayName.setText("aksldjl");
				userName.setText(merchant.getShopInfoVO().getPrimaryAccountUserName());
				address.setText("");
				productFeed.setText(merchant.getShopInfoVO().getFeed());
				website.setText(merchant.getShopInfoVO().getWebsite());

				final ResponseEntity<UserVO> user = Utils.MyRestemplate.getInstance(this)
                        .exchange(
						getResources().getString(R.string.users_json_url) + "/merchant/" +
                                user_id, HttpMethod.GET, null, UserVO.class);

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

                Utils.MyPicasso.with(ProfileActivity.this)
                        .load(user.getBody().getPhoto())
                        .resize(200, 200).centerCrop()
                        .placeholder(R.drawable.ic_small_avatar)
                        .error(R.drawable.ic_small_avatar)
                        .into(avt);
			}
			dialogPrg.cancel();
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
		return merchant;
	}
}
