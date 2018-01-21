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

import org.nuvola.mobile.prixpascher.AnnounceActivity;
import org.nuvola.mobile.prixpascher.R;
import org.nuvola.mobile.prixpascher.adapters.DealDevisAdapter;
import org.nuvola.mobile.prixpascher.business.BadgeUtils;
import org.nuvola.mobile.prixpascher.business.UserSessionManager;
import org.nuvola.mobile.prixpascher.confs.constants;
import org.nuvola.mobile.prixpascher.dto.ProductAnnonceVO;
import org.nuvola.mobile.prixpascher.models.User;
import org.nuvola.mobile.prixpascher.receivers.NotificationFiredReceiver;
import org.nuvola.mobile.prixpascher.tasks.AnnounceFetchTask;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.facebook.FacebookSdk.getApplicationContext;
import static org.nuvola.mobile.prixpascher.business.UserSessionManager.PRIVATE_MODE;
import static org.nuvola.mobile.prixpascher.business.UserSessionManager.SHARED_PREF_DATA;

public class NotifDevisFragment extends Fragment {
    public static NotificationFiredReceiver.NotificationFiredListener notificationFiredListener;

    String TAG = "NotifDevisFragment";

    ArrayList<ProductAnnonceVO> deals = new ArrayList<>();
    DealDevisAdapter adapter;
    int user_id = 0, pastVisiblesItems, visibleItemCount, totalItemCount;

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.btnClear)
    FloatingActionButton btnClear;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    AdmobRecyclerAdapterWrapper adapterWrapper;
    private AsyncTask<Void, Void, ProductAnnonceVO> loadMoreDataTask;
    private SharedPreferences sharePre = getApplicationContext().getSharedPreferences(
            SHARED_PREF_DATA, PRIVATE_MODE);

    public static NotifDevisFragment newInstance() {
        NotifDevisFragment fragment = new NotifDevisFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public NotifDevisFragment() {
        deals.clear();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-1074284300135896~4737246366");

        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        adapter = new DealDevisAdapter(getActivity(), deals);

        adapterWrapper = new AdmobRecyclerAdapterWrapper(getContext(),
                "ca-app-pub-1074284300135896/6213979568");
        adapterWrapper.setAdapter(adapter);
        adapterWrapper.setLimitOfAds(5);
        adapterWrapper.setNoOfDataBetweenAds(3);
        adapterWrapper.setFirstAdIndex(3);
        rv.setAdapter(adapterWrapper);

        adapter.SetOnItemClickListener(new DealDevisAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int originalContentPosition = adapterWrapper
                        .getAdapterCalculator().getOriginalContentPosition(position,
                                adapterWrapper.getFetchedAdsCount(), adapter.getItemCount());
                if (user_id == 0) {
                    Intent intent = new Intent(getActivity(), AnnounceActivity.class);
                    intent.putExtra(constants.COMMON_KEY, deals.get(originalContentPosition)
                            .getId());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), AnnounceActivity.class);
                    intent.putExtra(constants.COMMON_KEY, deals.get(originalContentPosition)
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
                        BadgeUtils.devis = new HashSet<>();
                        editor.putStringSet("DEVIS", BadgeUtils.devis);
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
            SharedPreferences sharePre = getApplicationContext().getSharedPreferences(
                    SHARED_PREF_DATA, PRIVATE_MODE);
            final Set<String> notifs = sharePre.getStringSet("DEVIS", new HashSet<String>());
            for (final String pid : notifs) {
                loadMoreDataTask =  new AnnounceFetchTask(getResources().getString(
                        R.string.announce_root_json_url)
                        + pid,
                        new Handler() {
                            public void handleMessage(android.os.Message msg) {
                                Bundle bundle = msg.getData();
                                if (bundle.containsKey(pid)) {
                                    ProductAnnonceVO annonce = (ProductAnnonceVO) bundle
                                            .getSerializable(pid);
                                    deals.add(annonce);
                                    if (deals.size() == notifs.size() && isAdded()) {
                                        adapter.notifyDataSetChanged();
                                        swipeRefreshLayout.setRefreshing(false);
                                    }
                                }
                            }
                        });
                loadMoreDataTask.execute();
            }
        }
    }
}
