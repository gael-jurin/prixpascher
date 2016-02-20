package org.nuvola.mobile.prixpascher;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Locale;

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
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
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
import org.nuvola.mobile.prixpascher.business.Utils;
import org.nuvola.mobile.prixpascher.business.UserSessionManager;
import org.nuvola.mobile.prixpascher.models.Categories;
import org.nuvola.mobile.prixpascher.models.County;
import org.nuvola.mobile.prixpascher.models.User;
import com.facebook.Session;
import com.facebook.SessionState;
import com.gc.materialdesign.views.ButtonRectangle;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class UploadActivity extends ActionBarParentActivity {
	private static final int SELECT_PICTURE = 0;
	private static final int TAKE_PICTURE = 1;
	ImageView btnPickPhoto_1, btnPickPhoto_2, btnPickPhoto_3;
	Cursor cursor;
	int currentChooserPhotoId = 0;
	String[] categories_name;
	int[] categories_id;
	String[] county_name;
	int[] county_id;
	String[] city_name;
	int[] city_id;
	String[] aim_id;
	String[] aim_name;
	Spinner categoriesSpinner, countySpinner, aimSpinner, citiesSpinner;
	String TAG = "UploadActivity";
	static final String KEY_CATEGORIES_RESPONSE = "KEY_CATEGORIES_RESPONSE";
	static final String KEY_COUNTY_RESPONSE = "KEY_COUNTY_RESPONSE";
	static final String KEY_CITIES_RESPONSE = "KEY_CITIES_RESPONSE";
	EditText price, title, content;
	int categories_selected = 0;
	int county_selected = 0;
	int city_selected = 0;
	int aim_selected = -1;
	String photoPath1 = null;
	ButtonRectangle btnUpload;
	ProgressDialog dialog;
	File tmpFile;
	CharSequence[] items;
    Toolbar toolbar;

	Handler handler_categories = new Handler() {
		@SuppressLint("NewApi")
		public void handleMessage(android.os.Message msg) {
			Bundle data = msg.getData();
			Log.i(TAG, "Handler");
			if (data.containsKey(KEY_CATEGORIES_RESPONSE)) {
				Log.i(TAG, "Handler");
				String jsonString = data.getString(KEY_CATEGORIES_RESPONSE);
				try {
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
							UploadActivity.this,
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

					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
						new JSONFetchTask(getResources().getString(
								R.string.county_json_url)
								+ "county", handler_county, KEY_COUNTY_RESPONSE)
								.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					} else {
						new JSONFetchTask(getResources().getString(
								R.string.county_json_url)
								+ "county", handler_county, KEY_COUNTY_RESPONSE)
								.execute();
					}
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
							UploadActivity.this,
							android.R.layout.simple_list_item_1, county_name);
					countySpinner.setAdapter(adapter);
					countySpinner
							.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

								@Override
								public void onItemSelected(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									// TODO Auto-generated method stub
									county_selected = county_id[arg2];
									if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
										new JSONFetchTask(
												getResources()
														.getString(
																R.string.cities_json_url)
														+ "cities_by_county_id/id/"
														+ county_selected,
												handler_cities,
												KEY_CITIES_RESPONSE)
												.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
									} else {
										new JSONFetchTask(
												getResources()
														.getString(
																R.string.cities_json_url)
														+ "cities_by_county_id/id/"
														+ county_selected,
												handler_cities,
												KEY_CITIES_RESPONSE).execute();
									}
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
							UploadActivity.this,
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

				} catch (Exception e) {
					// TODO: handle exception
					city_name = new String[0];
					city_selected = 0;
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							UploadActivity.this,
							android.R.layout.simple_list_item_1, city_name);
					citiesSpinner.setAdapter(adapter);
					e.printStackTrace();
				}
			}
		};
	};

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload_activity_layout);


		categoriesSpinner = (Spinner) findViewById(R.id.categories_spinner);
		countySpinner = (Spinner) findViewById(R.id.county_spinner);
		aimSpinner = (Spinner) findViewById(R.id.aim_spinner);
		citiesSpinner = (Spinner) findViewById(R.id.cities_spinner);
		aim_id = getResources().getStringArray(R.array.aim_id);
		aim_name = getResources().getStringArray(R.array.aim_name);

		// items[0] = getResources().getString(R.string.sd_card);
		// items[1] = getResources().getString(R.string.camera);

		items = getResources().getStringArray(R.array.choose_photo);
		ArrayAdapter<String> aimAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, aim_name);
		aimSpinner.setAdapter(aimAdapter);
		aimSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						aim_selected = Integer.valueOf(aim_id[arg2]);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}

				});

		btnPickPhoto_1 = (ImageView) findViewById(R.id.btn_pick_photo_1);
		btnPickPhoto_1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showChoosePhotoMethod();
				currentChooserPhotoId = R.id.btn_pick_photo_1;
			}
		});
/*		btnPickPhoto_2 = (ImageView) findViewById(R.id.btn_pick_photo_2);
		btnPickPhoto_2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showChoosePhotoMethod();
				currentChooserPhotoId = R.id.btn_pick_photo_2;
			}
		});
		btnPickPhoto_3 = (ImageView) findViewById(R.id.btn_pick_photo_3);
		btnPickPhoto_3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showChoosePhotoMethod();
				currentChooserPhotoId = R.id.btn_pick_photo_3;
			}
		});*/

		title = (EditText) findViewById(R.id.title);
		price = (EditText) findViewById(R.id.price);
		content = (EditText) findViewById(R.id.content);
		btnUpload = (ButtonRectangle) findViewById(R.id.btn_submit);
		btnUpload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i(TAG, "submit clicked");
				if (!Utils.isConnectingToInternet(UploadActivity.this)) {
					showMsg(getResources().getString(R.string.open_network));
				} else {
					doUpload();
				}
			}
		});

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			new JSONFetchTask(getResources().getString(
					R.string.categories_json_url)
					+ "categories", handler_categories, KEY_CATEGORIES_RESPONSE)
					.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			new JSONFetchTask(getResources().getString(
					R.string.categories_json_url)
					+ "categories", handler_categories, KEY_CATEGORIES_RESPONSE)
					.execute();
		}

		LinkedHashSet<Integer> disableItem = new LinkedHashSet<Integer>();
		disableItem.add(R.id.btn_action_upload);
		setDisableItem(disableItem);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.upload_label);
        setSupportActionBar(toolbar);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@SuppressLint("NewApi")
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	private void showChoosePhotoMethod() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getResources().getString(R.string.what_you_want));
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				switch (item) {
				case 0:
					pickPhoto();
					break;

				case 1:
					capturePhoto();
					break;

				default:
					break;
				}
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void pickPhoto() {
		// TODO: launch the photo picker
		Intent intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(Intent.createChooser(intent, "Select"),
				SELECT_PICTURE);
	}

	public void capturePhoto() {
		String ts = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
				.format(new Date());
		File dir = Utils.createDirOnSDCard(getResources().getString(
                R.string.folder_save_photo));
		tmpFile = new File(dir.getPath() + "/Photo_" + ts + ".jpg");
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (tmpFile != null) {
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tmpFile));
			startActivityForResult(intent, TAKE_PICTURE);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			final ImageView currentImageView = (ImageView) findViewById(currentChooserPhotoId);
			switch (requestCode) {
			case SELECT_PICTURE:
				String selectAbpath = getPath(data.getData());
				Ion.with(getApplicationContext(), new File(selectAbpath))
						.withBitmap().resize(200, 200).centerCrop().asBitmap()
						.setCallback(new FutureCallback<Bitmap>() {
							@Override
							public void onCompleted(Exception arg0,
									Bitmap bitmap) {
								currentImageView.setImageBitmap(bitmap);
							}
						});
				switch (currentChooserPhotoId) {
				case R.id.btn_pick_photo_1:
					photoPath1 = selectAbpath;
					break;

			/*	case R.id.btn_pick_photo_2:
					photoPath2 = selectAbpath;
					break;

				case R.id.btn_pick_photo_3:
					photoPath3 = selectAbpath;
					break;*/

				default:
					break;
				}
				break;

			case TAKE_PICTURE:
				if (tmpFile.exists()) {
					Ion.with(getApplicationContext(), tmpFile).withBitmap()
							.resize(200, 200).centerCrop().asBitmap()
							.setCallback(new FutureCallback<Bitmap>() {
								@Override
								public void onCompleted(Exception arg0,
										Bitmap bitmap) {
									currentImageView.setImageBitmap(bitmap);
								}
							});
				}
				switch (currentChooserPhotoId) {
				case R.id.btn_pick_photo_1:
					photoPath1 = tmpFile.getAbsolutePath();
					break;

			/*	case R.id.btn_pick_photo_2:
					photoPath2 = tmpFile.getAbsolutePath();
					break;

				case R.id.btn_pick_photo_3:
					photoPath3 = tmpFile.getAbsolutePath();
					break;*/

				default:
					break;
				}
				Log.i(TAG, tmpFile.getAbsolutePath());
				tmpFile = null;
				break;
			default:
				break;
			}
		}
	}

	private String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		cursor = getContentResolver().query(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String filePath = cursor.getString(column_index);
		return filePath;
	}

	public boolean validateBeforeUpload() {
		String titleText = title.getText().toString();
		String priceText = price.getText().toString();
		String contentText = content.getText().toString();
		Log.i(TAG, "text" + titleText);
		Log.i(TAG, "text" + priceText);
		Log.i(TAG, "text" + contentText);
		if (titleText.equalsIgnoreCase("") || contentText.equalsIgnoreCase("")
				|| categories_selected == 0 || county_selected == 0
				|| city_selected == 0) {
			/*AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
			dialog.show();*/
            showDialog(	getResources().getString(R.string.incorrect_info));
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
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					Log.i(TAG + "user_id", user.getId() + "kasjd");
					new Upload(user.getFbId(), user.getId())
							.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				} else {
					new Upload(user.getFbId(), user.getId()).execute();
				}
			} else {
				Intent intent = new Intent(this, AuthenticationActivity.class);
				startActivity(intent);
			}
		}
	}

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

		public Upload() {
			// TODO Auto-generated constructor stub
		}

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

		protected void onPostExecute() {
			dialog.dismiss();
		};

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = new ProgressDialog(UploadActivity.this);
			dialog.setMessage(UploadActivity.this.getResources().getString(
					R.string.upload_product_msg));
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String handleInserUrl = getResources().getString(
					R.string.products_json_url)
					+ "products";
			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(handleInserUrl);
				MultipartEntity reqEntity = new MultipartEntity();

				FileBody fileUpload = null;
				if (photoPath1 != null) {
					fileUpload = new FileBody(new File(photoPath1));
					// reqEntity.addPart("photo1", fileUpload);
				}


				String titleText = title.getText().toString();
				String priceText = price.getText().toString();
				String contentText = content.getText().toString();
				// reqEntity.addPart("user_id", new StringBody(user_id + ""));
				// reqEntity.addPart("fb_id", new StringBody(fb_id));
				// reqEntity.addPart("title", new StringBody(titleText));
				// reqEntity.addPart("price", new StringBody(priceText));
				// reqEntity.addPart("content", new StringBody(contentText));
				// reqEntity.addPart("city", new StringBody(city_selected + ""));
				// reqEntity.addPart("categories", new StringBody(categories_selected + ""));
				// reqEntity.addPart("county",new StringBody(county_selected + ""));
				// reqEntity.addPart("aim", new StringBody(aim_selected + ""));
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
								title.setText("");
								price.setText("");
								content.setText("");
								btnPickPhoto_1
										.setImageResource(R.drawable.ic_picker_photo);
								btnPickPhoto_2
										.setImageResource(R.drawable.ic_picker_photo);
								btnPickPhoto_3
										.setImageResource(R.drawable.ic_picker_photo);
								photoPath1 = null;

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
