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
import org.nuvola.mobile.prixpascher.AnnounceActivity;
import org.nuvola.mobile.prixpascher.R;
import org.nuvola.mobile.prixpascher.adapters.AnnouncesAdapter;
import org.nuvola.mobile.prixpascher.business.EmptyRecyclerView;
import org.nuvola.mobile.prixpascher.business.Utils;
import org.nuvola.mobile.prixpascher.confs.constants;
import org.nuvola.mobile.prixpascher.dto.ProductAnnonceVO;
import org.nuvola.mobile.prixpascher.dto.SearchFilterVO;
import org.nuvola.mobile.prixpascher.models.AnnounceType;
import org.nuvola.mobile.prixpascher.models.AnnouncesResponse;
import org.nuvola.mobile.prixpascher.models.Category;
import org.nuvola.mobile.prixpascher.models.City;
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

public class AnnouncesFragment extends Fragment {
    static Toolbar toolbar;

    public static String TAG = "AnnouncesFragment";
    List<ProductAnnonceVO> announces = new ArrayList<>();
    long announcesCount = 0;
    SearchFilterVO searchFilter = new SearchFilterVO();
    int COUNT_ITEM_LOAD_MORE = 40;
    int first = 0;
    boolean loadingMore = true;

    int user_id = 0, user_post = 0, pastVisiblesItems, visibleItemCount, totalItemCount;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv)
    EmptyRecyclerView rv;
    @BindView(R.id.btnSell) Button btnSell;
    @BindView(R.id.btnOffer) Button btnOffer;
    @BindView(R.id.btnFilter) Button btnFilter;
    @BindView(R.id.prgLoadMore) LinearLayout loadMorePrg;
    private AnnouncesAdapter adapter;
    private AdmobRecyclerAdapterWrapper adapterWrapper;
    private DiscreteSeekBar priceBar;
    private Spinner citiesSpinner;
    private Spinner categoriesSpinner;
    private MaterialDialog filterDialog;
    private String categ_selected;
    private LoadMoreDataTask loadMoreDataTask = new LoadMoreDataTask();
    private PullToRefreshDataTask pullToRefreshDataTask = new PullToRefreshDataTask();
    private String city_selected;

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

        MobileAds.initialize(getApplicationContext(), getString(R.string.admob_publisher_id));

        resetSearch();
        searchFilter.setType(null); // Only on create

        /*final ViewTarget targetS = new ViewTarget(view.findViewById(R.id.btnSell));
        final ViewTarget targetD = new ViewTarget(view.findViewById(R.id.btnOffer));
        new ShowcaseView.Builder(getActivity())
                .setTarget(targetD)
                .setContentTitle("Afficher ...")
                .setContentText("Les demandes d'achat et devis")
                .hideOnTouchOutside()
                .replaceEndButton((Button) view.findViewById(R.id.btnOffer))
                .singleShot(3)
                .build();*/

        adapter = new AnnouncesAdapter(getActivity(), announces);
        adapter.SetOnItemClickListener(new AnnouncesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int originalContentPosition = adapterWrapper
                        .getAdapterCalculator().getOriginalContentPosition(position,
                                adapterWrapper.getFetchedAdsCount(), adapter.getItemCount());
                Intent intent = new Intent(getActivity(), AnnounceActivity.class);
                intent.putExtra(constants.COMMON_KEY, announces.get(originalContentPosition)
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
                if (page == totalItemCount && page <= announcesCount) {
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

        setButtonFocus(btnSell, R.drawable.tab_categories_pressed);

        btnOffer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonFocus(btnOffer, R.drawable.tab_categories_pressed);
                setButtonFocus(btnSell, R.drawable.tab_categories_normal);
                setButtonFocus(btnFilter, R.drawable.tab_categories_normal);
                if (filterDialog != null) {
                    resetSearch();
                }
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

        btnFilter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonFocus(btnFilter, R.drawable.tab_categories_pressed);
                setButtonFocus(btnOffer, R.drawable.tab_categories_normal);
                setButtonFocus(btnSell, R.drawable.tab_categories_normal);

                MaterialDialog.Builder filterDialogBuilder = new MaterialDialog.Builder(getActivity());
                filterDialogBuilder.customView(R.layout.filter_dialog_layout, true);

                if (filterDialog == null) {
                    filterDialog = filterDialogBuilder.build();
                    city_selected = City.__.name();
                    categ_selected = Category.___.name();

                    categoriesSpinner  = (Spinner) filterDialog.findViewById(R.id.categories_spinner);
                    final String[] category_name = Category.filterMobCategoryValues();
                    ArrayAdapter<String> cadapter = new ArrayAdapter<>(
                            getActivity(),
                            android.R.layout.simple_list_item_1, category_name);
                    categoriesSpinner.setAdapter(cadapter);
                    categoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                            categ_selected = category_name[arg2];
                            categ_selected = categ_selected.replaceAll(" ", "_");
                        }
                        @Override
                        public void onNothingSelected(
                                AdapterView<?> arg0) {
                        }
                    });
                    LinearLayout citiesFilter  = (LinearLayout) filterDialog.findViewById(R.id.cities_filter);
                    citiesFilter.setVisibility(View.VISIBLE);
                    citiesSpinner = (Spinner) filterDialog.findViewById(R.id.cities_spinner);
                    final String[] city_name = City.names();
                    ArrayAdapter<String> cityadapter = new ArrayAdapter<>(
                            getActivity(),
                            android.R.layout.simple_list_item_1, city_name);
                    citiesSpinner.setAdapter(cityadapter);
                    citiesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                            city_selected = city_name[arg2];
                            city_selected = city_selected.replaceAll(" ", "_");
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
                        searchFilter.setCity(city_selected.equals(City.__.name()) ?
                                null : City.valueOf(city_selected));
                        searchFilter.setCategory(categ_selected.equals(Category.___.name()) ?
                                null : Category.valueOf(categ_selected));

                        loadingMore = false;
                        announces.clear();
                        adapter.notifyDataSetChanged();
                        loadMoreDataTask = new LoadMoreDataTask();
                        loadMoreDataTask.execute();
                        filterDialog.dismiss();
                    }

                });
                filterDialog.show();
            }
        });

        btnSell.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonFocus(btnSell, R.drawable.tab_categories_pressed);
                setButtonFocus(btnFilter, R.drawable.tab_categories_normal);
                setButtonFocus(btnOffer, R.drawable.tab_categories_normal);
                if (filterDialog != null) {
                    resetSearch();
                }
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
            if (searchFilter.getUserId() == null) {
                ResponseEntity<AnnouncesResponse> products = Utils.MyRestemplate.getInstance(getContext()).exchange(
                        getResources().getString(R.string.announces_json_url),
                        HttpMethod.POST,
                        requestEntity, AnnouncesResponse.class);
                announces.addAll(products.getBody().getPayload());
                announcesCount = products.getBody().getTotalElements();
            } else {
                ResponseEntity<AnnouncesResponse> products = Utils.MyRestemplate.getInstance(getContext()).exchange(
                        getResources().getString(R.string.user_announces_url),
                        HttpMethod.POST,
                        requestEntity, AnnouncesResponse.class);
                announces.addAll(products.getBody().getPayload());
                announcesCount = products.getBody().getTotalElements();
            }
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
        filterDialog = null;
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
