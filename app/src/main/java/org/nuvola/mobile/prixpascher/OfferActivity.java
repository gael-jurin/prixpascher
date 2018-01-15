package org.nuvola.mobile.prixpascher;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONObject;
import org.nuvola.mobile.prixpascher.adapters.DealOffersAdapter;
import org.nuvola.mobile.prixpascher.adapters.FullScreenImageAdapter;
import org.nuvola.mobile.prixpascher.business.UserSessionManager;
import org.nuvola.mobile.prixpascher.business.Utils;
import org.nuvola.mobile.prixpascher.confs.constants;
import org.nuvola.mobile.prixpascher.dto.OfferVO;
import org.nuvola.mobile.prixpascher.models.User;
import org.nuvola.mobile.prixpascher.tasks.OfferFetchTask;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint({ "NewApi", "HandlerLeak" })
public class OfferActivity extends ActionBarParentActivity {

    // @BindView(R.id.btn_edit_desc) ImageButton btnUpdateDesc;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.full_name) TextView fullName;
    @BindView(R.id.merchantAvt) ImageView merchantAvt;
    @BindView(R.id.views) TextView views;
    @BindView(R.id.city) TextView city;
    @BindView(R.id.price) TextView price;
    @BindView(R.id.pricetag) TextView pricetag;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.qty) TextView qty;
    @BindView(R.id.trackingDate) TextView trackingDate;
    @BindView(R.id.pager) ViewPager viewPager;
    @BindView(R.id.btnCallSeller) FloatingActionButton btnCallSeller;
    @BindView(R.id.btnAccept) FloatingActionButton btnAccept;
    @BindView(R.id.btnMenu) FloatingActionMenu btnMenu;
    // @BindView(R.id.ratingBarClick) RatingBar ratingBar;
    @BindView(R.id.concurrentOffers) RecyclerView concurrentOffers;

    private FullScreenImageAdapter imageAdapter;
    private DealOffersAdapter offersAdapter;
    private ProgressDialog dialog;
    private List<String> paths;
    private OfferVO offer;
    private String offerId;
    private String user_id;
    private String email, phoneText;
    private User logedUser;
    private EditText comment;
    private List<OfferVO> offerList  = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.offer_main_layout);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey(constants.USER_ID_KEY)) {
            // int user_id=bundle.getInt(constants.USER_ID_KEY);
            // ImageButton btnUpdateGallery = (ImageButton) findViewById(R.id.btn_update_gallery);
            /*btnUpdateGallery.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent(ProductActivity.this,
                            UpdateGalleryActivity.class);
                    intent.putExtra(constants.COMMON_KEY, offerId);
                    startActivity(intent);
                }
            });*/
        } else {
            // LinearLayout updateButtonWrapper = (LinearLayout) findViewById(R.id.update_button_wrapper);
            // updateButtonWrapper.setVisibility(LinearLayout.INVISIBLE);
            // updateButtonWrapper.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        }

        // Send email alert request();
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserSessionManager sessionManager = new UserSessionManager(OfferActivity.this);
                logedUser = sessionManager.getUserSession();
                if (logedUser != null) {
                    Utils.showCommingSoon(OfferActivity.this);
                } else {
                    Intent intent = new Intent(OfferActivity.this,
                            AuthenticationActivity.class);
                    startActivity(intent);
                }
            }
        });

        if (getIntent().getExtras() != null
                && getIntent().getExtras().containsKey(constants.COMMON_KEY)) {
            offerId = getIntent().getExtras().getString(constants.COMMON_KEY);
            new OfferFetchTask(getResources().getString(
                    R.string.offer_root_json_url)
                    + offerId, handler).execute();
        }

        final LinearLayoutManager llm = new LinearLayoutManager(OfferActivity.this);
        concurrentOffers.setLayoutManager(llm);
        offersAdapter = new DealOffersAdapter(this, offerList);
        offersAdapter.SetOnItemClickListener(new DealOffersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(OfferActivity.this, OfferActivity.class);
                intent.putExtra(constants.COMMON_KEY, offerList.get(position)
                        .getId());
                finish();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        concurrentOffers.setAdapter(offersAdapter);

        dialog = new ProgressDialog(this);
        dialog.setMessage(getResources().getString(R.string.loading));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        paths = new ArrayList<>();

        toolbar.setTitle(getResources().getString(R.string.compare_label));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // loadAd();
    }

    public static final String TAG = "OfferActivity";

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            Bundle bundle = msg.getData();
            if (bundle.containsKey(offerId)) {
                dialog.dismiss();
                offer = (OfferVO) bundle
                        .getSerializable(offerId);
                parseProduct(offer);
                toolbar.setTitle(offer.getShop().getName()
                        + " - " + offer.getProductAnnonce().getTitle());
                new LoadPendingOffersTask().execute();
            }
        };
    };

    Handler handlerRate = new Handler() {
        public void handleMessage(android.os.Message msg) {
            Bundle bundle = msg.getData();
            if (bundle.containsKey("rate")) {
                dialog.dismiss();
                String jsonString = bundle.getString("rate");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    float ratingText = Float.parseFloat(jsonObject
                            .getString("rating"));

                    // ratingBar.setRating(ratingText);
                } catch (Exception e) {
                    Log.e(TAG, "error parse rate");
                }
            }
        };
    };

    public void parseProduct(final OfferVO jsonObj) {
        try {
            if (jsonObj.getProductAnnonce().getImage() != null) {
                paths.add(jsonObj.getProductAnnonce().getImage());

                imageAdapter = new FullScreenImageAdapter(OfferActivity.this, paths);
                viewPager.setAdapter(imageAdapter);
                viewPager.setCurrentItem(0);
            }

            String fullNameText = jsonObj.getShop().getName();
            fullName.setText(fullNameText);
            merchantAvt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO : Display any merchant profile (This will be useful when including localisation and Stats)
                    /*Intent intent = new Intent(AnnounceActivity.this,
                            ProfileActivity.class);
                    intent.putExtra(constants.COMMON_KEY, jsonObj.getUser().getProviderUserId());
                    startActivity(intent);*/
                    if (jsonObj.getInfos() != null && jsonObj.getInfos().trim().length() > 5) {
                        AlertDialog.Builder buidler = new AlertDialog.Builder(OfferActivity.this);
                        buidler.setMessage(jsonObj.getInfos());
                        buidler.setPositiveButton(getResources().getString(R.string.ok_label),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog dialog = buidler.create();
                        dialog.show();
                    }
                }
            });

            user_id = "";
            email = "";
            phoneText = "";

            btnCallSeller.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (phoneText != null && phoneText.length() > 0) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneText, null));
                        startActivity(intent);
                    }
                }
            });

            String titleText = jsonObj.getProductAnnonce().getTitle();
            title.setText(titleText);
            String addressText = jsonObj.getProductAnnonce().getViews() + " nombres de vues";
            views.setText(addressText);
            city.setText("{faw-map_marker} " + jsonObj.getProductAnnonce().getVille().name().replaceAll("_", " "));
            String date = "Posté " + Utils.formatToYesterdayOrToday(jsonObj.getPosted());
            trackingDate.setText(date);

            double delta = offer.getProductAnnonce().getPrice() - Double.valueOf(offer.getTargetPrice());
            Double remise = (delta * 100) / offer.getProductAnnonce().getPrice();
            if (delta > 1) {
                pricetag.setText("- " +NumberFormat.getInstance(Locale.FRANCE).format(remise) + " %");
                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(400);
                anim.setStartOffset(10);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(Animation.INFINITE);
                pricetag.startAnimation(anim);
                pricetag.setVisibility(View.VISIBLE);
            } else {
                pricetag.setVisibility(View.INVISIBLE);
            }

            qty.setText("En Stock : " + String.valueOf(jsonObj.getStock()));

            Utils.MyPicasso.with(OfferActivity.this)
                    .load(jsonObj.getAnnonceur().getPhoto())
                    .placeholder(R.drawable.ic_avatar)
                    .into(merchantAvt);

            String priceText = NumberFormat.getInstance(Locale.FRANCE).format(jsonObj.getTargetPrice());
            if (priceText != null && !priceText.equalsIgnoreCase("")) {
                price.setText("Prix offert : " + priceText + " Dhs");
            } else {
                price.setText("0 Dhs");
            }

            WebView comment = (WebView) findViewById(R.id.comment);
            comment.getSettings().setJavaScriptEnabled(true);
            comment.setWebViewClient(new WebViewClient());
            WebSettings webSettings = comment.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);
            comment.getSettings().setJavaScriptEnabled(true);
            comment.getSettings().setDomStorageEnabled(true);
            //comment.getSettings().setPluginState(PluginState.ON);

            /*comment.loadUrl(getResources().getString(R.string.domain_url)
                    + "api/products_api/comment?id=" + offerId);*/
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @SuppressLint("NewApi")
    private void doAcceptOffer() {
        UserSessionManager sessionManager = new UserSessionManager(this);
        User user = sessionManager.getUserSession();
        if (user != null) {
            // TODO: Implements accept offer
        } else {
            Intent intent = new Intent(this, AuthenticationActivity.class);
            startActivity(intent);
        }
    }

    private class LoadPendingOffersTask extends AsyncTask<Void, Void, List<OfferVO>> {

        @Override
        protected List<OfferVO> doInBackground(Void... params) {
            if (isCancelled()) {
                return null;
            }
            return loadConcurrentOffers();
        }

        @Override
        protected void onPostExecute(List<OfferVO> result) {
            parseAndAppend();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onCancelled() {
        }
    }

    private void parseAndAppend() {
        try {
            offersAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private List<OfferVO> loadConcurrentOffers() {
        try {
            org.springframework.http.HttpEntity<OfferVO> requestEntity =
                    new org.springframework.http.HttpEntity<>(offer);
            byte[] encodedId = Base64.encode(offer.getProductAnnonce().getId().getBytes(), Base64.NO_WRAP);
            ResponseEntity<OfferVO[]> products = Utils.MyRestemplate.getInstance(this).exchange(
                    getResources().getString(R.string.annonce_offers_json_url) +
                            new String(encodedId),
                    HttpMethod.POST,
                    requestEntity, OfferVO[].class);

            for (OfferVO offer: products.getBody()) {
                if (!offer.getId().equals(offerId)) {
                    offerList.add(offer);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        return offerList;
    }

    public void showSuccessDialog() {
        AlertDialog.Builder buidler = new AlertDialog.Builder(this);
        buidler.setMessage(getResources()
                .getString(R.string.delete_successfull));
        buidler.setPositiveButton(getResources().getString(R.string.ok_label),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // stub
                        Intent intent = new Intent(OfferActivity.this,
                                HomeActivity.class);
                        intent.putExtra(constants.USER_ID_KEY, user_id);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                                | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog dialog = buidler.create();
        dialog.show();
    }

    public void showDialogConfirmDelete(String msg) {
        AlertDialog.Builder buidler = new AlertDialog.Builder(this);
        buidler.setMessage(msg);
        buidler.setPositiveButton(getResources().getString(R.string.ok_label),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // new Unpublish().start();
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

    private int getDrawable(Context context, String name)
    {
        return context.getResources().getIdentifier(name,
                "drawable", context.getPackageName());
    }
}