
package org.nuvola.mobile.prixpascher;

import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gc.materialdesign.views.ButtonFlat;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.nuvola.mobile.prixpascher.adapters.ProductsAdapter;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends ActionBarParentActivity {
    List<ProductVO> productsList = new ArrayList<>();
    long productsCount = 0;
    SearchFilterVO searchFilter = new SearchFilterVO();
    ProductsAdapter adapter;

    String TAG = "SearchActivity";
    int COUNT_ITEM_LOAD_MORE = 40;
    int first = 0, pastVisiblesItems, visibleItemCount, totalItemCount;

    boolean loadingMore = true;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv) RecyclerView rv;
    @BindView(R.id.btnAll) Button btnAll;
    @BindView(R.id.btnFilter) Button btnFilter;
    @BindView(R.id.btnPromo) Button btnPromo;
    @BindView(R.id.prgLoadMore) LinearLayout loadMorePrg;

    @BindView(R.id.toolbar) Toolbar toolbar;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    private DiscreteSeekBar priceBar;
    private Spinner citiesSpinner;
    private Spinner categoriesSpinner;
    private MaterialDialog filterDialog;
    private String categ_selected;
    private LoadMoreDataTask loadMoreDataTask = new LoadMoreDataTask();
    private PullToRefreshDataTask pullToRefreshDataTask = new PullToRefreshDataTask();

    @SuppressWarnings("deprecation")
    private void setButtonFocus(Button btn, int drawable) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            btn.setBackgroundDrawable(getResources().getDrawable(drawable));
        } else {
            btn.setBackground(getResources().getDrawable(drawable));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_endless_container_layout);
        ButterKnife.bind(this);
        setTitle(getResources().getString(R.string.result_label));

        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        handleIntent(getIntent());
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.btn_action_search);
        item.setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    @Override
    public void onStop() {
        super.onStop();

        loadMoreDataTask.cancel(true);
        pullToRefreshDataTask.cancel(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void handleIntent(Intent intent) {
        resetSearch();

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String title = intent.getStringExtra(SearchManager.QUERY);
            Log.i(TAG, title);
            if (title != null && title != "") {
                searchFilter.setSearchText(title);
                searchFilter.setDefaultSort(SortField.PRICE);
                searchFilter.setDefaultOrder(true);
            }
        }

        setButtonFocus(btnAll, R.drawable.tab_categories_pressed);

        final LinearLayoutManager llm = new LinearLayoutManager(SearchActivity.this);
        rv.setLayoutManager(llm);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(constants.TITLE_KEY)) {
                String title = bundle.getString(constants.TITLE_KEY);
                if (title != null && title != "") {
                    searchFilter.setSearchText(title);
                    searchFilter.setDefaultSort(SortField.PRICE);
                    searchFilter.setDefaultOrder(true);
                }
            }
        }

        btnAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                
                setButtonFocus(btnAll, R.drawable.tab_categories_pressed);
                setButtonFocus(btnFilter, R.drawable.tab_categories_normal);
                setButtonFocus(btnPromo, R.drawable.tab_categories_normal);
                resetSearch();
                first = 0;

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
                
                setButtonFocus(btnPromo, R.drawable.tab_categories_pressed);
                setButtonFocus(btnAll, R.drawable.tab_categories_normal);
                setButtonFocus(btnFilter, R.drawable.tab_categories_normal);
                resetSearch();
                first = 0;

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

                MaterialDialog.Builder filterDialogBuilder = new MaterialDialog.Builder(SearchActivity.this);
                filterDialogBuilder.customView(R.layout.filter_dialog_layout, true);

                priceBar = (DiscreteSeekBar) filterDialog.findViewById(R.id.priceBar);
                priceBar.setMax(25000);
                priceBar.setScrollbarFadingEnabled(true);
                priceBar.setIndicatorPopupEnabled(true);

                if (filterDialog == null) {
                    filterDialog = filterDialogBuilder.build();
                    categ_selected = Category.electromenager.name();

                    categoriesSpinner  = (Spinner) filterDialog.findViewById(R.id.categories_spinner);
                    final String[] category_name = Category.filterMobCategoryValues();
                    final ArrayAdapter<String> cadapter = new ArrayAdapter<>(
                            SearchActivity.this,
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

        adapter = new ProductsAdapter(this, productsList); // productsList
        adapter.SetOnItemClickListener(new ProductsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(SearchActivity.this, ProductActivity.class);
                intent.putExtra(constants.COMMON_KEY, productsList.get(position)
                        .getId());
                startActivity(intent);
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

        new LoadMoreDataTask().execute();
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
    }

    private void parseAndAppend() {
        try {
            loadingMore = false;
            adapter.notifyDataSetChanged();
            loadMorePrg.setVisibility(View.GONE);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
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
            loadingMore = false;
            Log.e(TAG, e.getMessage());
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private List<ProductVO> feedJson(boolean refresh) {
        try {
            if (refresh) {
                productsList.clear();
                resetSearch();
                searchFilter.setPage(0);
            }

            HttpEntity<SearchFilterVO> requestEntity = new HttpEntity<>(searchFilter);
            ResponseEntity<ProductsResponse> products = Utils.MyRestemplate.getInstance(this).exchange(
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
            parseAndAppend();
            super.onPostExecute(result);
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

    private OnItemClickListener listViewOnClick = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            
            Intent intent = new Intent(SearchActivity.this, ProductActivity.class);
            intent.putExtra(constants.COMMON_KEY, productsList.get(arg2)
                    .getId());
            startActivity(intent);
        }
    };


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
            parseAndPrepend();
            swipeRefreshLayout.setRefreshing(false);
            super.onPostExecute(result);
        }

        @Override
        protected void onCancelled() {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void resetSearch() {
        searchFilter.setPage(first);
        searchFilter.setSize(COUNT_ITEM_LOAD_MORE);
        searchFilter.setType(null);
        searchFilter.setUserId(null);
        searchFilter.setDefaultOrder(null);
    }

}

