package org.nuvola.mobile.prixpascher.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import org.nuvola.mobile.prixpascher.ImagePreviewActivity;
import org.nuvola.mobile.prixpascher.R;
import org.nuvola.mobile.prixpascher.business.TouchImageView;
import org.nuvola.mobile.prixpascher.business.Utils;
import org.nuvola.mobile.prixpascher.confs.constants;

import java.util.ArrayList;
import java.util.List;

public class FullScreenImageAdapter extends PagerAdapter {
	private Activity activity;
	private List<String> paths;
	private LayoutInflater inflater;

	// constructor
	public FullScreenImageAdapter(Activity activity,
			List<String> imagePaths) {
		this.activity = activity;
		this.paths = imagePaths;
	}

	@Override
	public int getCount() {
		return this.paths.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((RelativeLayout) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		TouchImageView imgDisplay;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View viewLayout = inflater.inflate(R.layout.full_preview_item,
				container, false);

		imgDisplay = viewLayout.findViewById(R.id.imgDisplay);
		Utils.MyPicasso.with(activity)
				.load(paths.get(position))
				.placeholder(R.drawable.no_photo)
				.into(imgDisplay);

		/*Ion.with(activity)
                .load(paths.get(position))
                .withBitmap()
				.error(R.drawable.no_photo).placeholder(R.drawable.no_photo)
				.intoImageView(imgDisplay);*/

		(container).addView(viewLayout);

        imgDisplay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(activity,
                        ImagePreviewActivity.class);
                intent.putStringArrayListExtra(
                        constants.IMAGES_PATH, (ArrayList<String>) paths);
                intent.putExtra(constants.IMAGE_POSITION, position);
                activity.startActivity(intent);
            }
        });

		return viewLayout;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		(container).removeView((RelativeLayout) object);
	}
}
