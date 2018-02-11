package org.nuvola.mobile.prixpascher;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import org.nuvola.mobile.prixpascher.business.CustomWebViewClient;
import org.nuvola.mobile.prixpascher.confs.constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoOnlineShopActivity extends ActionBarParentActivity {

    @BindView(R.id.content) WebView content;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private String productUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.go_online_shop);
        ButterKnife.bind(this);

        if (getIntent().getExtras() != null
                && getIntent().getExtras().containsKey(constants.COMMON_KEY)) {
            productUrl = getIntent().getExtras().getString(constants.COMMON_KEY);

            content.getSettings().setJavaScriptEnabled(true);
            content.setWebViewClient(new CustomWebViewClient());
            content.loadUrl(productUrl);
        }

        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
}
