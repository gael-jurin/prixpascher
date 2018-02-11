package org.nuvola.mobile.prixpascher.tasks;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.nuvola.mobile.prixpascher.R;
import org.nuvola.mobile.prixpascher.business.UserSessionManager;
import org.nuvola.mobile.prixpascher.models.User;

import java.io.File;

public class UploadTask extends AsyncTask<Void, Void, Boolean> {
    private String fb_id = null;
    private String user_id = "0";
    private String photoPath1 = null;
    private String photoId = "";
    private Context context;
    private UploadType type;
    private UploadCompletedListener mListener;

    ProgressDialog dialog;
    File tmpFile;

    public UploadTask(UploadType type) {
        this.type = type;
    }

    public UploadTask(UploadType type, String fb_id, String user_id,
                      File tmpFile, Context context, UploadCompletedListener mListener) {
        this.fb_id = fb_id;
        this.user_id = user_id;
        this.context = context;
        this.type = type;
        this.mListener = mListener;
        this.tmpFile = tmpFile;
        this.photoId = user_id + "_" + tmpFile.getName();
        this.photoPath1 = tmpFile.getAbsolutePath();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        dialog.dismiss();
    }

    protected void onPostExecute(Boolean result) {
        dialog.dismiss();
        if (mListener != null) {
            mListener.onUploadCompleted(result);
        }
    };

    @Override
    protected void onPreExecute() {
        if (tmpFile == null) {
            cancel(true);
        }

        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setMessage(context.getResources().getString(
                R.string.upload_product_msg));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        if (UploadType.OFFER.equals(type)) {
            String handleInserUrl = context.getResources().getString(
                    R.string.gallery_post_url) // TODO : Use HTTPS upload too
                    + photoId;
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(handleInserUrl);
                MultipartEntityBuilder reqEntityBuilder = MultipartEntityBuilder.create();

                FileBody fileUpload;
                if (photoPath1 != null) {
                    fileUpload = new FileBody(new File(photoPath1), ContentType.MULTIPART_FORM_DATA, "");
                    reqEntityBuilder.addPart("photo1", fileUpload);
                }

                post.setEntity(reqEntityBuilder.build());
                HttpResponse response = client.execute(post);
                HttpEntity resEntity = response.getEntity();
                final String response_str = EntityUtils.toString(resEntity);
                if (response.getStatusLine().getStatusCode() == 200) {
                    Log.i("RESPONSE", response_str);
                    return true;
                } else {
                    return false;
                }
            } catch (Exception ex) {
                Log.e("Debug", "error: " + ex.getMessage(), ex);
                return false;
            }
        }

        if (UploadType.PRODUCT.equals(type)) {
            String handleInserUrl = context.getResources().getString(
                    R.string.products_json_url)
                    + "products";
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(handleInserUrl);
                MultipartEntityBuilder reqEntityBuilder = MultipartEntityBuilder.create();

                FileBody fileUpload;
                if (photoPath1 != null) {
                    fileUpload = new FileBody(new File(photoPath1));
                    reqEntityBuilder.addPart("photo1", fileUpload);
                }

                post.setEntity(reqEntityBuilder.build());
                HttpResponse response = client.execute(post);
                HttpEntity resEntity = response.getEntity();
                final String response_str = EntityUtils.toString(resEntity);
                if (resEntity != null) {
                    Log.i("RESPONSE", response_str);
                /*runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            dialog.dismiss();
                            title.setText("");
                            price.setText("");
                            content.setText("");
                            btnPickPhoto_1
                                    .setImageResource(R.drawable.ic_picker_photo);
                            btnPickPhoto_2
                                    .setImageResource(R.drawable.ic_picker_photo);
                            btnPickPhoto_3
                                    .setImageResource(R.drawable.ic_picker_photo);
                            photoPath1 = null;

                            Log.i(TAG, "upload");
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                });*/
                }
            } catch (Exception ex) {
                Log.e("Debug", "error: " + ex.getMessage(), ex);
            }
            return null;
        }

        if (UploadType.CHANGE_PASS.equals(type)) {
            String handleInserUrl = context.getResources().getString(
                    R.string.users_json_url)
                    + "pwd";
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(handleInserUrl);
                MultipartEntityBuilder reqEntityBuilder = MultipartEntityBuilder.create();

                UserSessionManager userSessionManager = new UserSessionManager(
                        context);
                User user = userSessionManager.getUserSession();
                // String oldPassText = oldPass.getText().toString();
                // String newPassText = newPass.getText().toString();
                // String cfmPassText=cfmPass.getText().toString();
				/*reqEntity.addPart("id", new StringBody(user.getId() + ""));
				reqEntity.addPart("old_pass", new StringBody(oldPassText));
				reqEntity.addPart("new_pass", new StringBody(newPassText));*/
                post.setEntity(reqEntityBuilder.build());
                HttpResponse response = client.execute(post);
                HttpEntity resEntity = response.getEntity();
                final String jsonString = EntityUtils.toString(resEntity);

                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setPositiveButton(
                        context.getResources().getString(R.string.ok_label),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub

                            }
                        });
                if (resEntity != null) {
                    /*runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                dialogPrg.dismiss();
                                JSONObject jsonObj = new JSONObject(jsonString);
                                if (jsonObj.getString("ok").equals("1")) {
						*//*			builder.setMessage(getResources()
											.getString(
													R.string.update_success_label));
									AlertDialog dialog = builder.create();
									dialog.show();*//*

                                    showDialog(context.getResources()
                                            .getString(
                                                    R.string.update_success_label));
                                } else {
								*//*	builder.setMessage(getResources()
											.getString(
													R.string.update_failed_label));
									AlertDialog dialog = builder.create();
									dialog.show();*//*

                                    showDialog(context.getResources()
                                            .getString(
                                                    R.string.update_failed_label));
                                }

                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    });*/
                }
            } catch (Exception ex) {
                Log.e("Debug", "error: " + ex.getMessage(), ex);
            }
            return null;
        }
        return true;
    }
}
