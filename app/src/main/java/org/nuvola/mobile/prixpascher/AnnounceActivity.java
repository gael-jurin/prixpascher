package org.nuvola.mobile.prixpascher;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFlat;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONObject;
import org.nuvola.mobile.prixpascher.adapters.FullScreenImageAdapter;
import org.nuvola.mobile.prixpascher.business.UserSessionManager;
import org.nuvola.mobile.prixpascher.business.Utils;
import org.nuvola.mobile.prixpascher.confs.constants;
import org.nuvola.mobile.prixpascher.dto.ContactMailVO;
import org.nuvola.mobile.prixpascher.dto.OfferVO;
import org.nuvola.mobile.prixpascher.dto.ProductAnnonceVO;
import org.nuvola.mobile.prixpascher.models.AnnounceType;
import org.nuvola.mobile.prixpascher.models.User;
import org.nuvola.mobile.prixpascher.tasks.AnnounceFetchTask;
import org.nuvola.mobile.prixpascher.tasks.ServiceType;
import org.nuvola.mobile.prixpascher.tasks.ServicesTask;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint({ "NewApi", "HandlerLeak" })
public class AnnounceActivity extends ActionBarParentActivity {

    // @BindView(R.id.btn_edit_desc) ImageButton btnUpdateDesc;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.full_name) TextView fullName;
    @BindView(R.id.merchantAvt) ImageView merchantAvt;
    @BindView(R.id.address) TextView address;
    @BindView(R.id.price) TextView price;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.qty) TextView qty;
    @BindView(R.id.trackingDate) TextView trackingDate;
    @BindView(R.id.pager) ViewPager viewPager;
    @BindView(R.id.btnProviderCall) FloatingActionButton btnProviderCall;
    @BindView(R.id.btnProviderEmail) FloatingActionButton btnProviderEmail;
    @BindView(R.id.btnMenu) FloatingActionMenu btnMenu;
    // @BindView(R.id.ratingBarClick) RatingBar ratingBar;

    @BindView(R.id.priceOfferUserInput) EditText priceOfferUserInput;
    @BindView(R.id.qtyOfferUserInput) EditText qtyOfferUserInput;
    @BindView(R.id.msgOfferUserInput) EditText msgOfferUserInput;
    @BindView(R.id.codePromoOfferUserInput) EditText codePromoOfferUserInput;
    @BindView(R.id.sendOffer) ButtonFlat btnSendOffer;
    // @BindView(R.id.btnUpload) ButtonFlat btnUpload;
    @BindView(R.id.layout_offer)
    LinearLayout layout_offer;

    private FullScreenImageAdapter imageAdapter;
    private ProgressDialog dialog;
    private List<String> paths;
    private ProductAnnonceVO announce;
    private String announceId;
    private String user_id;
    private String email, phoneText;
    private User logedUser;
    private ImageView avt;
    private EditText comment;
    private ContactMailVO contactMailVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.announce_main_layout);
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
                    intent.putExtra(constants.COMMON_KEY, announceId);
                    startActivity(intent);
                }
            });*/
        } else {
            // LinearLayout updateButtonWrapper = (LinearLayout) findViewById(R.id.update_button_wrapper);
            // updateButtonWrapper.setVisibility(LinearLayout.INVISIBLE);
            // updateButtonWrapper.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        }

        // Send email alert request();
        btnProviderEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ServicesTask(ServiceType.REPLY_OFFER, AnnounceActivity.this, announce).execute();
            }
        });

        /*btnUpdateDesc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(ProductActivity.this,
                        UpdateProductActivity.class);
                intent.putExtra(constants.COMMON_KEY, announceId);
                startActivity(intent);
            }
        });*/

        if (getIntent().getExtras() != null
                && getIntent().getExtras().containsKey(constants.COMMON_KEY)) {
            announceId = getIntent().getExtras().getString(constants.COMMON_KEY);
            new AnnounceFetchTask(getResources().getString(
                    R.string.announce_root_json_url)
                    + announceId, handler).execute();
        }

        /*ratingBar.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (!Utils.isConnectingToInternet(AnnounceActivity.this)) {
                        showMsg(getResources().getString(R.string.open_network));
                        return false;
                    }
                    UserSessionManager userSession = new UserSessionManager(
                            AnnounceActivity.this);
                    if (userSession.getUserSession() == null) {
                        Intent intent = new Intent(AnnounceActivity.this,
                                AuthenticationActivity.class);
                        startActivity(intent);
                    } else {
                        final ProgressDialog dialogPrg = new ProgressDialog(
                                AnnounceActivity.this);
                        dialogPrg.setCanceledOnTouchOutside(false);
                        logedUser = userSession.getUserSession();

                        MaterialDialog.Builder rankDialogBuilder = new MaterialDialog.Builder(AnnounceActivity.this);
                        rankDialogBuilder.customView(R.layout.rating_layout, true);

                        final MaterialDialog rankDialog = rankDialogBuilder.build();
                        rankDialog.show();

                        final RatingBar ratingBar = (RatingBar) rankDialog.findViewById(R.id.dialog_ratingbar);
                        ButtonFlat updateButton = (ButtonFlat) rankDialog.findViewById(R.id.rank_dialog_button);

                        comment = (EditText) rankDialog.findViewById(R.id.comment);

                        rankDialog.show();
                    }

                }
                return true;
            }
        });*/

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

    public static final String TAG = "AnnounceActivity";

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            Bundle bundle = msg.getData();
            if (bundle.containsKey(announceId)) {
                dialog.dismiss();
                announce = (ProductAnnonceVO) bundle
                        .getSerializable(announceId);
                parseProduct(announce);
                toolbar.setTitle(NumberFormat.getInstance(Locale.FRANCE).format(announce.getPrice())
                        + " - " + announce.getTitle());
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
                    e.printStackTrace();
                }
            }
        };
    };

    public void parseProduct(final ProductAnnonceVO jsonObj) {
        try {
            if (jsonObj.getImage() != null) {
                if (jsonObj.getType().equals(AnnounceType.OFFER_ASK)) {
                    paths.add(jsonObj.getImage());
                } else {
                    paths.add(Utils.buildImageUri(jsonObj.getImage()));
                }
                if (jsonObj.getImage1() != null) {
                    paths.add(Utils.buildImageUri(jsonObj.getImage1()));
                }
                if (jsonObj.getImage2() != null) {
                    paths.add(Utils.buildImageUri(jsonObj.getImage2()));
                }
                if (jsonObj.getImage3() != null) {
                    paths.add(Utils.buildImageUri(jsonObj.getImage3()));
                }

                imageAdapter = new FullScreenImageAdapter(AnnounceActivity.this, paths);
                viewPager.setAdapter(imageAdapter);
                viewPager.setCurrentItem(0);
            }

            String fullNameText = jsonObj.getUser().getUserName();
            fullName.setText(fullNameText);
            merchantAvt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO : Display any merchant profile (This will be useful when including localisation and Stats)
                    /*Intent intent = new Intent(AnnounceActivity.this,
                            ProfileActivity.class);
                    intent.putExtra(constants.COMMON_KEY, jsonObj.getUser().getProviderUserId());
                    startActivity(intent);*/
                    if (jsonObj.getDetail() != null && jsonObj.getDetail().trim().length() > 5) {
                        AlertDialog.Builder buidler = new AlertDialog.Builder(AnnounceActivity.this);
                        buidler.setMessage(jsonObj.getDetail());
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

            if (jsonObj.getType().equals(AnnounceType.COMMON_SELL)) {
                email = jsonObj.getContactMail();
                phoneText = jsonObj.getContactPhone();
            }

            btnProviderCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (phoneText.length() > 0) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneText, null));
                        startActivity(intent);
                    }
                }
            });

            btnSendOffer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doSendOffer();
                }
            });

            String titleText = jsonObj.getTitle();
            title.setText(titleText);
            String addressText = jsonObj.getViews() + " nombres de vues";
            address.setText(addressText);
            String date = "Posté " + Utils.formatToYesterdayOrToday(jsonObj.getTrackingDate());
            trackingDate.setText(date);

            if (jsonObj.getType().equals(AnnounceType.OFFER_ASK)) {
                qty.setText("Qté demandée : " + String.valueOf(jsonObj.getQty()));
                qty.setVisibility(View.VISIBLE);
                layout_offer.setVisibility(View.VISIBLE);
                // viewPager.setVisibility(View.GONE);
            } else {
                qty.setVisibility(View.GONE);
                layout_offer.setVisibility(View.GONE);
                // viewPager.setVisibility(View.VISIBLE);
            }

            Utils.MyPicasso.with(AnnounceActivity.this)
                    .load(jsonObj.getUser().getPhoto())
                    .placeholder(R.drawable.ic_avatar)
                    .into(merchantAvt);
            
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

            String priceText = NumberFormat.getInstance(Locale.FRANCE).format(jsonObj.getPrice());
            if (priceText != null && !priceText.equalsIgnoreCase("")) {
                if (jsonObj.getType().equals(AnnounceType.OFFER_ASK)) {
                    price.setText("Prix vu : " + priceText + " Dhs");
                } else {
                    price.setText(priceText + " Dhs");
                }
            } else {
                price.setText("0 Dhs");
            }

            String avtPath = null; // jsonObj.getString(User.TAG_AVT);
            if (avtPath != null && !avtPath.equalsIgnoreCase("")) {
                Log.i(TAG, " no tag");
                String avtString = "";
                avtString = avtPath;
                Utils.MyPicasso.with(AnnounceActivity.this)
                        .load(avtString)
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
                    + "api/products_api/comment?id=" + announceId);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validateBeforeSend() {
        UserSessionManager sessionManager = new UserSessionManager(this);
        User user = sessionManager.getUserSession();

        String qtyText = qtyOfferUserInput.getText().toString();
        String priceText = priceOfferUserInput.getText().toString();
        String codeText = codePromoOfferUserInput.getText().toString();
        String notesText = msgOfferUserInput.getText().toString();

        if (qtyText != null && !qtyText.equalsIgnoreCase("")
            && priceText != null && !priceText.equalsIgnoreCase("")) {
            try {
                OfferVO newOffer = new OfferVO();
                newOffer.setProductAnnonce(announce);
                newOffer.setTargetPrice(priceText);
                newOffer.setStock(qtyText);
                newOffer.setPosted(new Date());
                newOffer.setCodePromo(codeText);

                //TODO : Removed this redundant in server side
                if (user.getEmail() == null || user.getEmail().isEmpty()) {
                    newOffer.setEmail("nuvola.maroc@gmail.com");
                } else {
                    newOffer.setEmail(user.getEmail());
                }

                contactMailVO = new ContactMailVO();
                contactMailVO.setEmail(newOffer.getEmail());
                contactMailVO.setSubject("PrixPasCher - Offre de vente sur Devis");
                contactMailVO.setMessage(notesText);
                contactMailVO.setOffer(newOffer);
            } catch (Exception ex) {
                showDialog(getResources().getString(R.string.incorrect_offer));
                return false;
            }
            return true;
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(
                    getResources().getString(R.string.incorrect_offer))
                    .setTitle(getResources().getString(R.string.info_msg))
                    .setPositiveButton(
                            getResources().getString(R.string.ok_label),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub
                                }
                            });
            AlertDialog dialog = builder.create();
            dialog.show();
            return false;
        }
    }

    @SuppressLint("NewApi")
    private void doSendOffer() {
        boolean valid = validateBeforeSend();
        if (valid) {
            UserSessionManager sessionManager = new UserSessionManager(this);
            User user = sessionManager.getUserSession();
            if (user != null) {
                new ServicesTask(ServiceType.REPLY_OFFER, AnnounceActivity.this,
                        contactMailVO).execute();
            } else {
                Intent intent = new Intent(this, AuthenticationActivity.class);
                startActivity(intent);
            }
        }
    }

    private class Unpublish extends Thread {
        public Unpublish() {
        }

        @Override
        public void run() {
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
                e.printStackTrace();
            }
        }
    };

    public void showSuccessDialog() {
        AlertDialog.Builder buidler = new AlertDialog.Builder(this);
        buidler.setMessage(getResources()
                .getString(R.string.delete_successfull));
        buidler.setPositiveButton(getResources().getString(R.string.ok_label),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // stub
                        Intent intent = new Intent(AnnounceActivity.this,
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
                        new Unpublish().start();
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