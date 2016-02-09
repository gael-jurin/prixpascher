package org.nuvola.mobile.prixpascher.fragments;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import org.nuvola.mobile.prixpascher.ProductsActivity;
import org.nuvola.mobile.prixpascher.R;
import org.nuvola.mobile.prixpascher.business.JSONFetchTask;
import org.nuvola.mobile.prixpascher.confs.constants;
import org.nuvola.mobile.prixpascher.models.Categories;
import org.nuvola.mobile.prixpascher.models.County;
import com.gc.materialdesign.views.ButtonRectangle;

@SuppressLint("NewApi")
public class FilterFragment extends Fragment {
	Spinner categoriesSpinner, countySpinner, citiesSpinner;
	EditText title;
	String titleText = null;
	static final String KEY_CATEGORIES_RESPONSE = "KEY_CATEGORIES_RESPONSE";
	static final String KEY_COUNTY_RESPONSE = "KEY_COUNTY_RESPONSE";
	static final String KEY_CITIES_RESPONSE = "KEY_CITIES_RESPONSE";
	String[] categories_name;
	int[] categories_id;
	String[] county_name;
	int[] county_id;
	String[] city_name;
	int[] city_id;
	String TAG = "FilterFragment";
	int categories_selected = 0;
	int county_selected = 0;
	int aim_selected = 0;
	int city_selected = 0;
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
					categories_id = new int[jsonArray.length() + 1];
					categories_name = new String[jsonArray.length() + 1];
					categories_id[0] = 0;
					categories_name[0] = getActivity().getResources()
							.getString(R.string.all_label);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject categoriesJSON = jsonArray.getJSONObject(i);
						int id = categoriesJSON.getInt(Categories.TAG_ID);
						String name = categoriesJSON
								.getString(Categories.TAG_NAME);
						categories_id[i + 1] = id;
						categories_name[i + 1] = name;
					}

					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							getActivity(), android.R.layout.simple_list_item_1,
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
							getActivity(), android.R.layout.simple_list_item_1,
							county_name);
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
							getActivity(), android.R.layout.simple_list_item_1,
							city_name);
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
							getActivity(),
							android.R.layout.simple_list_item_1, city_name);
					citiesSpinner.setAdapter(adapter);
					e.printStackTrace();
					e.printStackTrace();
				}
			}
		};
	};

	public static final FilterFragment newInstance() {
		// TODO Auto-generated constructor stub
		FilterFragment fragment = new FilterFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.filter_layout, null);
		categoriesSpinner = (Spinner) view
				.findViewById(R.id.categories_spinner);
		countySpinner = (Spinner) view.findViewById(R.id.county_spinner);
		citiesSpinner = (Spinner) view.findViewById(R.id.cities_spinner);
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

		title = (EditText) view.findViewById(R.id.title);

		ButtonRectangle btnFilter = (ButtonRectangle) view.findViewById(R.id.btn_filter);
		btnFilter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						ProductsActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt(constants.COUNTY_ID_KEY, county_selected);
				bundle.putInt(constants.CATEGORIES_ID_KEY, categories_selected);
				bundle.putInt(constants.CITY_ID_KEY, city_selected);
				titleText = title.getText().toString();
				if (titleText != null && !titleText.equalsIgnoreCase("")) {
					bundle.putString(constants.TITLE_KEY, titleText);
				}
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		return view;
	}
}
