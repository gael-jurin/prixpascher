package org.nuvola.mobile.prixpascher.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.nuvola.mobile.prixpascher.R;
import org.nuvola.mobile.prixpascher.business.Utils;
import org.nuvola.mobile.prixpascher.models.DrawerMenuItem;

import java.util.ArrayList;

public class DrawerMenuAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<DrawerMenuItem> drawerItems;

	public DrawerMenuAdapter(Context context,
			ArrayList<DrawerMenuItem> drawerItems) {
		this.context = context;
		this.drawerItems = drawerItems;
	}

	@Override
	public int getCount() {
		return drawerItems.size();
	}

	@Override
	public Object getItem(int position) {
		return drawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.drawer_menu_item, null);
		}
		final ImageView imgIcon = (ImageView) convertView
				.findViewById(R.id.icon);
		TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
		if (drawerItems.get(position).getAvt() != null
				&& !drawerItems.get(position).getAvt().equals("")) {
			Utils.MyPicasso.with(context)
					.load(drawerItems.get(position).getAvt())
					.resize(64, 64).centerCrop()
					.placeholder(R.drawable.ic_small_avatar)
					.error(R.drawable.ic_small_avatar)
					.into(imgIcon);
		} else {
			imgIcon.setImageResource(drawerItems.get(position).getIcon());
		}
		txtTitle.setText(drawerItems.get(position).getTitle());
		return convertView;
	}

}