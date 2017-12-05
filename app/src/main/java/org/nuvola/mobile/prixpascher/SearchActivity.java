
package org.nuvola.mobile.prixpascher;

import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.LinearLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import org.nuvola.mobile.prixpascher.adapters.ProductsAdapter;
import org.nuvola.mobile.prixpascher.confs.constants;
import org.nuvola.mobile.prixpascher.dto.ProductVO;
import org.nuvola.mobile.prixpascher.dto.SearchFilterVO;
import org.nuvola.mobile.prixpascher.models.PagedResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("NewApi")
public class SearchActivity extends ActionBarParentActivity {
    List<ProductVO> productsList = new ArrayList<>();
    SearchFilterVO searchFilter = new SearchFilterVO();
    ProductsAdapter adapter;
    String TAG = "ProductsActivity";

    int COUNT_ITEM_LOAD_MORE = 40;
    int first = 0, pastVisiblesItems, visibleItemCount, totalItemCount;
    boolean loadingMore = true;

    @Bind(R.id.activity_main_swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.rv) RecyclerView rv;
    @Bind(R.id.btnAll) Button btnAll;
    @Bind(R.id.btnBest) Button btnBest;
    @Bind(R.id.btnPromo) Button btnPromo;
    @Bind(R.id.prgLoadMore) LinearLayout loadMorePrg;
    @Bind(R.id.toolbar) Toolbar toolbar;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @SuppressWarnings("deprecation")
    private void setButtonFocus(Button btn, int drawable) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
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
    protected void onStart() {
        super.onStart();
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String title = intent.getStringExtra(SearchManager.QUERY);
            Log.i(TAG, title);
            if (title != null && title != "") {
                searchFilter.setSearchText(title);
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
                }
            }
        }

        btnAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                
                setButtonFocus(btnAll, R.drawable.tab_categories_pressed);
                setButtonFocus(btnBest, R.drawable.tab_categories_normal);
                setButtonFocus(btnPromo, R.drawable.tab_categories_normal);
                first = 0;

                searchFilter.setPage(first);
                searchFilter.setSize(COUNT_ITEM_LOAD_MORE);

                loadingMore = false;
                productsList.clear();
                adapter.notifyDataSetChanged();
                new LoadMoreDataTask().execute();
            }
        });

        btnPromo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                
                setButtonFocus(btnPromo, R.drawable.tab_categories_pressed);
                setButtonFocus(btnAll, R.drawable.tab_categories_normal);
                setButtonFocus(btnBest, R.drawable.tab_categories_normal);
                first = 0;
                searchFilter.setPage(first);
                searchFilter.setSize(COUNT_ITEM_LOAD_MORE);

                loadingMore = false;
                productsList.clear();
                adapter.notifyDataSetChanged();
                new LoadMoreDataTask().execute();
            }
        });

        btnBest.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                
                setButtonFocus(btnBest, R.drawable.tab_categories_pressed);
                setButtonFocus(btnAll, R.drawable.tab_categories_normal);
                setButtonFocus(btnPromo, R.drawable.tab_categories_normal);
                first = 0;

                searchFilter.setPage(first);
                searchFilter.setSize(COUNT_ITEM_LOAD_MORE);

                loadingMore = false;
                productsList.clear();
                adapter.notifyDataSetChanged();
                new LoadMoreDataTask().execute();
            }
        });

        adapter = new ProductsAdapter(this, productsList); // productsList
        adapter.SetOnItemClickListener(new ProductsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
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
                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                    loadingMore = false;
                    first += COUNT_ITEM_LOAD_MORE;

                    searchFilter.setPage(first);
                    searchFilter.setSize(COUNT_ITEM_LOAD_MORE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        new LoadMoreDataTask()
                                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        new LoadMoreDataTask().execute();
                    }
                }
            }
        });

        new LoadMoreDataTask().execute();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (productsList != null && productsList.size() != 0) {

                    searchFilter.setPage(first);
                    searchFilter.setSize(COUNT_ITEM_LOAD_MORE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        new PullToRefreshDataTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        new PullToRefreshDataTask().execute();
                    }
                } else {
                    swipeRefreshLayout.setRefreshing(false);
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
            
            e.printStackTrace();
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
            e.printStackTrace();
            // mPullToRefreshLayout.setRefreshComplete();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private List<ProductVO> feedJson() {
        try {
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);

            searchFilter.setSearchText("*");
            searchFilter.setPage(first);
            searchFilter.setSize(COUNT_ITEM_LOAD_MORE);
            searchFilter.setType(null);
            searchFilter.setUserId(null);
            searchFilter.setBrand(null);
            searchFilter.setCity(null);

            HttpEntity<SearchFilterVO> requestEntity = new HttpEntity<>(searchFilter, requestHeaders);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<PagedResponse> products = restTemplate.exchange(
                    getResources().getString(R.string.products_json_url),
                    HttpMethod.POST,
                    requestEntity, PagedResponse.class);

            productsList.addAll(products.getBody().getPayload());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return productsList;
    }

    private class LoadMoreDataTask extends AsyncTask<Void, Void, List<ProductVO>> {

        @Override
        protected List<ProductVO> doInBackground(Void... params) {
            if (isCancelled()) {
                return null;
            }
            return feedJson();
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
            
            Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
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
            return feedJson();
        }

        @Override
        protected void onPostExecute(List<ProductVO> result) {
            parseAndPrepend();
            swipeRefreshLayout.setRefreshing(false);
            // mPullToRefreshLayout.setRefreshComplete();
            super.onPostExecute(result);
        }

        @Override
        protected void onCancelled() {
            swipeRefreshLayout.setRefreshing(false);
            //mPullToRefreshLayout.setRefreshComplete();
        }
    }

}

