package org.nuvola.mobile.prixpascher.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import org.nuvola.mobile.prixpascher.ProductActivity;
import org.nuvola.mobile.prixpascher.R;
import org.nuvola.mobile.prixpascher.adapters.ProductsAdapter;
import org.nuvola.mobile.prixpascher.business.Utils;
import org.nuvola.mobile.prixpascher.confs.constants;
import org.nuvola.mobile.prixpascher.dto.ProductVO;
import org.nuvola.mobile.prixpascher.dto.SearchFilterVO;
import org.nuvola.mobile.prixpascher.models.Category;
import org.nuvola.mobile.prixpascher.models.ProductsResponse;
import org.nuvola.mobile.prixpascher.models.SortField;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductsFragment extends Fragment {
    List<ProductVO> productsList = new ArrayList<>();
    SearchFilterVO searchFilter = new SearchFilterVO();
    int COUNT_ITEM_LOAD_MORE = 40;
    int first = 0;
    boolean loadingMore = true;
    int user_id = 0, user_post = 0, pastVisiblesItems, visibleItemCount, totalItemCount;

    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv) RecyclerView rv;
    @BindView(R.id.btnAll) Button btnAll;
    @BindView(R.id.btnBest) Button btnBest;
    @BindView(R.id.btnPromo) Button btnPromo;
    @BindView(R.id.prgLoadMore) LinearLayout loadMorePrg;
    ProductsAdapter adapter;
    static Toolbar toolbar;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public static final ProductsFragment newInstance(Toolbar otoolbar) {
        ProductsFragment fragment = new ProductsFragment();
        toolbar = otoolbar;
        return fragment;
    }

    @SuppressWarnings("deprecation")
    private void setButtonFocus(Button btn, int drawable) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            btn.setBackgroundDrawable(getActivity().getResources().getDrawable(
                    drawable));
        } else {
            btn.setBackground(getActivity().getResources()
                    .getDrawable(drawable));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_list_container_layout, null);
        ButterKnife.bind(this, view);

        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        resetSearch();
        searchFilter.setCategory(null); // Only on create

        adapter = new ProductsAdapter(getActivity(), productsList);
        adapter.SetOnItemClickListener(new ProductsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (user_id == 0) {
                    Intent intent = new Intent(getActivity(), ProductActivity.class);
                    intent.putExtra(constants.COMMON_KEY, productsList.get(position)
                            .getId());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), ProductActivity.class);
                    intent.putExtra(constants.COMMON_KEY, productsList.get(position)
                            .getId());
                    intent.putExtra(constants.USER_ID_KEY, user_id);
                    startActivity(intent);
                }
            }
        });
        rv.setAdapter(adapter);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = llm.getChildCount();
                totalItemCount = llm.getItemCount();
                pastVisiblesItems = llm.findFirstVisibleItemPosition();
                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                    loadingMore = true;
                    first += COUNT_ITEM_LOAD_MORE;

                    searchFilter.setPage(first);
                    searchFilter.setSize(COUNT_ITEM_LOAD_MORE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        new LoadMoreDataTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        new LoadMoreDataTask().execute();
                    }
                }
            }
        });


        if (getArguments() != null) {
            Bundle bundle = getArguments();
            if (bundle.containsKey(constants.TITLE_KEY)) {
                String title = bundle.getString(constants.TITLE_KEY);
                if (title != null && title != "") {
                    try {
                        title = URLEncoder.encode(title, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    searchFilter.setSearchText(title);
                }
            }

            if (bundle.containsKey(constants.CATEGORIES_ID_KEY)) {
                String category = bundle.getString(constants.CATEGORIES_ID_KEY);
                if (category != null && category != "") {
                    Category value = Category.valueOf(category);
                    toolbar.setTitle(Category.getLabel(value));
                    searchFilter.setCategory(value);
                }
            }

            if (bundle.containsKey(constants.COUNTY_ID_KEY)) {
                int id = bundle.getInt(constants.COUNTY_ID_KEY);
                if (id != 0) {
                    // query += "&county_id=" + id;
                }
            }

            if (bundle.containsKey(constants.CITY_ID_KEY)) {
                int id = bundle.getInt(constants.CITY_ID_KEY);
                if (id != 0) {
                    // query += "&cities_id=" + id;
                }
            }

            if (bundle.containsKey(constants.USER_ID_KEY)) {
                String id = bundle.getString(constants.USER_ID_KEY);
                // if (id != 0) {
                    // query += "&user_id=" + id;
                    // user_id = id;

                // }
            }

            if (bundle.containsKey(constants.USER_POST_KEY)) {
                int id = bundle.getInt(constants.USER_POST_KEY);
                if (id != 0) {
                    // query += "&user_id=" + id;
                    user_post = id;
                }
            }

        }

        setButtonFocus(btnAll, R.drawable.tab_categories_pressed);

        btnAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                setButtonFocus(btnAll, R.drawable.tab_categories_pressed);
                setButtonFocus(btnBest, R.drawable.tab_categories_normal);
                setButtonFocus(btnPromo, R.drawable.tab_categories_normal);
                first = 0;

                searchFilter.setPage(first);
                searchFilter.setSize(COUNT_ITEM_LOAD_MORE);
                searchFilter.setPromotion(false);
                searchFilter.setDefaultSort(SortField.MOST_UPDATED);

                loadingMore = false;
                productsList.clear();
                adapter.notifyDataSetChanged();
                new LoadMoreDataTask().execute();
            }
        });

        btnPromo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                setButtonFocus(btnPromo, R.drawable.tab_categories_pressed);
                setButtonFocus(btnAll, R.drawable.tab_categories_normal);
                setButtonFocus(btnBest, R.drawable.tab_categories_normal);
                first = 0;

                searchFilter.setPage(first);
                searchFilter.setSize(COUNT_ITEM_LOAD_MORE);
                searchFilter.setPromotion(true);

                loadingMore = false;
                productsList.clear();
                adapter.notifyDataSetChanged();
                new LoadMoreDataTask().execute();
            }
        });

        btnBest.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                setButtonFocus(btnBest, R.drawable.tab_categories_pressed);
                setButtonFocus(btnAll, R.drawable.tab_categories_normal);
                setButtonFocus(btnPromo, R.drawable.tab_categories_normal);
                first = 0;

                searchFilter.setPage(first);
                searchFilter.setSize(COUNT_ITEM_LOAD_MORE);
                searchFilter.setPromotion(false);
                searchFilter.setDefaultSort(SortField.MOST_VIEWED);

                loadingMore = false;
                productsList.clear();
                adapter.notifyDataSetChanged();
                new LoadMoreDataTask().execute();
            }
        });

        new LoadMoreDataTask().execute();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(productsList != null && productsList.size() != 0);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    new PullToRefreshDataTask()
                            .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    new PullToRefreshDataTask().execute();
                }
            }
        });
        return view;
    }

    private void parseAndAppend() {
        try {
            loadingMore = false;
            adapter.notifyDataSetChanged();
            loadMorePrg.setVisibility(View.GONE);
        } catch (Exception e) {
            loadingMore = true;
            loadMorePrg.setVisibility(View.GONE);
        }
    }

    private void parseAndPrepend() {
        try {
            loadingMore = false;
            adapter.notifyDataSetChanged();
            loadMorePrg.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<ProductVO> feedJson(boolean refresh) {
        if (refresh) {
            productsList.clear();
            resetSearch();
            searchFilter.setPage(0);
        }

        HttpEntity<SearchFilterVO> requestEntity = new HttpEntity<>(searchFilter);
        ResponseEntity<ProductsResponse> products = Utils.MyRestemplate.getInstance().postForEntity(
                getResources().getString(R.string.products_json_url),
                requestEntity, ProductsResponse.class);

        productsList.addAll(products.getBody().getPayload());
        return productsList;
    }

    private void resetSearch() {
        searchFilter.setSearchText("*");
        searchFilter.setPage(first);
        searchFilter.setSize(COUNT_ITEM_LOAD_MORE);
        searchFilter.setType(null);
        searchFilter.setUserId(null);
        searchFilter.setBrand(null);
        searchFilter.setCity(null);
        // searchFilter.setCategory(null);
    }

    private class LoadMoreDataTask extends AsyncTask<Void, Void, List<ProductVO>> {

        @Override
        protected List<ProductVO> doInBackground(Void... params) {
            if (isCancelled()) {
                return null;
            }
            return feedJson(false);
        }

        @Override
        protected void onPostExecute(List<ProductVO> result) {
            if(isAdded()) {
                parseAndAppend();
                super.onPostExecute(result);
            }
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            loadMorePrg.setVisibility(View.VISIBLE);
            loadingMore = true;
        }

        @Override
        protected void onCancelled() {
            loadingMore = false;
            loadMorePrg.setVisibility(View.GONE);
        }
    }


    private class PullToRefreshDataTask extends AsyncTask<Void, Void, List<ProductVO>> {

        public PullToRefreshDataTask() {
        }

        @Override
        protected List<ProductVO> doInBackground(Void... params) {
            if (isCancelled()) {
                return null;
            }
            return feedJson(true);
        }

        @Override
        protected void onPostExecute(List<ProductVO> result) {
            if(isAdded()) {
                super.onPostExecute(result);
                parseAndPrepend();
            }
            swipeRefreshLayout.setRefreshing(false);
        }

       @Override
        protected void onCancelled() {
           swipeRefreshLayout.setRefreshing(false);
        }
    }
}
