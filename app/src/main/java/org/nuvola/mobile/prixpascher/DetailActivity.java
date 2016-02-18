package org.nuvola.mobile.prixpascher;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gc.materialdesign.views.ButtonRectangle;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.nuvola.mobile.prixpascher.adapters.FullScreenImageAdapter;
import org.nuvola.mobile.prixpascher.business.ProductFetchTask;
import org.nuvola.mobile.prixpascher.business.RoundedAvatarDrawable;
import org.nuvola.mobile.prixpascher.business.UserSessionManager;
import org.nuvola.mobile.prixpascher.business.Utils;
import org.nuvola.mobile.prixpascher.confs.constants;
import org.nuvola.mobile.prixpascher.dto.ProductVO;
import org.nuvola.mobile.prixpascher.models.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

@SuppressLint({ "NewApi", "HandlerLeak" })
public class DetailActivity extends ActionBarParentActivity {
    TextView fullName, address, dateUpdated, phone, price, title;
    ImageView avt;
    ProgressDialog dialog;
    public static final String IMAGES_RESPONSE = "images_feed";
    ArrayList<String> paths;
    ProductVO product;
    String productId;
    String user_id;
    ImageButton btnAddToCart, btnDelete, btnUpdateDesc;
    String email, phoneText;
    EditText message, comment;
    ImageView shop;
    User logedUser;
    Toolbar toolbar;

    private FullScreenImageAdapter adapter;
    private ViewPager viewPager;

    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);
        Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey(constants.USER_ID_KEY)) {
            // int user_id=bundle.getInt(constants.USER_ID_KEY);
            ImageButton btnUpdateGallery = (ImageButton) findViewById(R.id.btn_update_gallery);
            btnUpdateGallery.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(DetailActivity.this,
                            UpdateGalleryActivity.class);
                    intent.putExtra(constants.COMMON_KEY, productId);
                    startActivity(intent);
                }
            });
        } else {
            LinearLayout updateButtonWrapper = (LinearLayout) findViewById(R.id.update_button_wrapper);
            updateButtonWrapper.setVisibility(LinearLayout.INVISIBLE);
            updateButtonWrapper.setLayoutParams(new LinearLayout.LayoutParams(
                    0, 0));
        }

        // initGallery();
        fullName = (TextView) findViewById(R.id.full_name);
        address = (TextView) findViewById(R.id.address);
        price = (TextView) findViewById(R.id.price);
        title = (TextView) findViewById(R.id.title);
        shop = (ImageView) findViewById(R.id.shop);
        // avt = (ImageView) findViewById(R.id.avt);
        // condition = (TextView) findViewById(R.id.condition);
        phone = (TextView) findViewById(R.id.phone);
        btnAddToCart = (ImageButton) findViewById(R.id.btnAddToCart);
        // btnSMS = (ImageButton) findViewById(R.id.btnSmS);
        // btnCall = (ImageButton) findViewById(R.id.btnPhone);
        btnDelete = (ImageButton) findViewById(R.id.btn_delete);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDialogConfirmDelete(getResources().getString(
                        R.string.confirm_del));
            }
        });

        btnUpdateDesc = (ImageButton) findViewById(R.id.btn_edit_desc);
        btnUpdateDesc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(DetailActivity.this,
                        UpdateProductActivity.class);
                intent.putExtra(constants.COMMON_KEY, productId);
                startActivity(intent);
            }
        });

        if (getIntent().getExtras() != null
                && getIntent().getExtras().containsKey(constants.COMMON_KEY)) {
            productId = getIntent().getExtras().getString(constants.COMMON_KEY);
            new ProductFetchTask(getResources().getString(
                    R.string.products_root_json_url)
                    + productId, handler).execute();
        }

        ratingBar = (RatingBar) findViewById(R.id.ratingBarClick);
        ratingBar.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (!Utils.isConnectingToInternet(DetailActivity.this)) {
                        showMsg(getResources().getString(R.string.open_network));
                        return false;
                    }
                    UserSessionManager userSession = new UserSessionManager(
                            DetailActivity.this);
                    if (userSession.getUserSession() == null) {
                        Intent intent = new Intent(DetailActivity.this,
                                AuthenticationActivity.class);
                        startActivity(intent);
                    } else {
                        final ProgressDialog dialogPrg = new ProgressDialog(
                                DetailActivity.this);
                        dialogPrg.setCanceledOnTouchOutside(false);
                        logedUser = userSession.getUserSession();

                        MaterialDialog.Builder rankDialogBuilder=new MaterialDialog.Builder(DetailActivity.this);
                        rankDialogBuilder.customView(R.layout.rating_layout,true);

                        final MaterialDialog rankDialog= rankDialogBuilder.build();
                        rankDialog.show();


                        final RatingBar ratingBar = (RatingBar) rankDialog
                                .findViewById(R.id.dialog_ratingbar);
                        ButtonRectangle updateButton = (ButtonRectangle) rankDialog
                                .findViewById(R.id.rank_dialog_button);

                        comment = (EditText) rankDialog
                                .findViewById(R.id.comment);

                        rankDialog.show();
                    }

                }
                return true;
            }
        });


        dialog = new ProgressDialog(this);
        dialog.setMessage(getResources().getString(R.string.loading));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        paths = new ArrayList<>();

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.detail_label));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadAd();
    }

    public static final String TAG = "DetailActivity";
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            Bundle bundle = msg.getData();
            if (bundle.containsKey(productId)) {
                dialog.dismiss();
                product = (ProductVO) bundle
                        .getSerializable(productId);
               /*new JSONFetchTask(getResources().getString(
                 R.string.products_json_url)
                 + "avg_rate/product_id/" + productId, handlerRate,
                 "rate").execute();*/
                parseProduct(product);
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
                    e.printStackTrace();
                    // TODO: handle exception
                }
            }
        };
    };

    public void parseProduct(ProductVO jsonObj) {
        try {
            paths.add(jsonObj.getImage());
            adapter = new FullScreenImageAdapter(DetailActivity.this, paths);
            viewPager = (ViewPager) findViewById(R.id.pager);
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(0);

            shop.setImageResource(getDrawable(this, product.getShopName() + "_large"));

            // String fullNameText = jsonObj.getString(User.TAG_FULL_NAME);
            fullName.setText("");
            fullName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(DetailActivity.this,
                            ProfileActivity.class);
                    intent.putExtra(constants.COMMON_KEY, user_id);
                    startActivity(intent);
                }
            });

            user_id = "";
            email = "";
            phoneText = "";

            btnAddToCart.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                }

            });

            String titleText = jsonObj.getTitle();
            title.setText(titleText);
            String addressText = jsonObj.getViews() + " nombres de vues";
            address.setText(addressText);
            String phoneText = "Mis à jour le " + new SimpleDateFormat("dd/MM/yyyy").format(jsonObj.getTrackingDate());
            phone.setText(phoneText);
            
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

            String priceText = jsonObj.getPrice().toString();
            if (priceText != null && !priceText.equalsIgnoreCase("")) {
                price.setText(priceText + " Dhs");
            } else {
                // price.setText(" " + getResources().getString(R.string.negotiate_label));
                price.setText("0 Dhs");
            }

            String avtPath = null; // jsonObj.getString(User.TAG_AVT);
            if (avtPath != null && !avtPath.equalsIgnoreCase("")) {
                Log.i(TAG, " no tag");
                String avtString = "";
                if (Utils.checkFacebookAvt(avtPath)) {
                    avtString = avtPath;
                } else {
                    avtString = getResources().getString(R.string.domain_url)
                            + avtPath;
                }
                Ion.with(DetailActivity.this, avtString).withBitmap()
                        .resize(200, 200).centerCrop()
                        .placeholder(R.drawable.ic_avatar)
                        .error(R.drawable.ic_avatar).asBitmap()
                        .setCallback(new FutureCallback<Bitmap>() {

                            @Override
                            public void onCompleted(Exception arg0,
                                                    Bitmap bitmap) {
                                // TODO Auto-generated
                                // method stub
                                if (bitmap != null) {
                                    RoundedAvatarDrawable avtDrawable = new RoundedAvatarDrawable(
                                            bitmap);
                                    avt.setImageDrawable(avtDrawable);
                                }
                            }

                        });

            }

            /*String content = jsonObj.getProductCategory().toUpperCase(); // jsonObj.getString(Products.TAG_PRODUCT_CATEGORY);
            DetailContentFragment fragment = DetailContentFragment
                    .newInstance();
            Bundle bundle = new Bundle();
            bundle.putString(DetailContentFragment.DETAIL_KEY, content);
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content, fragment).commit();*/

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
            e.printStackTrace();
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
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(handleInsertUser);
                MultipartEntity reqEntity = new MultipartEntity();
                reqEntity.addPart("id", new StringBody(productId + ""));
                post.setEntity(reqEntity);
                HttpResponse res = client.execute(post);
                HttpEntity resEntity = res.getEntity();
                final String response_str = EntityUtils.toString(resEntity);
                if (resEntity != null) {
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
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
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
                        Intent intent = new Intent(DetailActivity.this,
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

    private class SendEnquiry extends AsyncTask<Void, Void, Boolean> {

        public SendEnquiry() {
            // TODO Auto-generated constructor stub
        }

        @Override
        protected void onCancelled() {
            // TODO Auto-generated method stub
            super.onCancelled();
            dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = new ProgressDialog(DetailActivity.this);
            dialog.setMessage(DetailActivity.this.getResources().getString(
                    R.string.please_wait_msg));
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO Auto-generated method stub
            String handleInserUrl = getResources().getString(
                    R.string.users_json_url)
                    + "send_enquiry";
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(handleInserUrl);
                MultipartEntity reqEntity = new MultipartEntity();
                reqEntity.addPart("email", new StringBody(email));
                reqEntity.addPart("message", new StringBody(message.getText()
                        .toString()));
                reqEntity.addPart("reply_to",
                        new StringBody(logedUser.getEmail()));
                reqEntity.addPart("user_name",
                        new StringBody(logedUser.getUserName()));
                post.setEntity(reqEntity);
                HttpResponse response = client.execute(post);
                HttpEntity resEntity = response.getEntity();
                final String response_str = EntityUtils.toString(resEntity);
                if (resEntity != null) {
                    Log.i("RESPONSE", response_str);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                dialog.dismiss();
                                JSONObject jsonObj = new JSONObject(
                                        response_str);
                                if (jsonObj.getString("ok").equals("0")) {
                                    showDialog(getResources().getString(
                                            R.string.spam_msg));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            } catch (Exception ex) {
                Log.e("Debug", "error: " + ex.getMessage(), ex);
            }
            return null;
        }
    };
}