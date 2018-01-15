package org.nuvola.mobile.prixpascher.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationFiredReceiver extends BroadcastReceiver {

    public static NotificationFiredListener notificationFiredListener;

    public NotificationFiredReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent arg1) {
        if (notificationFiredListener != null) {
            notificationFiredListener.onNotificationFired();
        }
    }

    public interface NotificationFiredListener {
        void onNotificationFired();
    }
}
