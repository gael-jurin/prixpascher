package org.nuvola.mobile.prixpascher.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.ads.MobileAds;

import org.nuvola.mobile.prixpascher.OfferActivity;
import org.nuvola.mobile.prixpascher.R;
import org.nuvola.mobile.prixpascher.adapters.DealOffersAdapter;
import org.nuvola.mobile.prixpascher.business.BadgeUtils;
import org.nuvola.mobile.prixpascher.business.EmptyRecyclerView;
import org.nuvola.mobile.prixpascher.business.UserSessionManager;
import org.nuvola.mobile.prixpascher.confs.constants;
import org.nuvola.mobile.prixpascher.dto.OfferVO;
import org.nuvola.mobile.prixpascher.models.User;
import org.nuvola.mobile.prixpascher.tasks.OfferFetchTask;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

import static org.nuvola.mobile.prixpascher.business.UserSessionManager.PRIVATE_MODE;
import static org.nuvola.mobile.prixpascher.business.UserSessionManager.SHARED_PREF_DATA;

public class NotifOffersFragment extends Fragment {
    String TAG = "NotifOffersFragment";

    ArrayList<OfferVO> deals = new ArrayList<>();
    DealOffersAdapter adapter;
    int user_id = 0, pastVisiblesItems, visibleItemCount, totalItemCount;

    @BindView(R.id.rv)
    EmptyRecyclerView rv;
    @BindView(R.id.btnClear)
    FloatingActionButton btnClear;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    AdmobRecyclerAdapterWrapper adapterWrapper;
    private AsyncTask<Void, Void, OfferVO> loadMoreDataTask;
    private SharedPreferences sharePre;

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
        sharePre = getContext().getSharedPreferences(SHARED_PREF_DATA, PRIVATE_MODE);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (loadMoreDataTask != null) {
            loadMoreDataTask.cancel(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        View view = inflater.inflate(R.layout.deals_container_layout, null);
        ButterKnife.bind(this, view);

        MobileAds.initialize(getContext(), getString(R.string.admob_publisher_id));

        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        adapter = new DealOffersAdapter(getActivity(), deals);

        adapterWrapper = new AdmobRecyclerAdapterWrapper(getContext(),
                getString(R.string.public_admob_unit_id));
        adapterWrapper.setAdapter(adapter);
        adapterWrapper.setLimitOfAds(5);
        adapterWrapper.setNoOfDataBetweenAds(2);
        adapterWrapper.setFirstAdIndex(3);
        rv.setAdapter(adapterWrapper);
        rv.setEmptyView(view.findViewById(R.id.empty_view));

        adapter.SetOnItemClickListener(new DealOffersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int originalContentPosition = adapterWrapper
                        .getAdapterCalculator().getOriginalContentPosition(position,
                                adapterWrapper.getFetchedAdsCount(), adapter.getItemCount());
                Intent intent = new Intent(getActivity(), OfferActivity.class);
                intent.putExtra(constants.COMMON_KEY, deals.get(originalContentPosition)
                        .getId());
                if (user_id == 0) {
                    startActivity(intent);
                } else {
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

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogConfirmDelete(getString(R.string.confirm_del));
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void showDialogConfirmDelete(String msg) {
        AlertDialog.Builder buidler = new AlertDialog.Builder(getActivity());
        buidler.setMessage(msg);
        buidler.setPositiveButton(getResources().getString(R.string.ok_label),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = sharePre.edit();
                        BadgeUtils.offers = new HashSet<>();
                        editor.putStringSet("OFFERS", BadgeUtils.offers);
                        editor.apply();
                        editor.commit();
                        deals.clear();
                        adapter.notifyDataSetChanged();
                        Intent broadcastIntent = new Intent("inbox");
                        getActivity().sendBroadcast(broadcastIntent);
                    }
                }).setNegativeButton(
                getResources().getString(R.string.cancel_label),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                });
        AlertDialog dialog = buidler.create();
        dialog.show();
    }

    private void loadUnreadNotifications() {
        UserSessionManager sessionManager = new UserSessionManager(getActivity());
        User user = sessionManager.getUserSession();
        if (user != null) {
            final Set<String> offers = sharePre.getStringSet("OFFERS", new HashSet<String>());
            for (final String pid : offers) {
                loadMoreDataTask = new OfferFetchTask(getResources().getString(
                        R.string.offer_root_json_url)
                        + pid,
                        new Handler() {
                            public void handleMessage(android.os.Message msg) {
                                Bundle bundle = msg.getData();
                                if (bundle.containsKey(pid)) {
                                    OfferVO product = (OfferVO) bundle
                                            .getSerializable(pid);

                                    deals.add(product);
                                    if (deals.size() == offers.size() && isAdded()) {
                                        adapter.notifyDataSetChanged();
                                        swipeRefreshLayout.setRefreshing(false);
                                    }
                                    btnClear.setVisibility(deals.size() == 0 ? View.GONE : View.VISIBLE);
                                }
                            }
                        });
                loadMoreDataTask.execute();
            }
            btnClear.setVisibility(deals.size() == 0 ? View.GONE : View.VISIBLE);
        }
    }
}
