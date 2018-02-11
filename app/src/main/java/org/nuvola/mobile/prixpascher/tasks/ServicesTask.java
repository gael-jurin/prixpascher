package org.nuvola.mobile.prixpascher.tasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gc.materialdesign.views.ButtonFlat;

import org.nuvola.mobile.prixpascher.R;
import org.nuvola.mobile.prixpascher.business.Utils;
import org.nuvola.mobile.prixpascher.dto.ContactMailVO;
import org.nuvola.mobile.prixpascher.dto.PriceAlertVO;
import org.nuvola.mobile.prixpascher.dto.ProductAnnonceVO;
import org.nuvola.mobile.prixpascher.dto.ProductVO;
import org.nuvola.mobile.prixpascher.dto.ReviewVO;
import org.nuvola.mobile.prixpascher.models.AnnounceStatus;
import org.nuvola.mobile.prixpascher.models.AnnounceType;
import org.nuvola.mobile.prixpascher.models.City;
import org.nuvola.mobile.prixpascher.models.ProductSource;
import org.nuvola.mobile.prixpascher.models.Taskable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;

public class ServicesTask extends AsyncTask<Void, Void, Boolean> {
    private EditText alertEmail;
    private EditText alertPrice;

    private EditText qtyDevisUserInput;
    private EditText priceDevisUserInput;
    private EditText emailDevisUserInput;
    private EditText telDevisUserInput;
    private EditText infoDevisUserInput;
    private TextView titleDevisInput;
    private Spinner categoriesSpinner, countySpinner, aimSpinner, citiesSpinner;

    private ServiceType serviceType;
    private Context context;

    private ProductVO product;
    private ProductAnnonceVO annonce;
    private ContactMailVO contact;
    private ReviewVO review;

    private ProgressDialog dialog;

    private String mail;
    private String price;
    private String tel;

    private String askQty;
    private String askInfos;

    private String[] city_name;
    private String city_selected;

    public ServicesTask(ServiceType type, Context activity, Taskable taskable) {
        serviceType = type;
        context = activity;
        dialog = new ProgressDialog(context);

        if (taskable instanceof ProductVO) {
            product = (ProductVO) taskable;
        }

        if (taskable instanceof ProductAnnonceVO) {
            annonce = (ProductAnnonceVO) taskable;
        }

        if (taskable instanceof ReviewVO) {
            review = (ReviewVO) taskable;
        }

        if (taskable instanceof ContactMailVO) {
            contact = (ContactMailVO) taskable;
        }

        if (serviceType.equals(ServiceType.ALERT)) {
            MaterialDialog.Builder alertDialogBuilder = new MaterialDialog.Builder(context);
            alertDialogBuilder.customView(R.layout.alerts_prompts_layout, true);

            final MaterialDialog alertDialog = alertDialogBuilder.build();
            alertDialog.setCanceledOnTouchOutside(true);

            alertEmail = (EditText) alertDialog.findViewById(R.id.emailDialogUserInput);
            alertPrice = (EditText) alertDialog.findViewById(R.id.priceDialogUserInput);
            Double suggestPrice = Math.ceil(product.getPrice() - (product.getPrice() * 0.2));
            if (alertPrice != null) {
                alertPrice.setText(suggestPrice.toString());
            }
            ButtonFlat sendAlert = (ButtonFlat) alertDialog.findViewById(R.id.sendAlert);

            sendAlert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    execute();
                    alertDialog.dismiss();
                }

            });
            alertDialog.show();
        }

        if (serviceType.equals(ServiceType.ASK_OFFER)) {
            MaterialDialog.Builder alertDialogBuilder = new MaterialDialog.Builder(context);
            alertDialogBuilder.customView(R.layout.devisask_prompts_layout, true);

            final MaterialDialog alertDialog = alertDialogBuilder.build();
            alertDialog.setCanceledOnTouchOutside(true);

            titleDevisInput = (TextView) alertDialog.findViewById(R.id.titleDevisInput);
            priceDevisUserInput = (EditText) alertDialog.findViewById(R.id.priceDevisUserInput);
            qtyDevisUserInput = (EditText) alertDialog.findViewById(R.id.qtyDevisUserInput);
            emailDevisUserInput = (EditText) alertDialog.findViewById(R.id.emailDevisUserInput);
            infoDevisUserInput = (EditText) alertDialog.findViewById(R.id.infoDevisUserInput);
            telDevisUserInput = (EditText) alertDialog.findViewById(R.id.telDevisUserInput);
            citiesSpinner = (Spinner) alertDialog.findViewById(R.id.cities_spinner);
            city_name = City.names();
            city_selected = City.Casablanca.name();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    context,
                    android.R.layout.simple_list_item_1, city_name);
            citiesSpinner.setAdapter(adapter);
            citiesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                            city_selected = city_name[arg2];
                            city_selected = city_selected.replaceAll(" ", "_");
                        }
                        @Override
                        public void onNothingSelected(
                                AdapterView<?> arg0) {
                        }
                    });

            if (titleDevisInput != null) {
                titleDevisInput.setText(annonce.getTitle());
            }
            if (qtyDevisUserInput != null) {
                qtyDevisUserInput.setText("1");
            }
            if (emailDevisUserInput != null) {
                emailDevisUserInput.setText(annonce.getContactMail());
            }
            if (priceDevisUserInput != null) {
                priceDevisUserInput.setText(annonce.getPrice().toString());
            }
            ButtonFlat sendAlert = (ButtonFlat) alertDialog.findViewById(R.id.sendRequest);

            sendAlert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    execute();
                    alertDialog.dismiss();
                }

            });
            alertDialog.show();
        }
    }

    @Override
    protected void onCancelled() {
        // TODO Auto-generated method stub
        super.onCancelled();
        dialog.dismiss();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        dialog.setMessage(context.getResources().getString(
                R.string.please_wait_msg));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        if (serviceType.equals(ServiceType.ALERT)) {
            mail = alertEmail.getText().toString();
            price = alertPrice.getText().toString();
        }

        if (serviceType.equals(ServiceType.ASK_OFFER)) {
            mail = emailDevisUserInput.getText().toString();
            price = priceDevisUserInput.getText().toString();
            askQty = qtyDevisUserInput.getText().toString();
            askInfos = infoDevisUserInput.getText().toString();
            tel = telDevisUserInput.getText().toString();
        }
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        if (serviceType.equals(ServiceType.ALERT)) {
            PriceAlertVO priceAlertVO = new PriceAlertVO();
            priceAlertVO.setProductId(product.getId());
            priceAlertVO.setEmail(mail);
            priceAlertVO.setPrice(price);

            try {
                HttpEntity<PriceAlertVO> requestEntity = new HttpEntity<>(priceAlertVO);
                ResponseEntity<String> resEntity = Utils.MyRestemplate.getInstance(context).exchange(
                        context.getResources().getString(R.string.alert_send_url),
                        HttpMethod.POST,
                        requestEntity, String.class);

                if (resEntity != null) {
                    Log.i("RESPONSE", resEntity.getBody());
                    if (!resEntity.getStatusCode().equals(HttpStatus.OK)) {
                        showDialog(context.getResources().getString(R.string.spam_msg));
                    }
                    dialog.dismiss();
                    return true;
                }
            } catch (Exception ex) {
                dialog.dismiss();
                Log.e("Debug", "error: " + ex.getMessage(), ex);
                return false;
            }
        }

        if (serviceType.equals(ServiceType.ASK_OFFER)) {
            annonce.setType(AnnounceType.OFFER_ASK);
            annonce.setSource(ProductSource.ANNONCE);
            annonce.setTrackingDate(new Date());
            annonce.setCityName(city_selected);
            annonce.setVille(City.valueOf(city_selected));
            annonce.setStatus(AnnounceStatus.SUBMITTED);
            if (!mail.isEmpty()) {
                annonce.setContactMail(mail);
            }
            annonce.setContactPhone(tel);
            if (!askQty.isEmpty() && !price.isEmpty()) {
                annonce.setQty(Long.valueOf(askQty));
                annonce.setPrice(Double.valueOf(price));
            }

            try {
                HttpEntity<ProductAnnonceVO> requestEntity = new HttpEntity<>(annonce);
                ResponseEntity<AnnounceStatus> resEntity = Utils.MyRestemplate.getInstance(context).exchange(
                        context.getResources().getString(R.string.announce_send_json_url),
                        HttpMethod.POST,
                        requestEntity, AnnounceStatus.class);

                if (resEntity != null) {
                    // Log.i("RESPONSE", resEntity.getBody());
                    if (!resEntity.getStatusCode().equals(HttpStatus.OK)) {
                        showDialog(context.getResources().getString(R.string.spam_msg));
                    } else {
                        // Send alert if OK
                        ResponseEntity<String> responseEntity = Utils.MyRestemplate.getInstance(context).exchange(
                                context.getResources().getString(R.string.announce_notif_json_url),
                                HttpMethod.POST,
                                requestEntity, String.class);
                    }
                    dialog.dismiss();
                    return true;
                }
            } catch (Exception ex) {
                dialog.dismiss();
                Log.e("Debug", "error: " + ex.getMessage(), ex);
                return false;
            }
        }

        if (serviceType.equals(ServiceType.REPLY_OFFER)) {
            try {
                HttpEntity<ContactMailVO> requestEntity =
                        new HttpEntity<>(contact);

                ResponseEntity<String> resEntity = Utils.MyRestemplate.getInstance(context).exchange(
                        context.getResources().getString(R.string.msg_send_url),
                        HttpMethod.POST,
                        requestEntity, String.class);

                if (resEntity != null) {
                    Log.i("RESPONSE", resEntity.getBody());
                    if (!resEntity.getStatusCode().equals(HttpStatus.OK)) {
                        showDialog(context.getResources().getString(
                                R.string.spam_msg));
                    } else {
                        showDialog(context.getResources().getString(R.string.devis_success_msg));
                    }
                }
            } catch (Exception ex) {
                dialog.dismiss();
                // showDialog(context.getResources().getString(R.string.error_alert));
                Log.e("Debug", "error: " + ex.getMessage(), ex);
                return false;
            }
        }

        if (serviceType.equals(ServiceType.RATING)) {
            try {
                HttpEntity<ReviewVO> requestEntity = new HttpEntity<>(review);
                ResponseEntity<ProductVO> resEntity = Utils.MyRestemplate.getInstance(context).exchange(
                        context.getResources().getString(R.string.review_send_url),
                        HttpMethod.POST,
                        requestEntity, ProductVO.class);

                if (resEntity != null) {
                    if (!resEntity.getStatusCode().equals(HttpStatus.OK)) {
                        showDialog(context.getResources().getString(
                                R.string.http_call_error));
                    } else {
                        showDialog(context.getResources().getString(R.string.review_success_msg));
                    }
                }
            } catch (Exception ex) {
                dialog.dismiss();
                Log.e("Debug", "error: " + ex.getMessage(), ex);
                return false;
            }
        }

        dialog.dismiss();
        return true;
    }

    private void showDialog(final String msg) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(
                        msg)
                        .setTitle(context.getResources().getString(R.string.info_msg))
                        .setPositiveButton(
                                context.getResources().getString(R.string.ok_label),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                    }
                                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}
