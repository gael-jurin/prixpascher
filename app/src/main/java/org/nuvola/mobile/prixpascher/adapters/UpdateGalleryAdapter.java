package org.nuvola.mobile.prixpascher.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import org.nuvola.mobile.prixpascher.R;
import org.nuvola.mobile.prixpascher.UpdateImagePreviewActivity;
import org.nuvola.mobile.prixpascher.business.Utils;
import org.nuvola.mobile.prixpascher.confs.constants;

import java.util.ArrayList;

public class UpdateGalleryAdapter extends BaseAdapter {

	private Activity context;
	private ArrayList<String> filePaths = new ArrayList<String>();
	private ArrayList<String> thumbPaths = new ArrayList<String>();
	private ArrayList<Integer> ids = new ArrayList<Integer>();
	int properties_id;

	public UpdateGalleryAdapter(Activity activity, int properties_id,
			ArrayList<String> filePaths, ArrayList<String> thumbPaths,
			ArrayList<Integer> ids, int imageWidth) {
		this.context = activity;
		this.filePaths = filePaths;
		this.properties_id = properties_id;
		this.ids = ids;
		this.thumbPaths = thumbPaths;
	}

	@Override
	public int getCount() {
		return this.filePaths.size();
	}

	@Override
	public Object getItem(int position) {
		return this.filePaths.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if (convertView == null) {
			imageView = new ImageView(context);
		} else {
			imageView = (ImageView) convertView;
		}

		Utils.MyPicasso.with(context)
				.load(filePaths.get(position))
				.resize(256, 256).centerCrop().placeholder(R.drawable.no_photo)
				.error(R.drawable.no_photo).into(imageView);

		imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// v.getTag();
				// TODO Auto-generated method stub
				Intent intent = new Intent(context,
						UpdateImagePreviewActivity.class);
				intent.putExtra(constants.IMAGES_PATH, filePaths.get(position));
				intent.putExtra(constants.THUMB_KEY, thumbPaths.get(position));
				intent.putExtra(constants.COMMON_KEY, ids.get(position));
				intent.putExtra(constants.PRODUCT_ID, properties_id);
				context.startActivity(intent);
			}
		});
		return imageView;
	}
}
