package org.nuvola.mobile.prixpascher.adapters;

import java.util.ArrayList;

import org.nuvola.mobile.prixpascher.R;
import org.nuvola.mobile.prixpascher.models.Categories;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CategoriesAdapter extends ArrayAdapter<Categories> {
	private Context context;
	private int itemLayoutResource;

	public CategoriesAdapter(Context context, int itemLayoutResource,
			ArrayList<Categories> categories) {
		super(context, itemLayoutResource, categories);
		this.itemLayoutResource = itemLayoutResource;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(this.itemLayoutResource, null);
		}
		Categories categorie = getItem(position);
		TextView title = (TextView) view.findViewById(R.id.title);
		title.setText(categorie.getName());
		return view;
	}
}
