package org.nuvola.mobile.prixpascher.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;
import org.nuvola.mobile.prixpascher.ProductsActivity;
import org.nuvola.mobile.prixpascher.R;
import org.nuvola.mobile.prixpascher.adapters.CountyAdapter;
import org.nuvola.mobile.prixpascher.tasks.JSONFetchTask;
import org.nuvola.mobile.prixpascher.confs.constants;
import org.nuvola.mobile.prixpascher.models.Categories;
import org.nuvola.mobile.prixpascher.models.County;

import java.util.ArrayList;

public class CitiesFragment extends Fragment {
	ArrayList<County> provinces_list = new ArrayList<County>();
	ListView list;
	CountyAdapter adapter;
	String TAG = "CitiesFragment";
	ProgressBar loadMorePrg;
	LayoutInflater inflater;

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
	}

	public static final CitiesFragment newInstance() {
		// TODO Auto-generated constructor stub
		CitiesFragment fragment = new CitiesFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.listview_container_layout, null);
		JSONFetchTask jsonFetchTask = new JSONFetchTask(getResources()
				.getString(R.string.cities_json_url) + "cities", handler);
		jsonFetchTask.execute();
		list = (ListView) view.findViewById(R.id.list);
		LinearLayout footerView = (LinearLayout) inflater.inflate(
				R.layout.footer_loadmore_layout, null);
		loadMorePrg = (ProgressBar) footerView.findViewById(R.id.prgLoadMore);
		list.addFooterView(footerView);
		list.setAdapter(adapter);
		return view;
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Bundle data = msg.getData();
			Log.i(TAG, "Handler");
			if (data.containsKey(JSONFetchTask.KEY_RESPONSE)) {
				Log.i(TAG, "Handler");
				String jsonString = data.getString(JSONFetchTask.KEY_RESPONSE);
				try {
					JSONArray jsonArray = new JSONArray(jsonString);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject provincesJSON = jsonArray.getJSONObject(i);
						int id = provincesJSON.getInt(Categories.TAG_ID);
						String name = provincesJSON
								.getString(Categories.TAG_NAME);
						County county = new County();
						county.setId(id);
						county.setName(name);
						provinces_list.add(county);
					}
					Log.i(TAG, provinces_list.size() + "");
					adapter = new CountyAdapter(getActivity(),
							R.layout.common_item_layout, provinces_list);
					list.setAdapter(adapter);
					list.setOnItemClickListener(onItemClickListener);
					loadMorePrg.setVisibility(View.GONE);
				} catch (Exception e) {
					// TODO: handle exception
					loadMorePrg.setVisibility(View.GONE);
					e.printStackTrace();
				}
			}
		};
	};

	private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(getActivity(), ProductsActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt(constants.CITY_ID_KEY, provinces_list.get(arg2)
					.getId());
			intent.putExtras(bundle);
			startActivity(intent);
		}
	};
}
