package org.nuvola.mobile.prixpascher.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import org.nuvola.mobile.prixpascher.R;

public class AboutUsFragment extends Fragment {
	public static final AboutUsFragment newInstance() {
		// TODO Auto-generated constructor stub
		AboutUsFragment fragment = new AboutUsFragment();
		return fragment;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		String mime = "text/html";
		String encoding = "utf-8";
		View view = inflater.inflate(R.layout.about_us, null);
		WebView content = (WebView) view.findViewById(R.id.content);
		content.loadDataWithBaseURL(null, getActivity().getResources()
				.getString(R.string.about_us_content), mime, encoding, null);
		return view;
	}
}

