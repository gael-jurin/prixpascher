package org.nuvola.mobile.prixpascher.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import org.nuvola.mobile.prixpascher.ProductsActivity;
import org.nuvola.mobile.prixpascher.R;
import org.nuvola.mobile.prixpascher.adapters.CategoriesAdapter;
import org.nuvola.mobile.prixpascher.business.JSONFetchTask;
import org.nuvola.mobile.prixpascher.confs.constants;
import org.nuvola.mobile.prixpascher.models.Categories;
import android.app.Activity;
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

public class CategoriesFragment extends Fragment {
	ArrayList<Categories> categories_list = new ArrayList<Categories>();
	ListView list;
	CategoriesAdapter adapter;
	String TAG = "CategoriesFragment";
	ProgressBar loadMorePrg;
	LayoutInflater inflater;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		inflater=(LayoutInflater)activity.getLayoutInflater();
	}

	public static final CategoriesFragment newInstance() {
		// TODO Auto-generated constructor stub
		CategoriesFragment categoriesFragment = new CategoriesFragment();
		return categoriesFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.listview_container_layout, null);
		JSONFetchTask jsonFetchTask = new JSONFetchTask(getResources()
				.getString(R.string.categories_json_url) + "categories",
				handler);
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
						JSONObject categoriesJSON = jsonArray.getJSONObject(i);
						int id = categoriesJSON.getInt(Categories.TAG_ID);
						String name = categoriesJSON
								.getString(Categories.TAG_NAME);
						Categories categories = new Categories();
						categories.setId(id);
						categories.setName(name);
						categories_list.add(categories);
					}
					Log.i(TAG, categories_list.size() + "");
					adapter = new CategoriesAdapter(getActivity(),
							R.layout.common_item_layout, categories_list);
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
			bundle.putInt(constants.CATEGORIES_ID_KEY, categories_list
					.get(arg2).getId());
			intent.putExtras(bundle);
			startActivity(intent);
		}
	};
}
