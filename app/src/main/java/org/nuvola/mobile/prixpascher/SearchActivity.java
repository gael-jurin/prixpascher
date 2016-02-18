
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
import android.widget.ListView;
import org.nuvola.mobile.prixpascher.adapters.ProductsAdapter;
import org.nuvola.mobile.prixpascher.confs.constants;
import org.nuvola.mobile.prixpascher.dto.ProductVO;
import org.nuvola.mobile.prixpascher.dto.SearchFilterVO;
import org.nuvola.mobile.prixpascher.models.PagedResponse;
import org.nuvola.mobile.prixpascher.models.Products;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("NewApi")
public class SearchActivity extends ActionBarParentActivity {
    ArrayList<ProductVO> products_list = new ArrayList<>();
    SearchFilterVO searchFilter = new SearchFilterVO();
    ListView list;
    ProductsAdapter adapter;
    String TAG = "ProductsActivity";
    static String jsonString = "";
    String query = null, tmpQuery = null;
    int COUNT_ITEM_LOAD_MORE = 40;
    int first = 0, pastVisiblesItems, visibleItemCount, totalItemCount;
    boolean loadingMore = true;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView rv;
    Button btnAll, btnSell, btnBuy;
    LinearLayout loadMorePrg;
    Toolbar toolbar;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

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
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_endless_container_layout);
        setTitle(getResources().getString(R.string.result_label));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        query = getResources().getString(R.string.products_json_url);
        handleIntent(getIntent());
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        MenuItem item = menu.findItem(R.id.btn_action_search);
        item.setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        query = getResources().getString(R.string.products_json_url);
        handleIntent(intent);
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String title = intent.getStringExtra(SearchManager.QUERY);
            Log.i(TAG, title);
            try {
                title = URLEncoder.encode(title, "utf-8");
                Log.i(TAG, title);
                if (title != null && title != "") {
                    searchFilter.setSearchText(title);
                }
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        btnAll = (Button) findViewById(R.id.btnAll);
        btnSell = (Button) findViewById(R.id.btnSell);
        btnBuy = (Button) findViewById(R.id.btnBuy);
        setButtonFocus(btnAll, R.drawable.tab_categories_pressed);

        rv = (RecyclerView) findViewById(R.id.rv);
        final LinearLayoutManager llm = new LinearLayoutManager(SearchActivity.this);
        rv.setLayoutManager(llm);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(constants.TITLE_KEY)) {
                String title = bundle.getString(constants.TITLE_KEY);
                if (title != null && title != "") {
                    try {
                        title = URLEncoder.encode(title, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    query += "&title=" + title;
                }
            }
        }

        btnAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                setButtonFocus(btnAll, R.drawable.tab_categories_pressed);
                setButtonFocus(btnSell, R.drawable.tab_categories_normal);
                setButtonFocus(btnBuy, R.drawable.tab_categories_normal);
                first = 0;
                tmpQuery = query; // + "&page=" + first + "&size=" + COUNT_ITEM_LOAD_MORE;
                searchFilter.setPage(first);
                searchFilter.setSize(COUNT_ITEM_LOAD_MORE);

                loadingMore = false;
                products_list.clear();
                adapter.notifyDataSetChanged();
                new LoadMoreDataTask().execute();
            }
        });

        btnBuy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                setButtonFocus(btnBuy, R.drawable.tab_categories_pressed);
                setButtonFocus(btnAll, R.drawable.tab_categories_normal);
                setButtonFocus(btnSell, R.drawable.tab_categories_normal);
                first = 0;
                tmpQuery = query; // + "&page=" + first + "&size=" + COUNT_ITEM_LOAD_MORE + "&aim=" + constants.BUY_VALUE;
                searchFilter.setPage(first);
                searchFilter.setSize(COUNT_ITEM_LOAD_MORE);

                loadingMore = false;
                products_list.clear();
                adapter.notifyDataSetChanged();
                new LoadMoreDataTask().execute();
            }
        });

        btnSell.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                setButtonFocus(btnSell, R.drawable.tab_categories_pressed);
                setButtonFocus(btnAll, R.drawable.tab_categories_normal);
                setButtonFocus(btnBuy, R.drawable.tab_categories_normal);
                first = 0;
                tmpQuery = query; // + "&page=" + first + "&size=" + COUNT_ITEM_LOAD_MORE + "&aim=" + constants.SELL_VALUE;

                searchFilter.setPage(first);
                searchFilter.setSize(COUNT_ITEM_LOAD_MORE);

                loadingMore = false;
                products_list.clear();
                adapter.notifyDataSetChanged();
                new LoadMoreDataTask().execute();
            }
        });

        loadMorePrg = (LinearLayout) findViewById(R.id.prgLoadMore);
        adapter = new ProductsAdapter(this, products_list); // products_list
        adapter.SetOnItemClickListener(new ProductsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
                intent.putExtra(constants.COMMON_KEY, products_list.get(position)
                        .getId());
                startActivity(intent);
            }
        });
        rv.setAdapter(adapter);
        rv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = llm.getChildCount();
                totalItemCount = llm.getItemCount();
                pastVisiblesItems = llm.findFirstVisibleItemPosition();
                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                    loadingMore = false;
                    Log.v("...", "Last Item Wow !");
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

        tmpQuery = query + "&page=" + first + "&size="
                + COUNT_ITEM_LOAD_MORE;
        new LoadMoreDataTask().execute();


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (products_list != null && products_list.size() != 0) {
                    String pullQuery = query; //+ "&page=" + 0 + "&size="
                            //+ COUNT_ITEM_LOAD_MORE + "&sort_by=asc" + "&pull="
                            //+ products_list.get(0).getId();
                    searchFilter.setPage(first);
                    searchFilter.setSize(COUNT_ITEM_LOAD_MORE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        new PullToRefreshDataTask(pullQuery)
                                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        new PullToRefreshDataTask(pullQuery).execute();
                    }
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    /*private void parse(JSONObject jsonObj, boolean append) {
        try {
            Log.i("x", "xxxxxxxxxxx");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            loadingMore = true;
        }
    }*/

    private void parseAndAppend(String jsonString) {
        try {
            /*JSONObject jObj = new JSONObject(jsonString);
            JSONArray jsonArray = jObj.getJSONArray("payload");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                parse(jsonObj, true);
            }*/
            loadingMore = false;
            adapter.notifyDataSetChanged();
            loadMorePrg.setVisibility(View.GONE);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            loadingMore = true;
            loadMorePrg.setVisibility(View.GONE);
        }
    }

    private void parseAndPrepend(List<Products> productsList) {
        try {
            /*JSONObject jObj = new JSONObject(jsonString);
            JSONArray jsonArray = jObj.getJSONArray("payload");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                parse(jsonObj, false);
            }*/
            loadingMore = false;
            adapter.notifyDataSetChanged();
            loadMorePrg.setVisibility(View.GONE);
        } catch (Exception e) {
            // TODO: handle exception
            loadingMore = false;
            e.printStackTrace();
            //mPullToRefreshLayout.setRefreshComplete();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private List<ProductVO> feedJson(String pullQuery) {
        try {
            HttpHeaders requestHeaders = new HttpHeaders();

            // Sending a JSON or XML object i.e. "application/json" or "application/xml"
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);

            searchFilter.setSearchText("*");
            searchFilter.setPage(first);
            searchFilter.setSize(COUNT_ITEM_LOAD_MORE);
            searchFilter.setType(null);
            searchFilter.setUserId(null);
            searchFilter.setBrand(null);
            searchFilter.setCity(null);

            // Populate the Message object to serialize and headers in an
            // HttpEntity object to use for the request
            HttpEntity<SearchFilterVO> requestEntity = new HttpEntity<>(searchFilter, requestHeaders);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<PagedResponse> products = restTemplate.exchange(
                    getResources().getString(R.string.products_json_url),
                    HttpMethod.POST,
                    requestEntity, PagedResponse.class);

            products_list.addAll(products.getBody().getPayload());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return products_list;
    }

    private class LoadMoreDataTask extends AsyncTask<Void, Void, List<ProductVO>> {

        @Override
        protected List<ProductVO> doInBackground(Void... params) {
            if (isCancelled()) {
                return null;
            }
            return feedJson(null);
        }

        @Override
        protected void onPostExecute(List<ProductVO> result) {
            parseAndAppend(jsonString);
            super.onPostExecute(result);
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

    private OnItemClickListener listViewOnClick = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
            intent.putExtra(constants.COMMON_KEY, products_list.get(arg2)
                    .getId());
            startActivity(intent);
        }
    };


    private class PullToRefreshDataTask extends AsyncTask<Void, Void, List<Products>> {
        String pullQuery = null;

        public PullToRefreshDataTask(String pullQuery) {
            // TODO Auto-generated constructor stub
            this.pullQuery = pullQuery;
        }

        @Override
        protected List<Products> doInBackground(Void... params) {
            if (isCancelled()) {
                return null;
            }
            return null; // feedJson(pullQuery);
        }

        @Override
        protected void onPostExecute(List<Products> result) {
            parseAndPrepend(result);
            swipeRefreshLayout.setRefreshing(false);
            //mPullToRefreshLayout.setRefreshComplete();
            super.onPostExecute(result);
        }

        @Override
        protected void onCancelled() {
            swipeRefreshLayout.setRefreshing(false);
            //mPullToRefreshLayout.setRefreshComplete();
        }
    }

}

