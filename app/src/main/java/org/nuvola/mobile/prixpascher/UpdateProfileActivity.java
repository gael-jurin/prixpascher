package org.nuvola.mobile.prixpascher;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFlat;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.nuvola.mobile.prixpascher.business.UserSessionManager;
import org.nuvola.mobile.prixpascher.business.Utils;
import org.nuvola.mobile.prixpascher.confs.constants;
import org.nuvola.mobile.prixpascher.models.User;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@SuppressLint({"NewApi", "ShowToast"})
public class UpdateProfileActivity extends ActionBarParentActivity {
    EditText website, address, phone;
    ButtonFlat btnUpdate;
    User user;
    public static final String TAG = "EditProfileActivity";
    ImageView avt;
    private static final int SELECT_PICTURE = 0;
    private static final int TAKE_PICTURE = 1;
    File tmpFile;
    String avtPath = null;
    CharSequence[] items;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.update_profile_label);

        setSupportActionBar(toolbar);
        btnUpdate = (ButtonFlat) findViewById(R.id.btn_update);

        website = (EditText) findViewById(R.id.websites);
        address = (EditText) findViewById(R.id.address);
        phone = (EditText) findViewById(R.id.trackingDate);
        avt = (ImageView) findViewById(R.id.avt);
        if (getIntent().getExtras() != null
                && getIntent().getExtras().containsKey(constants.COMMON_KEY)) {
            user = getIntent().getExtras().getParcelable(constants.COMMON_KEY);
        }

        items = getResources().getStringArray(R.array.choose_photo);
        website.setText(user.getWebsite());
        address.setText(user.getAddress());
        phone.setText(user.getPhone());
        if (user.getAvt() != null && !user.getAvt().equalsIgnoreCase("")) {
            String avtString = user.getAvt();

            Utils.MyPicasso.with(this)
                    .load(avtString)
                    .resize(200, 200).centerCrop()
                    .placeholder(R.drawable.ic_avatar)
                    .error(R.drawable.ic_avatar)
                    .into(avt);
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                UserSessionManager userSessionManager = new UserSessionManager(
                        UpdateProfileActivity.this);
                if (userSessionManager.getUserSession() != null) {
                    if (!Utils
                            .isConnectingToInternet(UpdateProfileActivity.this)) {
                        showMsg(getResources().getString(R.string.open_network));
                    } else {
                        Utils.showCommingSoon(UpdateProfileActivity.this);
                    }
                } else {
                    Intent intent = new Intent(UpdateProfileActivity.this,
                            AuthenticationActivity.class);
                    startActivity(intent);
                }
            }
        });

        avt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showChoosePhotoMethod();
            }
        });

        //getSupportActionBar().setTitle(R.string.update_profile_label);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void showMsg(String msg) {
        Toast ts = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        ts.show();
    }

    private void showChoosePhotoMethod() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.what_you_want));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        pickPhoto();
                        break;

                    case 1:
                        capturePhoto();
                        break;

                    default:
                        break;
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null,
                null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String filePath = cursor.getString(column_index);
        return filePath;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SELECT_PICTURE:
                    final String selectAbpath = getPath(data.getData());
                    Log.i(TAG, selectAbpath);
                    Utils.MyPicasso.with(this).load(new File(selectAbpath).getAbsoluteFile())
                            .placeholder(R.drawable.ic_small_avatar)
                            .error(R.drawable.ic_small_avatar).resize(200, 200)
                            .centerCrop();
                            /*.setCallback(new FutureCallback<Bitmap>() {
                                @Override
                                public void onCompleted(Exception arg0,
                                                        Bitmap bitmap) {
                                    // TODO Auto-generated method stub
                                    try {
                                        RoundedAvatarDrawable avtDrawable = new RoundedAvatarDrawable(
                                                bitmap);
                                        avt.setImageDrawable(avtDrawable);
                                        avtPath = new File(selectAbpath)
                                                .getAbsolutePath();
                                    } catch (Exception e) {
                                        // TODO: handle exception
                                    }
                                }
                            });*/
                    break;

                case TAKE_PICTURE:
                    if (tmpFile.exists()) {
                        Utils.MyPicasso.with(this).load(tmpFile.getAbsoluteFile())
                                .placeholder(R.drawable.ic_small_avatar)
                                .error(R.drawable.ic_small_avatar).resize(200, 200)
                                .centerCrop();
                                /*.setCallback(new FutureCallback<Bitmap>() {
                                    @Override
                                    public void onCompleted(Exception arg0,
                                                            Bitmap bitmap) {
                                        // TODO Auto-generated method stub
                                        RoundedAvatarDrawable avtDrawable = new RoundedAvatarDrawable(
                                                bitmap);
                                        avt.setImageDrawable(avtDrawable);
                                        avtPath = tmpFile.getAbsolutePath();
                                    }
                                });*/
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void pickPhoto() {
        // TODO: launch the photo picker
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select"),
                SELECT_PICTURE);
    }

    public void capturePhoto() {
        String ts = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
                .format(new Date());
        File dir = Utils.createDirOnSDCard(getResources().getString(
                R.string.folder_save_photo));
        tmpFile = new File(dir.getPath() + "/Photo_" + ts + ".jpg");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (tmpFile != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tmpFile));
            startActivityForResult(intent, TAKE_PICTURE);
        }
    }

    private void upload() {
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new Upload().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new Upload().execute();
        }*/


    }

    ProgressDialog dialog;

    private class Upload extends AsyncTask<Void, Void, Boolean> {

        public Upload() {
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
            dialog = new ProgressDialog(UpdateProfileActivity.this);
            dialog.setMessage(UpdateProfileActivity.this.getResources()
                    .getString(R.string.please_wait_msg));
            // dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO Auto-generated method stub
            String handleInserUrl = getResources().getString(
                    R.string.users_json_url)
                    + "update";
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(handleInserUrl);
                MultipartEntity reqEntity = new MultipartEntity();

                // String websiteText = website.getText().toString();
                // String addressText = address.getText().toString();
                // String phoneText = phone.getText().toString();

                // reqEntity.addPart("id", new StringBody(user.getId() + ""));
                // reqEntity.addPart("fb_id", new StringBody(user.getFbId()));
                // reqEntity.addPart("website", new StringBody(websiteText));
                // reqEntity.addPart("address", new StringBody(addressText));
                // reqEntity.addPart("productFeed", new StringBody(phoneText));
                if (avtPath != null && !avtPath.equalsIgnoreCase("")) {
                    // reqEntity.addPart("avt", new FileBody(new File(avtPath)));
                }

                post.setEntity(reqEntity);
                HttpResponse response = client.execute(post);
                HttpEntity resEntity = response.getEntity();
                final String response_str = EntityUtils.toString(resEntity);
                if (resEntity != null) {
                    Log.i("RESPONSE", response_str);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                try {
                                    JSONArray jsonArray = new JSONArray(
                                            response_str);
                                    if (jsonArray.length() == 1) {
                                        JSONObject obj = jsonArray
                                                .getJSONObject(0);
                                        User user = Utils.parseUser(obj);
                                        UserSessionManager userSession = new UserSessionManager(
                                                UpdateProfileActivity.this);
                                        // userSession.storeUserSession(user);
                                        Log.i(TAG, user.getAvt() + "ksjd");
                                    }
                                } catch (Exception e) {
                                    showDialogFailedUpdate();
                                    // TODO: handle exception
                                }
                                dialog.dismiss();
                                Log.i(TAG, "upload");
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    });
                }
            } catch (Exception ex) {
                Log.e("Debug", "error: " + ex.getMessage(), ex);
            }
            return null;
        }
    }

    ;

    public void showDialogFailedUpdate() {
        AlertDialog.Builder buidler = new AlertDialog.Builder(this);
        buidler.setMessage(getString(R.string.update_profile_failed_label));
        buidler.setPositiveButton(getResources().getString(R.string.ok_label),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method
                        // stub
                    }
                });
        AlertDialog dialog = buidler.create();
        dialog.show();
    }
}
