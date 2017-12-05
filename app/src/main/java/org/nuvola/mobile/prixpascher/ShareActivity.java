package org.nuvola.mobile.prixpascher;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.widget.ShareDialog;

import org.nuvola.mobile.prixpascher.business.ProductFetchTask;
import org.nuvola.mobile.prixpascher.confs.constants;
import org.nuvola.mobile.prixpascher.dto.ProductVO;

import java.text.NumberFormat;
import java.util.Locale;

public class ShareActivity extends FragmentActivity {
	CallbackManager callbackManager;
	ShareDialog shareDialog;
    private ProductVO product;
    private String productId;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		callbackManager = CallbackManager.Factory.create();
		shareDialog = new ShareDialog(this);

        if (getIntent().getExtras() != null
                && getIntent().getExtras().containsKey(constants.COMMON_KEY)) {
            productId = getIntent().getExtras().getString(constants.COMMON_KEY);
            new ProductFetchTask(getResources().getString(
                    R.string.product_root_json_url)
                    + productId, handler).execute();
        }

        if (shareDialog.canShow(ShareOpenGraphContent.class)) {
            String sharedDesc = NumberFormat.getInstance(Locale.FRANCE).format(product.getPrice())  + " Dhs";
            ShareOpenGraphObject object = new ShareOpenGraphObject.Builder()
                    .putString("og:type", "product")
                    .putString("og:title", product.getTitle())
                    .putString("og:description", sharedDesc)
                    .build();

            ShareOpenGraphAction action = new ShareOpenGraphAction.Builder()
                    .setActionType("product")
                    .putObject("product", object)
                    .build();

            ShareOpenGraphContent content1 = new ShareOpenGraphContent.Builder()
                    .setPreviewPropertyName("PrixPasCherMa - Share")
                    .setAction(action)
                    .build();
            shareDialog.show(content1);
        }

		shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {

			@Override
			public void onSuccess(Sharer.Result result) {
                finish();
			}

			@Override
			public void onCancel() {
                finish();
			}

			@Override
			public void onError(FacebookException error) {
                finish();
			}
		});
	}

    public static final String TAG = "ShareActivity";
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            Bundle bundle = msg.getData();
            if (bundle.containsKey(productId)) {
                // dialog.dismiss();
                product = (ProductVO) bundle
                        .getSerializable(productId);
            }
        };
    };

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
