package org.nuvola.mobile.prixpascher.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.nuvola.mobile.prixpascher.ProductsActivity;
import org.nuvola.mobile.prixpascher.R;
import org.nuvola.mobile.prixpascher.adapters.CategoriesAdapter;
import org.nuvola.mobile.prixpascher.confs.constants;
import org.nuvola.mobile.prixpascher.models.Category;
import org.nuvola.mobile.prixpascher.models.DrawerMenuItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CategoriesFragment extends Fragment {
    @Bind(R.id.rv) RecyclerView rv;

	List<DrawerMenuItem> categories = new ArrayList<>();
    private CategoriesAdapter rcAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

	public static final CategoriesFragment newInstance() {
		CategoriesFragment categoriesFragment = new CategoriesFragment();
		return categoriesFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		categories.addAll(Category.mobileAppCategories(this.getContext()));
		View view = inflater.inflate(R.layout.categories_main, null);
        ButterKnife.bind(this, view);

        final GridLayoutManager lLayout = new GridLayoutManager(getActivity(),2);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(lLayout);

        rcAdapter = new CategoriesAdapter(getActivity(), categories);
        rcAdapter.SetOnItemClickListener(new CategoriesAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				Intent intent = new Intent(getActivity(), ProductsActivity.class);
				intent.putExtra(constants.CATEGORIES_ID_KEY, categories.get(position)
						.getTitle());
				startActivity(intent);
			}
		});

        rv.setAdapter(rcAdapter);
		return view;
	}
}
