package org.nuvola.mobile.prixpascher;

import java.io.File;
import java.util.LinkedHashSet;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;


import org.nuvola.mobile.prixpascher.business.JSONFetchTask;
import org.nuvola.mobile.prixpascher.business.UserSessionManager;
import org.nuvola.mobile.prixpascher.business.Utils;
import org.nuvola.mobile.prixpascher.confs.constants;
import org.nuvola.mobile.prixpascher.models.Categories;
import org.nuvola.mobile.prixpascher.models.County;
import org.nuvola.mobile.prixpascher.models.Products;
import org.nuvola.mobile.prixpascher.models.User;
import com.facebook.Session;
import com.facebook.SessionState;
import com.gc.materialdesign.views.ButtonRectangle;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.NYXDigital.NiceSupportMapFragment;

public class UpdateProductActivity extends ActionBarParentActivity {
	ImageView btnPickPhoto;
	Cursor cursor;
	int currentChooserPhotoId = 0;
	String[] categories_name, county_name, city_name, purpose_id, purpose_name,
			condition_id, condition_name;
	int[] categories_id, county_id, city_id;
	Spinner categoriesSpinner, countySpinner, aimSpinner, citiesSpinner,
			conditionSpinner;
	String TAG = "UploadActivity";
	static final String KEY_CATEGORIES_RESPONSE = "KEY_CATEGORIES_RESPONSE",
			KEY_COUNTY_RESPONSE = "KEY_COUNTY_RESPONSE",
			KEY_CITIES_RESPONSE = "KEY_CITIES_RESPONSE";
	EditText price, title, content;
	int categories_selected = 0, county_selected = 0, city_selected = 0,
			aim_selected = -1, condition_selected = 0, product_id = 0; // default
																		// is
																		// new
	// condition
	String photoPath = null;
	ButtonRectangle btnUpload;
	ProgressDialog dialog;
	File tmpFile;
	CharSequence[] items;
	private GoogleMap gmap;
	Marker gpin = null;
	String lat = "10", lng = "20";
    Toolbar toolbar;

	@SuppressLint("HandlerLeak")
	Handler handler_categories = new Handler() {
		@SuppressLint("NewApi")
		public void handleMessage(android.os.Message msg) {
			Bundle data = msg.getData();
			Log.i(TAG, "Handler");
			if (data.containsKey(KEY_CATEGORIES_RESPONSE)) {
				Log.i(TAG, "Handler");
				String jsonString = data.getString(KEY_CATEGORIES_RESPONSE);
				try {
					dialog = new ProgressDialog(UpdateProductActivity.this);
					dialog.setMessage(getResources().getString(
							R.string.please_wait_msg));
					JSONArray jsonArray = new JSONArray(jsonString);
					categories_id = new int[jsonArray.length()];
					categories_name = new String[jsonArray.length()];
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject categoriesJSON = jsonArray.getJSONObject(i);
						int id = categoriesJSON.getInt(Categories.TAG_ID);
						String name = categoriesJSON
								.getString(Categories.TAG_NAME);
						categories_id[i] = id;
						categories_name[i] = name;
					}

					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							UpdateProductActivity.this,
							android.R.layout.simple_list_item_1,
							categories_name);
					categoriesSpinner.setAdapter(adapter);
					categoriesSpinner
							.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

								@Override
								public void onItemSelected(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									// TODO Auto-generated method stub
									categories_selected = categories_id[arg2];
								}

								@Override
								public void onNothingSelected(
										AdapterView<?> arg0) {
									// TODO Auto-generated method stub

								}

							});

					new JSONFetchTask(getResources().getString(
							R.string.county_json_url)
							+ "county", handler_county, KEY_COUNTY_RESPONSE)
							.execute();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		};
	};

	Handler handler_county = new Handler() {
		@SuppressLint("NewApi")
		public void handleMessage(android.os.Message msg) {
			Bundle data = msg.getData();
			Log.i(TAG, "Handler");
			if (data.containsKey(KEY_COUNTY_RESPONSE)) {
				Log.i(TAG, "Handler");
				String jsonString = data.getString(KEY_COUNTY_RESPONSE);
				try {
					JSONArray jsonArray = new JSONArray(jsonString);
					county_id = new int[jsonArray.length()];
					county_name = new String[jsonArray.length()];
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject countyJSON = jsonArray.getJSONObject(i);
						int id = countyJSON.getInt(County.TAG_ID);
						String name = countyJSON.getString(County.TAG_NAME);
						county_id[i] = id;
						county_name[i] = name;
					}
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							UpdateProductActivity.this,
							android.R.layout.simple_list_item_1, county_name);
					countySpinner.setAdapter(adapter);
					countySpinner
							.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

								@Override
								public void onItemSelected(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									// TODO Auto-generated method stub
									county_selected = county_id[arg2];

									new JSONFetchTask(getResources().getString(
											R.string.cities_json_url)
											+ "cities_by_county_id/id/"
											+ county_selected, handler_cities,
											KEY_CITIES_RESPONSE).execute();
								}

								@Override
								public void onNothingSelected(
										AdapterView<?> arg0) {
									// TODO Auto-generated method stub

								}

							});

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		};
	};

	@SuppressLint("HandlerLeak")
	Handler handler_cities = new Handler() {
		@SuppressLint("NewApi")
		public void handleMessage(android.os.Message msg) {
			Bundle data = msg.getData();
			Log.i(TAG, "Handler");
			if (data.containsKey(KEY_CITIES_RESPONSE)) {
				Log.i(TAG, "Handler");
				String jsonString = data.getString(KEY_CITIES_RESPONSE);
				try {
					JSONArray jsonArray = new JSONArray(jsonString);
					city_id = new int[jsonArray.length()];
					city_name = new String[jsonArray.length()];
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject countyJSON = jsonArray.getJSONObject(i);
						int id = countyJSON.getInt(County.TAG_ID);
						String name = countyJSON.getString(County.TAG_NAME);
						city_id[i] = id;
						city_name[i] = name;
					}
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							UpdateProductActivity.this,
							android.R.layout.simple_list_item_1, city_name);
					citiesSpinner.setAdapter(adapter);
					citiesSpinner
							.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

								@Override
								public void onItemSelected(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									// TODO Auto-generated method stub
									city_selected = city_id[arg2];
								}

								@Override
								public void onNothingSelected(
										AdapterView<?> arg0) {
									// TODO Auto-generated method stub

								}

							});

					new JSONFetchTask(getResources().getString(
							R.string.products_json_url)
							+ "products?product_id=" + product_id,
							handler_products).execute();
				} catch (Exception e) {
					// TODO: handle exception
					city_name = new String[0];
					city_selected = 0;
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							UpdateProductActivity.this,
							android.R.layout.simple_list_item_1, city_name);
					citiesSpinner.setAdapter(adapter);
					e.printStackTrace();
					Log.i("x", e.toString());
				}
			}
		};
	};

	Handler handler_products = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Bundle bundle = msg.getData();
			if (bundle.containsKey(JSONFetchTask.KEY_RESPONSE)) {
				dialog.dismiss();
				String jsonString = bundle
						.getString(JSONFetchTask.KEY_RESPONSE);
				try {
					JSONArray jsonArray = new JSONArray(jsonString);
					if (jsonArray.length() == 1) {
						JSONObject obj = jsonArray.getJSONObject(0);
						parseProduct(obj);
					}
				} catch (Exception e) {
					Log.e(TAG, "error parse");
					// TODO: handle exception
				}
			}
		};
	};

	public void parseProduct(JSONObject jsonObj) {
		try {
			String titleText = jsonObj.getString(Products.TAG_TITLE);
			title.setText(titleText);
			String priceText = jsonObj.getString(Products.TAG_PRICE);
			price.setText(priceText);
			String contentText = jsonObj.getString(Products.TAG_PRODUCT_CATEGORY);
			content.setText(contentText);
			lat = jsonObj.getString("lat");
			lng = jsonObj.getString("lng");

			CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(
					Float.parseFloat(lat), Float.parseFloat(lng)));
			gmap.moveCamera(center);
			gmap.animateCamera(CameraUpdateFactory.zoomTo(10.0f));
			gpin = gmap.addMarker(new MarkerOptions().position(
					new LatLng(Float.parseFloat(lat), Float.parseFloat(lng)))
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_pin)));
			// lat = gpsTracker.getLat() + "";
			// lng = gpsTracker.getLng() + "";

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (getIntent().getExtras() != null) {
			Bundle bundle = getIntent().getExtras();
			product_id = bundle.getInt(constants.COMMON_KEY);
		}
		setContentView(R.layout.update_product_activity);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

		categoriesSpinner = (Spinner) findViewById(R.id.categories_spinner);
		countySpinner = (Spinner) findViewById(R.id.county_spinner);
		conditionSpinner = (Spinner) findViewById(R.id.condition_spinner);
		citiesSpinner = (Spinner) findViewById(R.id.cities_spinner);
		purpose_id = getResources().getStringArray(R.array.purpose_id);
		purpose_name = getResources().getStringArray(R.array.purpose_name);

/*		try {
			gmap = ((NiceSupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

			gmap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

				@Override
				public void onMapClick(LatLng point) {
					// // TODO Auto-generated method stub
					if (gpin != null) {
						gpin.remove();
					}
					gpin = gmap.addMarker(new MarkerOptions().position(
							new LatLng(point.latitude, point.longitude)).icon(
							BitmapDescriptorFactory
									.fromResource(R.drawable.ic_pin)));
					lat = point.latitude + "";
					lng = point.longitude + "";
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}*/

		items = getResources().getStringArray(R.array.choose_photo);
		ArrayAdapter<String> aimAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, purpose_name);
		aimSpinner = (Spinner) findViewById(R.id.aim_spinner);
		aimSpinner.setAdapter(aimAdapter);
		aimSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						aim_selected = Integer.valueOf(purpose_id[arg2]);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});
		condition_id = getResources().getStringArray(R.array.condition_id);
		condition_name = getResources().getStringArray(R.array.condition_name);
		ArrayAdapter<String> conditionAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, condition_name);
		conditionSpinner.setAdapter(conditionAdapter);
		conditionSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						condition_selected = Integer
								.valueOf(condition_id[arg2]);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}

				});
		title = (EditText) findViewById(R.id.title);
		price = (EditText) findViewById(R.id.price);
		content = (EditText) findViewById(R.id.content);
		btnUpload = (ButtonRectangle) findViewById(R.id.btn_submit);
		btnUpload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i(TAG, "submit clicked");
				if (!Utils.isConnectingToInternet(UpdateProductActivity.this)) {
					showMsg(getResources().getString(R.string.open_network));
				} else {
					doUpload();
				}
			}
		});

		new JSONFetchTask(getResources()
				.getString(R.string.categories_json_url) + "categories",
				handler_categories, KEY_CATEGORIES_RESPONSE).execute();

		LinkedHashSet<Integer> disableItem = new LinkedHashSet<Integer>();
		disableItem.add(R.id.btn_action_upload);
		setDisableItem(disableItem);
		//setTitle(R.string.update_description);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@SuppressLint("NewApi")
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	public boolean validateBeforeUpload() {
		String titleText = title.getText().toString();
		String contentText = content.getText().toString();
		if (titleText.equalsIgnoreCase("") || contentText.equalsIgnoreCase("")
				|| categories_selected == 0 || city_selected == 0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(
					getResources().getString(R.string.incorrect_info))
					.setTitle(getResources().getString(R.string.alert))
					.setPositiveButton(
							getResources().getString(R.string.ok_label),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

								}
							});
			AlertDialog dialog = builder.create();
			dialog.show();
		} else {
			return true;
		}
		return false;
	}

	@SuppressLint("NewApi")
	private void doUpload() {
		boolean valid = validateBeforeUpload();
		if (valid) {
			UserSessionManager sessionManager = new UserSessionManager(this);
			User user = sessionManager.getUserSession();
			if (user != null) {
				new Upload(user.getFbId(), user.getId()).execute();
			} else {
				Intent intent = new Intent(this, AuthenticationActivity.class);
				startActivity(intent);
			}
		}
	}

	@SuppressWarnings("unused")
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(final Session session, final SessionState state,
				final Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	@SuppressLint("NewApi")
	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (state.isOpened()) {
			UserSessionManager sessionManager = new UserSessionManager(this);
			User user = sessionManager.getUserSession();
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				Log.i(TAG + "user_id", user.getId() + "kasjd");
				new Upload(user.getFbId(), user.getId())
						.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			} else {
				new Upload(user.getFbId(), user.getId()).execute();
			}
			Log.i("FB AUT FRAGMENT", "Logged in...");
		} else if (state.isClosed()) {
			Log.i("FB AUT FRAGMENT", "Logged out...");
		}
	}

	private class Upload extends AsyncTask<Void, Void, Boolean> {
		String fb_id = null;
		int user_id = 0;

		public Upload(String fb_id, int user_id) {
			this.fb_id = fb_id;
			this.user_id = user_id;
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			dialog.dismiss();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = new ProgressDialog(UpdateProductActivity.this);
			dialog.setMessage(UpdateProductActivity.this.getResources()
					.getString(R.string.upload_product_msg));
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String handleInserUrl = getResources().getString(
					R.string.products_json_url)
					+ "update";
			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(handleInserUrl);
				MultipartEntity reqEntity = new MultipartEntity();

				String titleText = title.getText().toString();
				String priceText = price.getText().toString();
				String contentText = content.getText().toString();

				// reqEntity.addPart("county",new StringBody(county_selected + ""));

				// reqEntity.addPart("id", new StringBody(product_id + ""));
				// reqEntity.addPart("title", new StringBody(titleText));
				// reqEntity.addPart("price", new StringBody(priceText));
				// reqEntity.addPart("content", new StringBody(contentText));
				// reqEntity.addPart("cities", new StringBody(city_selected + ""));
				// reqEntity.addPart("categories", new StringBody(categories_selected + ""));
				// reqEntity.addPart("condition", new StringBody(condition_selected + ""));
				// reqEntity.addPart("purpose", new StringBody(aim_selected + ""));

				// reqEntity.addPart("lat", new StringBody(lat));
				// reqEntity.addPart("lng", new StringBody(lng));
				post.setEntity(reqEntity);
				HttpResponse response = client.execute(post);
				HttpEntity resEntity = response.getEntity();
				final String response_str = EntityUtils.toString(resEntity);
				if (resEntity != null) {
					Log.i("RESPONSE", response_str);
					runOnUiThread(new Runnable() {
						public void run() {
							try {
								dialog.dismiss();
								Log.i(TAG, "upload");
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
	};

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (cursor != null) {
			cursor.close();
		}
	}
}
