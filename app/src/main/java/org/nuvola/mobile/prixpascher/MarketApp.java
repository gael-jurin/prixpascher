package org.nuvola.mobile.prixpascher;

import android.app.Application;

import org.nuvola.mobile.prixpascher.receivers.NetworkStateReceiver;
import org.nuvola.mobile.prixpascher.receivers.NetworkStateReceiver.ConnectivityReceiverListener;
import org.nuvola.mobile.prixpascher.receivers.NotificationFiredReceiver;
import org.nuvola.mobile.prixpascher.receivers.NotificationFiredReceiver.NotificationFiredListener;

import in.myinnos.customfontlibrary.TypefaceUtil;

public class MarketApp extends Application {

    private static MarketApp mInstance;

    public static synchronized MarketApp getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        TypefaceUtil.overrideFont(mInstance.getApplicationContext(), "SERIF", "assets/fonts/Roboto-Regular.ttf");
    }

    public void setConnectivityListener(ConnectivityReceiverListener listener) {
        NetworkStateReceiver.connectivityReceiverListener = listener;
    }

    public void setNotificationFiredListener(NotificationFiredListener listener) {
        NotificationFiredReceiver.notificationFiredListener = listener;
    }
}
