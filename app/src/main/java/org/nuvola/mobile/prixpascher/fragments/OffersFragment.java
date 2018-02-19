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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.clockbyte.admobadapter.AdmobRecyclerAdapterWrapper;
import com.google.android.gms.ads.MobileAds;

import org.nuvola.mobile.prixpascher.OfferActivity;
import org.nuvola.mobile.prixpascher.R;
import org.nuvola.mobile.prixpascher.adapters.DealOffersAdapter;
import org.nuvola.mobile.prixpascher.business.EmptyRecyclerView;
import org.nuvola.mobile.prixpascher.business.UserSessionManager;
import org.nuvola.mobile.prixpascher.business.Utils;
import org.nuvola.mobile.prixpascher.confs.constants;
import org.nuvola.mobile.prixpascher.dto.OfferVO;
import org.nuvola.mobile.prixpascher.dto.SearchFilterVO;
import org.nuvola.mobile.prixpascher.dto.UserVO;
import org.nuvola.mobile.prixpascher.models.AnnounceType;
import org.nuvola.mobile.prixpascher.models.Category;
import org.nuvola.mobile.prixpascher.models.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OffersFragment extends Fragment {
    static Toolbar toolbar;

    public static String TAG = "OffersFragment";
    List<OfferVO> offers = new ArrayList<>();
    long offersCount = 0;
    SearchFilterVO searchFilter = new SearchFilterVO();
    int COUNT_ITEM_LOAD_MORE = 40;
    int first = 0;
    boolean loadingMore = true;

    int user_id = 0, user_post = 0, pastVisiblesItems, visibleItemCount, totalItemCount;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv)
    EmptyRecyclerView rv;
    @BindView(R.id.prgLoadMore) LinearLayout loadMorePrg;

    private DealOffersAdapter adapter;
    private AdmobRecyclerAdapterWrapper adapterWrapper;
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

    public static final OffersFragment newInstance(Toolbar otoolbar) {
        OffersFragment fragment = new OffersFragment();
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
        View view = inflater.inflate(R.layout.offers_container_layout, null);
        ButterKnife.bind(this, view);
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        MobileAds.initialize(getContext(), getString(R.string.admob_publisher_id));

        adapter = new DealOffersAdapter(getActivity(), offers);
        adapter.SetOnItemClickListener(new DealOffersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int originalContentPosition = adapterWrapper
                        .getAdapterCalculator().getOriginalContentPosition(position,
                                adapterWrapper.getFetchedAdsCount(), adapter.getItemCount());
                Intent intent = new Intent(getActivity(), OfferActivity.class);
                intent.putExtra(constants.COMMON_KEY, offers.get(originalContentPosition)
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

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = llm.getChildCount();
                totalItemCount = llm.getItemCount();
                pastVisiblesItems = llm.findFirstVisibleItemPosition();
                int page = visibleItemCount + pastVisiblesItems;
                if (page == totalItemCount && page <= offersCount) {
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
                if (id != "0") {
                    searchFilter.setUserId(id);
                }
            }

            if (bundle.containsKey(constants.USER_POST_KEY)) {
                int id = bundle.getInt(constants.USER_POST_KEY);
                if (id != 0) {
                    // query += "&user_id=" + id;
                    user_post = id;
                }
            }

        }

        loadMoreDataTask.execute();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(offers != null && offers.size() != 0);

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

    private List<OfferVO> feedJson(boolean refresh) {
        if (refresh) {
            offers.clear();
            searchFilter.setPage(0);
        }

        try {
            UserSessionManager sessionManager = new UserSessionManager(getActivity());
            User user = sessionManager.getUserSession();
            if (user != null) {
                UserVO userVO = new UserVO();
                userVO.setId(user.getId());
                userVO.setProviderUserId(user.getFbId());
                userVO.setEmail(user.getEmail());
                HttpEntity<UserVO> requestEntity = new HttpEntity<>(userVO);

                ResponseEntity<OfferVO[]> offerList = Utils.MyRestemplate.getInstance(getActivity()).exchange(
                        getResources().getString(R.string.offers_user_json_url),
                        HttpMethod.POST,
                        requestEntity, OfferVO[].class);

                offers.addAll(Arrays.asList(offerList.getBody()));
                offersCount = offerList.getBody().length;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return offers;
    }

    private class LoadMoreDataTask extends AsyncTask<Void, Void, List<OfferVO>> {

        @Override
        protected List<OfferVO> doInBackground(Void... params) {
            if (isCancelled()) {
                return null;
            }
            return feedJson(true);
        }

        @Override
        protected void onPostExecute(List<OfferVO> result) {
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


    private class PullToRefreshDataTask extends AsyncTask<Void, Void, List<OfferVO>> {

        public PullToRefreshDataTask() {
            
        }

        @Override
        protected List<OfferVO> doInBackground(Void... params) {
            if (isCancelled()) {
                return null;
            }
            return feedJson(true);
        }

        @Override
        protected void onPostExecute(List<OfferVO> result) {
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
