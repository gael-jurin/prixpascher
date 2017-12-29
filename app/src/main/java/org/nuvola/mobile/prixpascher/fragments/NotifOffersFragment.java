package org.nuvola.mobile.prixpascher.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clockbyte.admobadapter.AdmobRecyclerAdapterWrapper;
import com.google.android.gms.ads.MobileAds;

import org.nuvola.mobile.prixpascher.ProductActivity;
import org.nuvola.mobile.prixpascher.R;
import org.nuvola.mobile.prixpascher.adapters.DealProductsAdapter;
import org.nuvola.mobile.prixpascher.confs.constants;
import org.nuvola.mobile.prixpascher.dto.ProductVO;
import org.nuvola.mobile.prixpascher.tasks.ProductFetchTask;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.facebook.FacebookSdk.getApplicationContext;
import static org.nuvola.mobile.prixpascher.business.UserSessionManager.PRIVATE_MODE;
import static org.nuvola.mobile.prixpascher.business.UserSessionManager.SHARED_PREF_DATA;

public class NotifOffersFragment extends Fragment {
    String TAG = "NotifPromosFragment";

    ArrayList<ProductVO> deals = new ArrayList<>();
    DealProductsAdapter adapter;
    int user_id = 0, pastVisiblesItems, visibleItemCount, totalItemCount;

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    AdmobRecyclerAdapterWrapper adapterWrapper;

    public static NotifOffersFragment newInstance() {
        NotifOffersFragment fragment = new NotifOffersFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public NotifOffersFragment() {
        deals.clear();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        View view = inflater.inflate(R.layout.deals_container_layout, null);
        ButterKnife.bind(this, view);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-1074284300135896~4737246366");

        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        adapter = new DealProductsAdapter(getActivity(), deals);

        adapterWrapper = new AdmobRecyclerAdapterWrapper(getContext(),
                "ca-app-pub-1074284300135896/6213979568");
        adapterWrapper.setAdapter(adapter);
        adapterWrapper.setLimitOfAds(5);
        adapterWrapper.setNoOfDataBetweenAds(2);
        adapterWrapper.setFirstAdIndex(3);
        rv.setAdapter(adapterWrapper);

        adapter.SetOnItemClickListener(new DealProductsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (user_id == 0) {
                    Intent intent = new Intent(getActivity(), ProductActivity.class);
                    intent.putExtra(constants.COMMON_KEY, deals.get(position)
                            .getId());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), ProductActivity.class);
                    intent.putExtra(constants.COMMON_KEY, deals.get(position)
                            .getId());
                    intent.putExtra(constants.USER_ID_KEY, user_id);
                    startActivity(intent);
                }
            }
        });

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = llm.getChildCount();
                totalItemCount = llm.getItemCount();
                pastVisiblesItems = llm.findFirstVisibleItemPosition();
                if ((visibleItemCount + pastVisiblesItems) > totalItemCount) {
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(deals != null && deals.size() != 0);
                deals.clear();
                loadUnreadNotifications();
            }
        });
        loadUnreadNotifications();

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void loadUnreadNotifications() {
        SharedPreferences sharePre = getApplicationContext().getSharedPreferences(
                SHARED_PREF_DATA, PRIVATE_MODE);
        final Set<String> notifs = sharePre.getStringSet("OFFERS", new HashSet<String>());
        for (final String pid: notifs) {
            new ProductFetchTask(getResources().getString(
                    R.string.product_root_json_url)
                    + pid,
                        new Handler() {
                        public void handleMessage(android.os.Message msg) {
                            Bundle bundle = msg.getData();
                            if (bundle.containsKey(pid)) {
                                ProductVO product = (ProductVO) bundle
                                        .getSerializable(pid);

                                deals.add(product);
                                if (deals.size() == notifs.size() && isAdded()) {
                                    adapter.notifyDataSetChanged();
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            }
                        }}).execute();
        }
    }
}
