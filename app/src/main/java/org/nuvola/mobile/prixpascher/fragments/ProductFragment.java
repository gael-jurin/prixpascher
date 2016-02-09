package org.nuvola.mobile.prixpascher.fragments;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import org.nuvola.mobile.prixpascher.DetailActivity;
import org.nuvola.mobile.prixpascher.R;
import org.nuvola.mobile.prixpascher.adapters.ProductsAdapter;
import org.nuvola.mobile.prixpascher.confs.constants;
import org.nuvola.mobile.prixpascher.models.Products;

public class ProductFragment extends Fragment{
    ArrayList<Products> products_list = new ArrayList<Products>();
    ProductsAdapter adapter;
    String TAG = "ProductsFragment";
    static InputStream is = null;
    static String jsonString = "";
    JSONObject searchFilter = new JSONObject();
    String query = null, tmpQuery = null;
    int COUNT_ITEM_LOAD_MORE = 5;
    int first = 0;
    LinearLayout loadMorePrg;
    boolean loadingMore = true;
    SwipeRefreshLayout swipeRefreshLayout;
    Button btnAll, btnSell, btnBuy;
    RecyclerView rv;
    int user_id = 0, user_post = 0, pastVisiblesItems, visibleItemCount, totalItemCount;

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
    }

    public static final ProductFragment newInstance() {
        // TODO Auto-generated constructor stub
        ProductFragment fragment = new ProductFragment();
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
        View view = null;
        // TODO Auto-generated method stub
        query = getResources().getString(R.string.products_json_url);
        Log.i("QUERY", query);
        view = inflater.inflate(
                R.layout.listview_endless_container_layout, null);
        rv = (RecyclerView) view.findViewById(R.id.rv);
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);


        loadMorePrg = (LinearLayout) view
                .findViewById(R.id.prgLoadMore);
        adapter = new ProductsAdapter(getActivity(), products_list);
        adapter.SetOnItemClickListener(new ProductsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (user_id == 0) {
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra(constants.COMMON_KEY, products_list.get(position)
                            .getId());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra(constants.COMMON_KEY, products_list.get(position)
                            .getId());
                    intent.putExtra(constants.USER_ID_KEY, user_id);
                    startActivity(intent);
                }
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
                        // tmpQuery += "&first=" + first + "&offset=" + COUNT_ITEM_LOAD_MORE;
                        try {
                            searchFilter.put("page", first);
                            searchFilter.put("size", COUNT_ITEM_LOAD_MORE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            new LoadMoreDataTask()
                                    .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } else {
                            new LoadMoreDataTask().execute();
                        }
                    }
            }
        } );



        if (getArguments() != null) {
            Bundle bundle = getArguments();
            if (bundle.containsKey(constants.TITLE_KEY)) {
                String title = bundle.getString(constants.TITLE_KEY);
                if (title != null && title != "") {
                    try {
                        title = URLEncoder.encode(title, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    try {
                        searchFilter.put("searchText", title);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (bundle.containsKey(constants.CATEGORIES_ID_KEY)) {
                int id = bundle.getInt(constants.CATEGORIES_ID_KEY);
                if (id != 0) {
                    query += "&categories_id=" + id;
                }
            }

            if (bundle.containsKey(constants.COUNTY_ID_KEY)) {
                int id = bundle.getInt(constants.COUNTY_ID_KEY);
                if (id != 0) {
                    query += "&county_id=" + id;
                }
            }

            if (bundle.containsKey(constants.CITY_ID_KEY)) {
                int id = bundle.getInt(constants.CITY_ID_KEY);
                if (id != 0) {
                    query += "&cities_id=" + id;
                }
            }

            if (bundle.containsKey(constants.USER_ID_KEY)) {
                int id = bundle.getInt(constants.USER_ID_KEY);
                if (id != 0) {
                    query += "&user_id=" + id;
                    user_id = id;

                }
            }

            if (bundle.containsKey(constants.USER_POST_KEY)) {
                int id = bundle.getInt(constants.USER_POST_KEY);
                if (id != 0) {
                    query += "&user_id=" + id;
                    user_post = id;
                }
            }

        }

        btnAll = (Button) view.findViewById(R.id.btnAll);
        btnSell = (Button) view.findViewById(R.id.btnSell);
        btnBuy = (Button) view.findViewById(R.id.btnBuy);
        setButtonFocus(btnAll, R.drawable.tab_categories_pressed);

        btnAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                setButtonFocus(btnAll, R.drawable.tab_categories_pressed);
                setButtonFocus(btnSell, R.drawable.tab_categories_normal);
                setButtonFocus(btnBuy, R.drawable.tab_categories_normal);
                first = 0;
                tmpQuery = query; // + "&first=" + first + "&offset=" + COUNT_ITEM_LOAD_MORE;
                try {
                    searchFilter.put("page", first);
                    searchFilter.put("size", COUNT_ITEM_LOAD_MORE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
                tmpQuery = query; // + "&first=" + first + "&offset=" + COUNT_ITEM_LOAD_MORE + "&aim=" + constants.BUY_VALUE;
                try {
                    searchFilter.put("page", first);
                    searchFilter.put("size", COUNT_ITEM_LOAD_MORE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
                tmpQuery = query; // + "&first=" + first + "&offset=" + COUNT_ITEM_LOAD_MORE + "&aim=" + constants.SELL_VALUE;
                try {
                    searchFilter.put("page", first);
                    searchFilter.put("size", COUNT_ITEM_LOAD_MORE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                loadingMore = false;
                products_list.clear();
                adapter.notifyDataSetChanged();
                new LoadMoreDataTask().execute();
            }
        });

        tmpQuery = query; // + "&first=" + first + "&offset=" + COUNT_ITEM_LOAD_MORE;
        new LoadMoreDataTask().execute();
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.activity_main_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (products_list != null && products_list.size() != 0) {
                    String pullQuery = query; // + "&sort_by=asc" + "&pull=" + products_list.get(0).getId();

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
        return view;
    }

    private void parse(JSONObject jsonObj, boolean append) {
        try {
            String id = jsonObj.getString(Products.TAG_ID);
            String name = jsonObj.getString(Products.TAG_TITLE);
            String link = jsonObj.getString(Products.TAG_LINK);
            String price = jsonObj.getString(Products.TAG_PRICE);
            String category = jsonObj.getString(Products.TAG_CATEGORY);
            String shopType = jsonObj.getString(Products.TAG_SHOP_TYPE);
            String shopName = jsonObj.getString(Products.TAG_SHOP_NAME);
            String thumb = jsonObj.getString(Products.TAG_IMAGE);
            String dateViewed = jsonObj.getString(Products.TAG_DATE_VIEWED);
            String dateTracking = jsonObj.getString(Products.TAG_DATE_TRACKING);
            String subCategory = jsonObj.getString(Products.TAG_SUB_CATEGORY);
            String productCategory = jsonObj.getString(Products.TAG_PRODUCT_CATEGORY);
            int views = jsonObj.getInt(Products.TAG_VIEWS);
            String promoted = jsonObj.getString(Products.TAG_PROMOTED);
            Products product = new Products();
            product.setId(id);
            product.setTitle(name);
            product.setLink(link);
            product.setPrice(price);
            product.setCategory(category);
            product.setProductCategory(productCategory);
            product.setShopName(shopName);
            product.setShopType(shopType);
            product.setImage(thumb);
            product.setViews(views);
            product.setViewed(dateViewed);
            product.setTrackingDate(dateTracking);
            product.setSubCategory(subCategory);
            product.setPromoted(promoted);
            if (append) {
                products_list.add(product);
            } else {
                products_list.add(0, product);
            }
        } catch (Exception e) {
            // TODO: handle exception
            loadingMore = false;
        }
    }

    private void parseAndAppend(String jsonString) {
        try {
            JSONObject jObj = new JSONObject(jsonString);
            JSONArray jsonArray = jObj.getJSONArray("payload");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                parse(jsonObj, true);
            }
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

    private void parseAndPrepend(String jsonString) {
        try {
            JSONObject jObj = new JSONObject(jsonString);
            JSONArray jsonArray = jObj.getJSONArray("payload");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                parse(jsonObj, false);
            }
            loadingMore = false;
            adapter.notifyDataSetChanged();
            loadMorePrg.setVisibility(View.GONE);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    private String feedJson(String pullQuery) {
        try {
            searchFilter.put("searchText", "*");
            searchFilter.put("page", first);
            searchFilter.put("size", COUNT_ITEM_LOAD_MORE);
            searchFilter.put("type", null);
            searchFilter.put("userId", null);
            searchFilter.put("brand", null);
            searchFilter.put("city", null);

            HttpPost httpPost;
            DefaultHttpClient httpClient = new DefaultHttpClient();
            if (pullQuery != null && !pullQuery.equalsIgnoreCase("")) {
                httpPost = new HttpPost(pullQuery);
            } else {
                httpPost = new HttpPost(tmpQuery);
            }

            httpPost.setHeader("Accept", "*/*");
            httpPost.setHeader("content-type", "application/json");
            httpPost.setHeader("Accept-language", "fr-FR,fr;q=0.8,en-US;q=0.6,en;q=0.4");

            StringEntity params = new StringEntity(searchFilter.toString());
            httpPost.setEntity(params);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, constants.STREAM_READER_CHARSET), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            jsonString = sb.toString();
            Log.i("JSON_FETCH_TAG", jsonString);
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        ;
        return jsonString.replaceAll("\\\\'", "'");
    }

    private class LoadMoreDataTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            if (isCancelled()) {
                return null;
            }
            return feedJson(null);
        }

        @Override
        protected void onPostExecute(String result) {
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


    private class PullToRefreshDataTask extends AsyncTask<Void, Void, String> {
        String pullQuery = null;

        public PullToRefreshDataTask(String pullQuery) {
            // TODO Auto-generated constructor stub
            this.pullQuery = pullQuery;
        }

        @Override
        protected String doInBackground(Void... params) {
            if (isCancelled()) {
                return null;
            }
            return feedJson(pullQuery);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            parseAndPrepend(result);
            swipeRefreshLayout.setRefreshing(false);
        }

       @Override
        protected void onCancelled() {
           swipeRefreshLayout.setRefreshing(false);
        }
    }
}
