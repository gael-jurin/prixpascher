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
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gc.materialdesign.views.ButtonFlat;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.robertsimoes.shareable.Shareable;

import org.json.JSONObject;
import org.nuvola.mobile.prixpascher.adapters.FullScreenImageAdapter;
import org.nuvola.mobile.prixpascher.adapters.SimilarProductsAdapter;
import org.nuvola.mobile.prixpascher.business.UserSessionManager;
import org.nuvola.mobile.prixpascher.business.Utils;
import org.nuvola.mobile.prixpascher.confs.constants;
import org.nuvola.mobile.prixpascher.dto.PriceHistoryVO;
import org.nuvola.mobile.prixpascher.dto.ProductAnnonceVO;
import org.nuvola.mobile.prixpascher.dto.ProductVO;
import org.nuvola.mobile.prixpascher.dto.ReviewVO;
import org.nuvola.mobile.prixpascher.dto.UserVO;
import org.nuvola.mobile.prixpascher.models.Category;
import org.nuvola.mobile.prixpascher.models.User;
import org.nuvola.mobile.prixpascher.tasks.ProductFetchTask;
import org.nuvola.mobile.prixpascher.tasks.ServiceType;
import org.nuvola.mobile.prixpascher.tasks.ServicesTask;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint({ "NewApi", "HandlerLeak" })
public class ProductActivity extends ActionBarParentActivity {

    // @BindView(R.id.btn_edit_desc) ImageButton btnUpdateDesc;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.full_name) TextView fullName;
    @BindView(R.id.address) TextView address;
    @BindView(R.id.price) TextView price;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.pricetag) TextView pricetag;
    @BindView(R.id.shop) ImageView shop;
    @BindView(R.id.trackingDate) TextView trackingDate;
    @BindView(R.id.pager) ViewPager viewPager;
    @BindView(R.id.btnAddToCart) FloatingActionButton btnAddToCart;
    @BindView(R.id.btnAlert) FloatingActionButton btnAlert;
    @BindView(R.id.btnAskDevis) FloatingActionButton btnAskDevis;
    @BindView(R.id.btnShare) FloatingActionButton btnShare;
    @BindView(R.id.btnMenu) FloatingActionMenu btnMenu;
    @BindView(R.id.ratingBarClick) RatingBar ratingBar;
    @BindView(R.id.similarProducts) RecyclerView similarProducts;

    private FullScreenImageAdapter imageAdapter;
    private SimilarProductsAdapter productsAdapter;
    private ProgressDialog dialog;
    private List<String> paths;
    private List<ProductVO> productsList = new ArrayList<>();
    private ProductVO product;
    private String productId;
    private String user_id;
    private User logedUser;
    private ImageView avt;
    private EditText comment;
    private GraphView graph;
    private LineGraphSeries<DataPointInterface> series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.product_main_layout);
        ButterKnife.bind(this);

        MaterialDialog.Builder priceDialogBuilder = new MaterialDialog.Builder(ProductActivity.this);
        priceDialogBuilder.customView(R.layout.graph_prices_layout, true);
        final MaterialDialog priceDialog = priceDialogBuilder.build();
        graph = (GraphView) priceDialog.findViewById(R.id.pricegraph);

        Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey(constants.USER_ID_KEY)) {
            // int user_id=bundle.getInt(constants.USER_ID_KEY);
            // ImageButton btnUpdateGallery = (ImageButton) findViewById(R.id.btn_update_gallery);
            /*btnUpdateGallery.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(ProductActivity.this,
                            UpdateGalleryActivity.class);
                    intent.putExtra(constants.COMMON_KEY, productId);
                    startActivity(intent);
                }
            });*/
        } else {
            // LinearLayout updateButtonWrapper = (LinearLayout) findViewById(R.id.update_button_wrapper);
            // updateButtonWrapper.setVisibility(LinearLayout.INVISIBLE);
            // updateButtonWrapper.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        }

        pricetag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar mCalendar = Calendar.getInstance();

                series = new LineGraphSeries<>();
                List<PriceHistoryVO> prices = new ArrayList<>();
                prices.addAll(product.getPrices());
                prices.add(new PriceHistoryVO(product.getPrice(), product.getTrackingDate()));

                Collections.sort(prices, PriceHistoryVO.ASC_ORDER);
                graph.removeAllSeries();
                series.resetData(new DataPoint[]{});

                for (PriceHistoryVO historyVO: prices) {
                    mCalendar.setTime(historyVO.getPriceDate());
                    series.appendData(new DataPoint(mCalendar.getTime(),
                            historyVO.getPrice()), true, 12);
                }

                graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                    @Override
                    public String formatLabel(double value, boolean isValueX) {
                        if (isValueX) {
                            mCalendar.setTimeInMillis((long) value);
                            return "" ; //new SimpleDateFormat("MM/yy").format(mCalendar.getTime());
                        } else {
                            return NumberFormat.getInstance(Locale.FRANCE).format(value) + " Dhs";
                        }
                    }
                });

                graph.getViewport().setMinX(series.getLowestValueX());
                graph.getViewport().setMaxX(series.getHighestValueX());
                // graph.getViewport().setMinY(series.getLowestValueY());
                // graph.getViewport().setMaxY(series.getHighestValueY());

                graph.getViewport().setXAxisBoundsManual(true);

                // activate horizontal zooming and scrolling
                graph.getViewport().setScalable(true);
                graph.getViewport().setScrollable(true);
                // graph.getViewport().setScalableY(true);
                // graph.getViewport().setScrollableY(true);

                // graph.getGridLabelRenderer().setNumHorizontalLabels(13);
                graph.getGridLabelRenderer().setTextSize(9);
                graph.getGridLabelRenderer().setHorizontalAxisTitle("");
                series.setDrawDataPoints(true);
                series.setDataPointsRadius(10f);

                graph.addSeries(series);
                priceDialog.show();
            }
        });

        btnAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ServicesTask(ServiceType.ALERT, ProductActivity.this, product);
            }
        });

        btnAskDevis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserSessionManager sessionManager = new UserSessionManager(ProductActivity.this);
                User user = sessionManager.getUserSession();
                if (user != null) {
                    ProductAnnonceVO annonceVO = new ProductAnnonceVO();
                    UserVO userVO = new UserVO();
                    userVO.setId(user.getId());
                    userVO.setProviderUserId(user.getFbId());
                    userVO.setEmail(user.getEmail());
                    annonceVO.setUser(userVO);
                    annonceVO.setParentId(product.getId());
                    annonceVO.setContactMail(user.getEmail());
                    annonceVO.setTitle(product.getTitle());
                    annonceVO.setPrice(product.getPrice());
                    annonceVO.setCategory(product.getCategory());
                    new ServicesTask(ServiceType.ASK_OFFER, ProductActivity.this, annonceVO);
                } else {
                    Intent intent = new Intent(ProductActivity.this,
                            AuthenticationActivity.class);
                    startActivity(intent);
                }
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDialog.Builder shareDialogBuilder = new MaterialDialog.Builder(ProductActivity.this);
                shareDialogBuilder.customView(R.layout.share_layout, true);

                final MaterialDialog shareDialog = shareDialogBuilder.build();
                Button fshareBtn = (Button) shareDialog.findViewById(R.id.fshare);
                Button tshareBtn = (Button) shareDialog.findViewById(R.id.tshare);
                Button gshareBtn = (Button) shareDialog.findViewById(R.id.gshare);

                fshareBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Shareable shareInstance = new Shareable.Builder(ProductActivity.this)
                                .message(product.getTitle())
                                .image(Uri.parse(product.getImage()))
                                .socialChannel(Shareable.Builder.FACEBOOK)
                                .url("http://prixpascher.ma/article/" + productId)
                                .build();
                        shareInstance.share();
                        shareDialog.dismiss();
                    }
                });

                tshareBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Shareable shareInstance = new Shareable.Builder(ProductActivity.this)
                                .message(product.getTitle())
                                .image(Uri.parse(product.getImage()))
                                .socialChannel(Shareable.Builder.TWITTER)
                                .url("http://prixpascher.ma/article/" + productId)
                                .build();
                        shareInstance.share();
                        shareDialog.dismiss();
                    }
                });

                gshareBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Shareable shareInstance = new Shareable.Builder(ProductActivity.this)
                                .message(product.getTitle())
                                .image(Uri.parse(product.getImage()))
                                .socialChannel(Shareable.Builder.GOOGLE_PLUS)
                                .url("http://prixpascher.ma/article/" + productId)
                                .build();
                        shareInstance.share();
                        shareDialog.dismiss();
                    }
                });
                shareDialog.show();
            }
        });

        /*btnUpdateDesc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(ProductActivity.this,
                        UpdateProductActivity.class);
                intent.putExtra(constants.COMMON_KEY, productId);
                startActivity(intent);
            }
        });*/

        if (getIntent().getExtras() != null
                && getIntent().getExtras().containsKey(constants.COMMON_KEY)) {
            productId = getIntent().getExtras().getString(constants.COMMON_KEY);
            new ProductFetchTask(getResources().getString(
                    R.string.product_root_json_url)
                    + productId, handler).execute();
        }

        ratingBar.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (!Utils.isConnectingToInternet(ProductActivity.this)) {
                        showMsg(getResources().getString(R.string.open_network));
                        return false;
                    }
                    UserSessionManager userSession = new UserSessionManager(
                            ProductActivity.this);
                    if (userSession.getUserSession() == null) {
                        Intent intent = new Intent(ProductActivity.this,
                                AuthenticationActivity.class);
                        startActivity(intent);
                    } else {
                        final ProgressDialog dialogPrg = new ProgressDialog(
                                ProductActivity.this);
                        dialogPrg.setCanceledOnTouchOutside(false);
                        logedUser = userSession.getUserSession();

                        MaterialDialog.Builder rankDialogBuilder = new MaterialDialog.Builder(ProductActivity.this);
                        rankDialogBuilder.customView(R.layout.rating_layout, true);

                        final MaterialDialog rankDialog = rankDialogBuilder.build();
                        final RatingBar ratingBar = (RatingBar) rankDialog.findViewById(R.id.dialog_ratingbar);
                        ButtonFlat updateButton = (ButtonFlat) rankDialog.findViewById(R.id.rank_dialog_button);
                        comment = (EditText) rankDialog.findViewById(R.id.comment);

                        updateButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                UserVO userVO = new UserVO();
                                userVO.setId(logedUser.getId());
                                userVO.setProviderUserId(logedUser.getFbId());
                                userVO.setEmail(logedUser.getEmail());

                                ReviewVO reviewVO = new ReviewVO();
                                reviewVO.setmNumStars((int) ratingBar.getRating());
                                reviewVO.setComment(comment.getText().toString());
                                reviewVO.setProductId(productId);
                                reviewVO.setUser(userVO);
                                new ServicesTask(ServiceType.RATING, ProductActivity.this, reviewVO)
                                        .execute();
                                rankDialog.dismiss();
                            }
                        });

                        rankDialog.show();
                    }

                }
                return true;
            }
        });

        final LinearLayoutManager llm = new LinearLayoutManager(ProductActivity.this);
        similarProducts.setLayoutManager(llm);
        productsAdapter = new SimilarProductsAdapter(this, productsList);
        productsAdapter.SetOnItemClickListener(new SimilarProductsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(ProductActivity.this, ProductActivity.class);
                intent.putExtra(constants.COMMON_KEY, productsList.get(position)
                        .getId());
                finish();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        similarProducts.setAdapter(productsAdapter);

        dialog = new ProgressDialog(this);
        dialog.dismiss();
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

    public static final String TAG = "ProductActivity";

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            Bundle bundle = msg.getData();
            if (bundle.containsKey(productId)) {
                dialog.dismiss();
                product = (ProductVO) bundle
                        .getSerializable(productId);
                parseProduct(product);
                toolbar.setTitle(NumberFormat.getInstance(Locale.FRANCE).format(product.getPrice())
                        + " - " + product.getTitle());
                new LoadSimilarDataTask().execute();
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

                    ratingBar.setRating(ratingText);
                } catch (Exception e) {
                    Log.e(TAG, "error parse rate");
                    Log.e(TAG, e.getMessage());
                    // TODO: handle exception
                }
            }
        };
    };

    private class LoadSimilarDataTask extends AsyncTask<Void, Void, List<ProductVO>> {

        @Override
        protected List<ProductVO> doInBackground(Void... params) {
            if (isCancelled()) {
                return null;
            }
            return feedSimilar();
        }

        @Override
        protected void onPostExecute(List<ProductVO> result) {
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

    private List<ProductVO> feedSimilar() {
        try {
            org.springframework.http.HttpEntity<ProductVO> requestEntity =
                    new org.springframework.http.HttpEntity<>(product);
            byte[] encodedId = Base64.encode(productId.getBytes(), Base64.NO_WRAP);
            ResponseEntity<ProductVO[]> products = Utils.MyRestemplate.getInstance(this).exchange(
                    getResources().getString(R.string.product_root_json_url) + new String(encodedId) + "/similar",
                    HttpMethod.POST,
                    requestEntity, ProductVO[].class);

            productsList.addAll(Arrays.asList(products.getBody()));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        return productsList;
    }

    private void parseAndAppend() {
        try {
            productsAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void parseProduct(ProductVO jsonObj) {
        try {
            paths.add(jsonObj.getImage());
            imageAdapter = new FullScreenImageAdapter(ProductActivity.this, paths);
            viewPager.setAdapter(imageAdapter);
            viewPager.setCurrentItem(0);

            shop.setImageResource(getDrawable(this, product.getShopName() + "_large"));

            // String fullNameText = jsonObj.getString(User.TAG_FULL_NAME);
            fullName.setText("");
            fullName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(ProductActivity.this,
                            ProfileActivity.class);
                    intent.putExtra(constants.COMMON_KEY, user_id);
                    startActivity(intent);
                }
            });

            user_id = "";

            if (Category.offerCategoryValues().contains(jsonObj.getCategory())) {
                btnAskDevis.setVisibility(View.VISIBLE);
            } else {
                btnAskDevis.setVisibility(View.GONE);
            }

            btnAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProductActivity.this,
                            GoOnlineShopActivity.class);
                    intent.putExtra(constants.COMMON_KEY, product.getLink());
                    startActivity(intent);
                }
            });

            String titleText = jsonObj.getTitle();
            title.setText(titleText);
            String addressText = jsonObj.getViews() + " nombres de vues";
            address.setText(addressText);
            String phoneText = "Mis à jour le " + new SimpleDateFormat("dd/MM/yyyy").format(jsonObj.getTrackingDate());
            trackingDate.setText(phoneText);

            if (jsonObj.getPromoted() && !jsonObj.getPrices().isEmpty()) {
                Double delta = jsonObj.getPrice() -
                        jsonObj.getPrices().get(0).getPrice();
                if (delta < 1) {
                    pricetag.setText(NumberFormat.getInstance(Locale.FRANCE).format(delta) + " Dhs");
                    Animation anim = new AlphaAnimation(0.0f, 1.0f);
                    anim.setDuration(400);
                    anim.setStartOffset(10);
                    anim.setRepeatMode(Animation.REVERSE);
                    anim.setRepeatCount(Animation.INFINITE);
                    pricetag.startAnimation(anim);
                    pricetag.setVisibility(View.VISIBLE);
                }
            } else {
                pricetag.setVisibility(View.INVISIBLE);
            }
            
            /*            String conditionText = jsonObj.getString("condition");
            String[] condition_id = getResources().getStringArray(
            R.array.condition_id);
            String[] condition_name = getResources().getStringArray(
            R.array.condition_name);
            for (int i = 0; i < condition_id.length; i++) {
                if (conditionText.equalsIgnoreCase(condition_id[i])) {
                    condition.setText(condition_name[i]);
                    break;
                }
            }*/

            float ratingVal = 0;
            for (ReviewVO reviews: product.getReviews()) {
                ratingVal += reviews.getmNumStars();
            }
            ratingVal = ratingVal / product.getReviews().size();
            ratingBar.setRating(ratingVal);

            String priceText = NumberFormat.getInstance(Locale.FRANCE).format(jsonObj.getPrice());
            if (priceText != null && !priceText.equalsIgnoreCase("")) {
                price.setText(priceText + " Dhs");
            } else {
                price.setText("0 Dhs");
            }

            String avtPath = null; // jsonObj.getString(User.TAG_AVT);
            if (avtPath != null && !avtPath.equalsIgnoreCase("")) {
                Log.i(TAG, " no tag");
                String avtString = "";
                avtString = avtPath;
                Utils.MyPicasso.with(ProductActivity.this)
                        .load(avtString)
                        .resize(200, 200).centerCrop()
                        .placeholder(R.drawable.ic_avatar)
                        .into(avt);
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
                    + "api/products_api/comment?id=" + productId);*/
        } catch (Exception e) {
            // TODO: handle exception
            Log.e(TAG, e.getMessage());
        }
    }

    private class DeleteProducts extends Thread {
        public DeleteProducts() {
            // TODO Auto-generated constructor stub

        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();
            runOnUiThread(new Runnable() {
                public void run() {
                    dialog.show();
                }
            });

            String handleInsertUser = getResources().getString(
                    R.string.products_json_url)
                    + "delete";
            try {
                /*if (resEntity != null) {
                    Log.i(TAG, response_str);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                dialog.dismiss();
                                showSuccessDialog();
                            } catch (Exception e) {
                                // showDeleteDialog();
                            }
                        }
                    });
                }*/
            } catch (Exception e) {
                // TODO: handle exception
                Log.e(TAG, e.getMessage());
            }
        }
    };

    public void showSuccessDialog() {
        // TODO Auto-generated method stub
        AlertDialog.Builder buidler = new AlertDialog.Builder(this);
        buidler.setMessage(getResources()
                .getString(R.string.delete_successfull));
        buidler.setPositiveButton(getResources().getString(R.string.ok_label),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method
                        // stub
                        Intent intent = new Intent(ProductActivity.this,
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
        // TODO Auto-generated method stub
        AlertDialog.Builder buidler = new AlertDialog.Builder(this);
        buidler.setMessage(msg);
        buidler.setPositiveButton(getResources().getString(R.string.ok_label),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method
                        // stub
                        new DeleteProducts().start();
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