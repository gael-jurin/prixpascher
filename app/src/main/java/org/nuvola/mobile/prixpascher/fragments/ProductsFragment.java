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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.afollestad.materialdialogs.MaterialDialog;
import com.clockbyte.admobadapter.AdmobRecyclerAdapterWrapper;
import com.gc.materialdesign.views.ButtonFlat;
import com.google.android.gms.ads.MobileAds;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.nuvola.mobile.prixpascher.ProductActivity;
import org.nuvola.mobile.prixpascher.R;
import org.nuvola.mobile.prixpascher.adapters.ProductsAdapter;
import org.nuvola.mobile.prixpascher.business.EmptyRecyclerView;
import org.nuvola.mobile.prixpascher.business.Utils;
import org.nuvola.mobile.prixpascher.confs.constants;
import org.nuvola.mobile.prixpascher.dto.ProductVO;
import org.nuvola.mobile.prixpascher.dto.SearchFilterVO;
import org.nuvola.mobile.prixpascher.models.Category;
import org.nuvola.mobile.prixpascher.models.ProductsResponse;
import org.nuvola.mobile.prixpascher.models.SortField;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ProductsFragment extends Fragment {
    static Toolbar toolbar;

    public static String TAG = "ProductsFragment";
    List<ProductVO> productsList = new ArrayList<>();
    long productsCount = 0;
    SearchFilterVO searchFilter = new SearchFilterVO();
    int COUNT_ITEM_LOAD_MORE = 40;
    int first = 0;
    boolean loadingMore = true;

    int user_id = 0, user_post = 0, pastVisiblesItems, visibleItemCount, totalItemCount;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv)
    EmptyRecyclerView rv;
    @BindView(R.id.btnAll) Button btnAll;
    @BindView(R.id.btnFilter) Button btnFilter;
    @BindView(R.id.btnPromo) Button btnPromo;
    @BindView(R.id.prgLoadMore) LinearLayout loadMorePrg;
    private ProductsAdapter adapter;
    private AdmobRecyclerAdapterWrapper adapterWrapper;
    private LoadMoreDataTask loadMoreDataTask = new LoadMoreDataTask();
    private PullToRefreshDataTask pullToRefreshDataTask = new PullToRefreshDataTask();
    private DiscreteSeekBar priceBar;
    private Spinner citiesSpinner;
    private Spinner categoriesSpinner;
    private MaterialDialog filterDialog;
    private String categ_selected;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public static final ProductsFragment newInstance(Toolbar otoolbar) {
        ProductsFragment fragment = new ProductsFragment();
        toolbar = otoolbar;
        return fragment;
    }

    @Override
    public void onStop() {
        super.onStop();

        loadMoreDataTask.cancel(true);
        pullToRefreshDataTask.cancel(true);
    }

    @SuppressWarnings("deprecation")
    private void setButtonFocus(Button btn, int drawable) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
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

        MobileAds.initialize(getApplicationContext(), getString(R.string.admob_publisher_id));

        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        resetSearch(false);
        searchFilter.setCategory(null);

        adapter = new ProductsAdapter(getActivity(), productsList);
        adapter.SetOnItemClickListener(new ProductsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int originalContentPosition = adapterWrapper
                        .getAdapterCalculator().getOriginalContentPosition(position,
                                adapterWrapper.getFetchedAdsCount(), adapter.getItemCount());
                Intent intent = new Intent(getActivity(), ProductActivity.class);
                intent.putExtra(constants.COMMON_KEY, productsList.get(originalContentPosition)
                        .getId());
                if (user_id == 0) {
                    startActivity(intent);
                } else {
                    intent.putExtra(constants.USER_ID_KEY, user_id);
                    startActivity(intent);
                }
            }
        });

        adapterWrapper = new AdmobRecyclerAdapterWrapper(getContext(),
                getString(R.string.affiliate_admob_unit_id));
        adapterWrapper.setAdapter(adapter);
        adapterWrapper.setLimitOfAds(10);
        adapterWrapper.setNoOfDataBetweenAds(50);
        adapterWrapper.setFirstAdIndex(10);

        rv.setAdapter(adapterWrapper);
        // rv.setEmptyView(view.findViewById(R.id.empty_view)); TODO Fix this component style

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = llm.getChildCount();
                totalItemCount = llm.getItemCount();
                pastVisiblesItems = llm.findFirstVisibleItemPosition();
                int page = visibleItemCount + pastVisiblesItems;
                if (page == totalItemCount && page <= productsCount) {
                    loadingMore = true;
                    first += 1;

                    searchFilter.setPage(first);
                    searchFilter.setSize(COUNT_ITEM_LOAD_MORE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        loadMoreDataTask = new LoadMoreDataTask();
                        loadMoreDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        loadMoreDataTask = new LoadMoreDataTask();
                        loadMoreDataTask.execute();
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
                        Log.e(TAG, e.getMessage());
                    }
                    searchFilter.setSearchText(title);
                    searchFilter.setDefaultSort(SortField.PRICE);
                    searchFilter.setDefaultOrder(true);
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
                setButtonFocus(btnFilter, R.drawable.tab_categories_normal);
                setButtonFocus(btnPromo, R.drawable.tab_categories_normal);
                resetSearch(false);
                first = 0;

                searchFilter.setPromotion(false);
                searchFilter.setDefaultSort(SortField.MOST_UPDATED);

                loadingMore = false;
                productsList.clear();
                adapter.notifyDataSetChanged();
                loadMoreDataTask = new LoadMoreDataTask();
                loadMoreDataTask.execute();
            }
        });

        btnPromo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                setButtonFocus(btnPromo, R.drawable.tab_categories_pressed);
                setButtonFocus(btnAll, R.drawable.tab_categories_normal);
                setButtonFocus(btnFilter, R.drawable.tab_categories_normal);
                if (filterDialog != null) {
                    resetSearch(false);
                }
                first = 0;

                searchFilter.setPage(first);
                searchFilter.setSize(COUNT_ITEM_LOAD_MORE);
                searchFilter.setPromotion(true);

                loadingMore = false;
                productsList.clear();
                adapter.notifyDataSetChanged();
                loadMoreDataTask = new LoadMoreDataTask();
                loadMoreDataTask.execute();
            }
        });

        btnFilter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                setButtonFocus(btnFilter, R.drawable.tab_categories_pressed);
                setButtonFocus(btnAll, R.drawable.tab_categories_normal);
                setButtonFocus(btnPromo, R.drawable.tab_categories_normal);

                MaterialDialog.Builder filterDialogBuilder = new MaterialDialog.Builder(getActivity());
                filterDialogBuilder.customView(R.layout.filter_dialog_layout, true);

                if (filterDialog == null) {
                    filterDialog = filterDialogBuilder.build();
                    categ_selected = Category.electromenager.name();

                    categoriesSpinner  = (Spinner) filterDialog.findViewById(R.id.categories_spinner);
                    final String[] category_name = Category.filterMobCategoryValues();
                    final ArrayAdapter<String> cadapter = new ArrayAdapter<>(
                            getActivity(),
                            android.R.layout.simple_list_item_1, category_name);
                    categoriesSpinner.setAdapter(cadapter);
                    categoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                            categ_selected = category_name[arg2];
                            categ_selected = categ_selected.replaceAll(" ", "_");
                            cadapter.notifyDataSetChanged();
                        }
                        @Override
                        public void onNothingSelected(
                                AdapterView<?> arg0) {
                        }
                    });
                }
                filterDialog.setCanceledOnTouchOutside(true);

                priceBar = (DiscreteSeekBar) filterDialog.findViewById(R.id.priceBar);
                priceBar.setMax(25000);
                priceBar.setScrollbarFadingEnabled(true);
                priceBar.setIndicatorPopupEnabled(true);

                LinearLayout citiesFilter  = (LinearLayout) filterDialog.findViewById(R.id.cities_filter);
                citiesFilter.setVisibility(View.GONE);
                ButtonFlat apply = (ButtonFlat) filterDialog.findViewById(R.id.btn_filter);

                apply.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        first = 0;
                        searchFilter.setPage(first);
                        searchFilter.setSize(COUNT_ITEM_LOAD_MORE);
                        searchFilter.setDefaultOrder(true);
                        searchFilter.setDefaultSort(SortField.PRICE);

                        searchFilter.setMinPrice(Double.valueOf(priceBar.getProgress()));
                        searchFilter.setCategory(Category.valueOf(categ_selected));

                        loadingMore = false;
                        productsList.clear();
                        adapter.notifyDataSetChanged();
                        loadMoreDataTask = new LoadMoreDataTask();
                        loadMoreDataTask.execute();
                        filterDialog.dismiss();
                    }

                });
                filterDialog.show();
            }
        });

        loadMoreDataTask.execute();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(productsList != null && productsList.size() != 0);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    pullToRefreshDataTask = new PullToRefreshDataTask();
                    pullToRefreshDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    pullToRefreshDataTask = new PullToRefreshDataTask();
                    pullToRefreshDataTask.execute();
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
            Log.e(TAG, e.getMessage());
        }
    }

    private List<ProductVO> feedJson(boolean refresh) {
        if (refresh) {
            productsList.clear();
            resetSearch(refresh);
            searchFilter.setPage(0);
        }

        HttpEntity<SearchFilterVO> requestEntity = new HttpEntity<>(searchFilter);
        try {
            ResponseEntity<ProductsResponse> products = Utils.MyRestemplate.getInstance(getContext()).exchange(
                    getResources().getString(R.string.products_json_url),
                    HttpMethod.POST,
                    requestEntity, ProductsResponse.class);
            productsList.addAll(products.getBody().getPayload());
            productsCount = products.getBody().getTotalElements();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return productsList;
    }

    private void resetSearch(boolean refresh) {
        searchFilter.setSearchText("*");
        searchFilter.setPage(first);
        searchFilter.setSize(COUNT_ITEM_LOAD_MORE);
        searchFilter.setType(null);
        searchFilter.setUserId(null);
        searchFilter.setBrand(null);
        searchFilter.setCity(null);
        searchFilter.setMaxPrice(null);
        filterDialog = null;
        if (!refresh) {
            searchFilter.setDefaultSort(SortField.MOST_VIEWED);
            searchFilter.setDefaultOrder(null);
        }
        loadMoreDataTask = new LoadMoreDataTask();
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
