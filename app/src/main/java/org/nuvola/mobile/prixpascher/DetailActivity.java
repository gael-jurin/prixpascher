package org.nuvola.mobile.prixpascher;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.afollestad.materialdialogs.MaterialDialog;
import org.nuvola.mobile.prixpascher.adapters.FullScreenImageAdapter;
import org.nuvola.mobile.prixpascher.business.JSONFetchTask;
import org.nuvola.mobile.prixpascher.business.RoundedAvatarDrawable;
import org.nuvola.mobile.prixpascher.business.Utils;
import org.nuvola.mobile.prixpascher.business.UserSessionManager;
import org.nuvola.mobile.prixpascher.confs.constants;
import org.nuvola.mobile.prixpascher.fragments.DetailContentFragment;
import org.nuvola.mobile.prixpascher.models.Products;
import org.nuvola.mobile.prixpascher.models.User;
import com.gc.materialdesign.views.ButtonRectangle;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

@SuppressLint({ "NewApi", "HandlerLeak" })
public class DetailActivity extends ActionBarParentActivity {
    TextView fullName, address, dateUpdated, phone, price, title;
    ImageView avt;
    ProgressDialog dialog;
    public static final String IMAGES_RESPONSE = "images_feed";
    ArrayList<String> paths;
    int product_id;
    String user_id;
    ImageButton btnEmail, btnSMS, btnCall, btnDelete, btnUpdateDesc;
    String email, phoneText;
    EditText message, comment;
    User logedUser;
    Toolbar toolbar;

    // private void initGallery() {
    // Resources r = getResources();
    // float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
    // constants.GRID_PADDING, r.getDisplayMetrics());
    // columnWidth = (int) ((Ultils.getScreenWidth(DetailActivity.this) -
    // ((constants.NUM_OF_COLUMNS + 1) * padding)) / constants.NUM_OF_COLUMNS);
    // gallery.setNumColumns(constants.NUM_OF_COLUMNS);
    // gallery.setColumnWidth(columnWidth);
    // gallery.setStretchMode(GridView.NO_STRETCH);
    // gallery.setHorizontalSpacing((int) padding);
    // gallery.setVerticalSpacing((int) padding);
    // }

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
                    intent.putExtra(constants.COMMON_KEY, product_id);
                    startActivity(intent);
                }
            });
        } else {
            LinearLayout updateButtonWrapper = (LinearLayout) findViewById(R.id.update_button_wrapper);
            updateButtonWrapper.setVisibility(LinearLayout.INVISIBLE);
            updateButtonWrapper.setLayoutParams(new LinearLayout.LayoutParams(
                    0, 0));
        };

        // gallery = (ImageView) findViewById(R.id.gallery);
        // initGallery();
        fullName = (TextView) findViewById(R.id.full_name);
        address = (TextView) findViewById(R.id.address);
        price = (TextView) findViewById(R.id.price);
        title = (TextView) findViewById(R.id.title);
        avt = (ImageView) findViewById(R.id.avt);
        // condition = (TextView) findViewById(R.id.condition);
        phone = (TextView) findViewById(R.id.phone);
        btnEmail = (ImageButton) findViewById(R.id.btnEmail);
        btnSMS = (ImageButton) findViewById(R.id.btnSmS);
        btnCall = (ImageButton) findViewById(R.id.btnPhone);
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
                intent.putExtra(constants.COMMON_KEY, product_id);
                startActivity(intent);
            }
        });

        if (getIntent().getExtras() != null
                && getIntent().getExtras().containsKey(constants.COMMON_KEY)) {
            product_id = getIntent().getExtras().getInt(constants.COMMON_KEY);
            new JSONFetchTask(getResources().getString(
                    R.string.products_json_url)
                    + "products?product_id=" + product_id, handler).execute();
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


                        updateButton
                                .setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // if have rating and user<>user
                                        if (ratingBar.getRating() > 0) {

                                            if((logedUser.getId() != Integer
                                                    .parseInt(user_id))){
                                            dialogPrg.show();
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    String handleInserUrl = getResources()
                                                            .getString(
                                                                    R.string.products_json_url)
                                                            + "rate";
                                                    try {
                                                        HttpClient client = new DefaultHttpClient();
                                                        HttpPost post = new HttpPost(
                                                                handleInserUrl);
                                                        MultipartEntity reqEntity = new MultipartEntity();
                                                        reqEntity
                                                                .addPart(
                                                                        "user_id",
                                                                        new StringBody(
                                                                                logedUser
                                                                                        .getId()
                                                                                        + ""));
                                                        reqEntity
                                                                .addPart(
                                                                        "product_id",
                                                                        new StringBody(
                                                                                product_id
                                                                                        + ""));
                                                        reqEntity
                                                                .addPart(
                                                                        "point",
                                                                        new StringBody(
                                                                                ratingBar
                                                                                        .getRating()
                                                                                        + ""));
                                                        reqEntity
                                                                .addPart(
                                                                        "comment",
                                                                        new StringBody(
                                                                                comment.getText()
                                                                                        .toString()));

                                                        reqEntity
                                                                .addPart(
                                                                        "product_user_id",
                                                                        new StringBody(
                                                                                user_id));
                                                        post.setEntity(reqEntity);
                                                        HttpResponse response = client
                                                                .execute(post);
                                                        HttpEntity resEntity = response
                                                                .getEntity();
                                                        final String response_str = EntityUtils
                                                                .toString(resEntity);
                                                        if (resEntity != null) {
                                                            Log.i("RESPONSE_DETAIL",
                                                                    response_str);
                                                            runOnUiThread(new Runnable() {
                                                                public void run() {
                                                                    try {
                                                                        dialogPrg
                                                                                .dismiss();
                                                                        rankDialog
                                                                                .dismiss();

                                                                        new JSONFetchTask(
                                                                                getResources()
                                                                                        .getString(
                                                                                                R.string.products_json_url)
                                                                                        + "avg_rate/product_id/"
                                                                                        + product_id,
                                                                                handlerRate,
                                                                                "rate")
                                                                                .execute();
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    } catch (Exception ex) {
                                                        Log.e("Debug",
                                                                "error: "
                                                                        + ex.getMessage(),
                                                                ex);
                                                    }
                                                }
                                            }).start();
                                            rankDialog.dismiss();
                                        }else{
                                               ///login activity
                                                Intent intent=new Intent(DetailActivity.this,AuthenticationActivity.class);
                                                startActivity(intent);
                                            }
                                    }else{
                                            //pls rate
                                        }

                                    }
                                });
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
        paths = new ArrayList<String>();

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
            if (bundle.containsKey(JSONFetchTask.KEY_RESPONSE)) {
                dialog.dismiss();
                String jsonString = bundle
                        .getString(JSONFetchTask.KEY_RESPONSE);
               new JSONFetchTask(getResources().getString(
                 R.string.products_json_url)
                 + "avg_rate/product_id/" + product_id, handlerRate,
                 "rate").execute();
                try {
                    JSONArray jsonArray = new JSONArray(jsonString);
                    if (jsonArray.length() == 1) {
                        JSONObject obj = jsonArray.getJSONObject(0);
                        parseProduct(obj);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "error parse");
                    // TODO: handle exception
                }
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

    private FullScreenImageAdapter adapter;
    private ViewPager viewPager;
    Handler handlerImages = new Handler() {
        public void handleMessage(android.os.Message msg) {
            Bundle bundle = msg.getData();
            if (bundle.containsKey(IMAGES_RESPONSE)) {
                String jsonString = bundle.getString(IMAGES_RESPONSE);
                try {
                    JSONArray jsonArray = new JSONArray(jsonString);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        String path = getResources().getString(
                                R.string.domain_url)
                                + jsonObj.getString("path");
                        paths.add(path);
                    }
                    if (paths.size() != 0) {
                        adapter = new FullScreenImageAdapter(DetailActivity.this, paths);
                        viewPager = (ViewPager) findViewById(R.id.pager);
                        viewPager.setAdapter(adapter);
                        viewPager.setCurrentItem(0);
                    }
                } catch (Exception e) {
                    LinearLayout pagerWrapper=(LinearLayout)findViewById(R.id.pager_wrapper);
                    pagerWrapper.setVisibility(View.GONE);
                }
            }
        };
    };

    public void parseProduct(JSONObject jsonObj) {
        try {
            // int id = jsonObj.getInt(Products.TAG_ID);
            new JSONFetchTask(getResources()
                    .getString(R.string.images_json_url)
                    + "images/product_id/"
                    + product_id, handlerImages, IMAGES_RESPONSE).execute();

           //// ImageView btnShowMap = (ImageView) findViewById(R.id.map);
            /* final String lat = jsonObj.getString("lat");
            final String lng = jsonObj.getString("lng");*/
           // btnShowMap.setOnClickListener(new View.OnClickListener() {

                //@Override
              /*  public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    *//*    Intent intent = new Intent(DetailActivity.this,
                    LocationActivity.class);
                    intent.putExtra("lat", lat);
                    intent.putExtra("lng", lng);
                    Log.i("Location", "lat: " + lat + ",lng: " + lng);
                    startActivity(intent);*//*
                }
            });*/

            String fullNameText = jsonObj.getString(User.TAG_FULL_NAME);
            fullName.setText(fullNameText);
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

            user_id = jsonObj.getString("user_id");
            email = jsonObj.getString("email");
            phoneText = jsonObj.getString("phone");
            Log.i(TAG, email);
            btnSMS.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (!Utils.isConnectingToInternet(DetailActivity.this)) {
                        showMsg(getResources().getString(R.string.open_network));
                        return;
                    }

                    UserSessionManager userSession = new UserSessionManager(
                            DetailActivity.this);

                    if (userSession.getUserSession() == null) {
                        Intent intent = new Intent(DetailActivity.this,
                                AuthenticationActivity.class);
                        startActivity(intent);
                    } else {
                        if (phoneText == null || phoneText.equals("")) {
                            showDialog(getResources().getString(
                                    R.string.this_user_not_share_phone_number));
                        } else {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri
                                    .parse("sms:" + phoneText)));
                        }
                    }
                }

            });

            btnCall.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (!Utils.isConnectingToInternet(DetailActivity.this)) {
                        showMsg(getResources().getString(R.string.open_network));
                        return;
                    }

                    UserSessionManager userSession = new UserSessionManager(
                            DetailActivity.this);

                    if (userSession.getUserSession() == null) {
                        Intent intent = new Intent(DetailActivity.this,
                                AuthenticationActivity.class);
                        startActivity(intent);
                    } else {
                        if (phoneText == null || phoneText.equals("")) {
                            showDialog(getResources().getString(
                                    R.string.this_user_not_share_phone_number));
                        } else {
                            startActivity(new Intent(Intent.ACTION_CALL, Uri
                                    .parse("tel:" + phoneText)));
                        }
                    }
                }

            });

            btnEmail.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (!Utils.isConnectingToInternet(DetailActivity.this)) {
                        showMsg(getResources().getString(R.string.open_network));
                        return;
                    }

                    UserSessionManager userSession = new UserSessionManager(
                            DetailActivity.this);

                    if (userSession.getUserSession() == null) {
                        Intent intent = new Intent(DetailActivity.this,
                                AuthenticationActivity.class);
                        startActivity(intent);
                    } else {
                        logedUser = userSession.getUserSession();
                        if (logedUser.getId() == Integer.parseInt(user_id)) {
                            showDialog(getResources().getString(
                                    R.string.enquiry_alert));
                            return;
                        }

                        LayoutInflater inflater = LayoutInflater
                                .from(DetailActivity.this);
                        View promptsView = inflater.inflate(
                                R.layout.enquiry_prompts_layout, null);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                DetailActivity.this);

                        // set prompts.xml to alertdialog builder
                        alertDialogBuilder.setView(promptsView);

                        message = (EditText) promptsView
                                .findViewById(R.id.editTextDialogUserInput);
                        alertDialogBuilder.setMessage(getResources().getString(
                                R.string.send_enquiry));
                        // set dialog message
                        alertDialogBuilder
                                .setCancelable(false)
                                .setPositiveButton(
                                        getResources().getString(
                                                R.string.ok_label),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(

                                                    DialogInterface dialog, int id) {
                                                if (message.getText()
                                                        .toString()
                                                        .equalsIgnoreCase("")) {
                                                    showDialog(getResources()
                                                            .getString(
                                                                    R.string.type_message));
                                                    return;
                                                }
                                                new SendEnquiry().execute();
                                            }
                                        })
                                .setNegativeButton(
                                        getResources().getString(
                                                R.string.cancel_label),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int id) {
                                                dialog.cancel();
                                            }
                                        });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();
                    }

                }
            });

            String titleText = jsonObj.getString(Products.TAG_TITLE);
            title.setText(titleText);
            String addressText = jsonObj.getString("cities_name");
            address.setText(addressText);
            String phoneText = jsonObj.getString("phone");
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

            String priceText = jsonObj.getString(Products.TAG_PRICE);
            if (priceText != null && !priceText.equalsIgnoreCase("")) {
                price.setText(priceText);
            } else {
                price.setText(" "
                        + getResources().getString(R.string.negotiate_label));
            }

            String avtPath = jsonObj.getString(User.TAG_AVT);
            if (avtPath != null && !avtPath.equalsIgnoreCase("")) {
                Log.i(TAG, "Khac null");
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

            String content = jsonObj.getString(Products.TAG_PRODUCT_CATEGORY);
            DetailContentFragment fragment = DetailContentFragment
                    .newInstance();
            Bundle bundle = new Bundle();
            bundle.putString(DetailContentFragment.DETAIL_KEY, content);
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content, fragment).commit();

            WebView comment = (WebView) findViewById(R.id.comment);
            comment.getSettings().setJavaScriptEnabled(true);
            comment.setWebViewClient(new WebViewClient());
            WebSettings webSettings = comment.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);
            comment.getSettings().setJavaScriptEnabled(true);
            comment.getSettings().setDomStorageEnabled(true);
            //comment.getSettings().setPluginState(PluginState.ON);

            comment.loadUrl(getResources().getString(R.string.domain_url)
                    + "api/products_api/comment?id=" + product_id);
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
                reqEntity.addPart("id", new StringBody(product_id + ""));
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