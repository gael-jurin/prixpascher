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
import android.widget.Button;
import android.widget.LinearLayout;

import org.nuvola.mobile.prixpascher.AnnounceActivity;
import org.nuvola.mobile.prixpascher.R;
import org.nuvola.mobile.prixpascher.adapters.AnnouncesAdapter;
import org.nuvola.mobile.prixpascher.business.Utils;
import org.nuvola.mobile.prixpascher.confs.constants;
import org.nuvola.mobile.prixpascher.dto.ProductAnnonceVO;
import org.nuvola.mobile.prixpascher.dto.SearchFilterVO;
import org.nuvola.mobile.prixpascher.models.AnnounceType;
import org.nuvola.mobile.prixpascher.models.AnnouncesResponse;
import org.nuvola.mobile.prixpascher.models.Category;
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

public class AnnouncesFragment extends Fragment {
    public static String TAG = "AnnouncesFragment";

    List<ProductAnnonceVO> announces = new ArrayList<>();
    SearchFilterVO searchFilter = new SearchFilterVO();
    int COUNT_ITEM_LOAD_MORE = 40;
    int first = 0;
    boolean loadingMore = true;
    int user_id = 0, user_post = 0, pastVisiblesItems, visibleItemCount, totalItemCount;

    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv) RecyclerView rv;
    @BindView(R.id.btnSell) Button btnSell;
    @BindView(R.id.btnOffer) Button btnOffer;
    @BindView(R.id.prgLoadMore) LinearLayout loadMorePrg;
    AnnouncesAdapter adapter;
    static Toolbar toolbar;
    private LoadMoreDataTask loadMoreDataTask = new LoadMoreDataTask();
    private PullToRefreshDataTask pullToRefreshDataTask = new PullToRefreshDataTask();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStop() {
        super.onStop();

        loadMoreDataTask.cancel(true);
        pullToRefreshDataTask.cancel(true);
    }

    public static final AnnouncesFragment newInstance(Toolbar otoolbar) {
        AnnouncesFragment fragment = new AnnouncesFragment();
        toolbar = otoolbar;
        return fragment;
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
        searchFilter.setType(AnnounceType.COMMON_SELL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.announce_list_container_layout, null);
        ButterKnife.bind(this, view);
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        resetSearch();
        searchFilter.setType(null); // Only on create

        adapter = new AnnouncesAdapter(getActivity(), announces);
        adapter.SetOnItemClickListener(new AnnouncesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), AnnounceActivity.class);
                intent.putExtra(constants.COMMON_KEY, announces.get(position)
                        .getId());
                if (user_id == 0) {
                    startActivity(intent);
                } else {
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
                }
            }

            if (bundle.containsKey(constants.TYPE_KEY)) {
                String announceType = bundle.getString(constants.TYPE_KEY);
                if (announceType != null && announceType != "") {
                    AnnounceType value = AnnounceType.valueOf(announceType);
                    toolbar.setTitle(value.getLabel());
                    searchFilter.setType(value);
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

        setButtonFocus(btnSell, R.drawable.tab_categories_pressed);

        btnOffer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                setButtonFocus(btnOffer, R.drawable.tab_categories_pressed);
                setButtonFocus(btnSell, R.drawable.tab_categories_normal);
                first = 0;

                searchFilter.setPage(first);
                searchFilter.setSize(COUNT_ITEM_LOAD_MORE);
                searchFilter.setType(AnnounceType.OFFER_ASK);
                searchFilter.setDefaultSort(SortField.MOST_UPDATED);

                loadingMore = false;
                announces.clear();
                adapter.notifyDataSetChanged();
                loadMoreDataTask = new LoadMoreDataTask();
                loadMoreDataTask.execute();
            }
        });

        btnSell.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                setButtonFocus(btnSell, R.drawable.tab_categories_pressed);
                setButtonFocus(btnOffer, R.drawable.tab_categories_normal);
                first = 0;

                searchFilter.setPage(first);
                searchFilter.setSize(COUNT_ITEM_LOAD_MORE);
                searchFilter.setType(AnnounceType.COMMON_SELL);
                searchFilter.setDefaultSort(SortField.MOST_UPDATED);

                loadingMore = false;
                announces.clear();
                adapter.notifyDataSetChanged();
                loadMoreDataTask = new LoadMoreDataTask();
                loadMoreDataTask.execute();
            }
        });

        loadMoreDataTask.execute();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(announces != null && announces.size() != 0);

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

    private List<ProductAnnonceVO> feedJson(boolean refresh) {
        if (refresh) {
            announces.clear();
            resetSearch();
            searchFilter.setPage(0);
        }

        try {
            HttpEntity<SearchFilterVO> requestEntity = new HttpEntity<>(searchFilter);
            ResponseEntity<AnnouncesResponse> products = Utils.MyRestemplate.getInstance(getContext()).exchange(
                    getResources().getString(R.string.announces_json_url),
                    HttpMethod.POST,
                    requestEntity, AnnouncesResponse.class);

            announces.addAll(products.getBody().getPayload());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return announces;
    }

    private void resetSearch() {
        searchFilter.setSearchText("*");
        searchFilter.setPage(first);
        searchFilter.setSize(COUNT_ITEM_LOAD_MORE);
        // searchFilter.setType(AnnounceType.COMMON_SELL);
        searchFilter.setUserId(null);
        searchFilter.setBrand(null);
        searchFilter.setCity(null);
        searchFilter.setCategory(null);
        loadMoreDataTask = new LoadMoreDataTask();

    }

    private class LoadMoreDataTask extends AsyncTask<Void, Void, List<ProductAnnonceVO>> {

        @Override
        protected List<ProductAnnonceVO> doInBackground(Void... params) {
            if (isCancelled()) {
                return null;
            }
            return feedJson(false);
        }

        @Override
        protected void onPostExecute(List<ProductAnnonceVO> result) {
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


    private class PullToRefreshDataTask extends AsyncTask<Void, Void, List<ProductAnnonceVO>> {

        public PullToRefreshDataTask() {
            
        }

        @Override
        protected List<ProductAnnonceVO> doInBackground(Void... params) {
            if (isCancelled()) {
                return null;
            }
            return feedJson(true);
        }

        @Override
        protected void onPostExecute(List<ProductAnnonceVO> result) {
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
