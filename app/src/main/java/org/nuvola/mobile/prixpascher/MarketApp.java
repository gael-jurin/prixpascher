package org.nuvola.mobile.prixpascher;

import android.app.Application;

import in.myinnos.customfontlibrary.TypefaceUtil;

public class MarketApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "assets/fonts/Roboto-Regular.ttf");
    }
}
