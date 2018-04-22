package org.nuvola.mobile.prixpascher;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.onesignal.OSNotification;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;
import org.nuvola.mobile.prixpascher.business.BadgeUtils;
import org.nuvola.mobile.prixpascher.business.LocalBroadcastManager;
import org.nuvola.mobile.prixpascher.confs.constants;
import org.nuvola.mobile.prixpascher.receivers.NetworkStateReceiver;
import org.nuvola.mobile.prixpascher.receivers.NetworkStateReceiver.ConnectivityReceiverListener;
import org.nuvola.mobile.prixpascher.receivers.NotificationFiredReceiver;
import org.nuvola.mobile.prixpascher.receivers.NotificationFiredReceiver.NotificationFiredListener;

import java.util.HashSet;
import java.util.Set;

import in.myinnos.customfontlibrary.TypefaceUtil;

import static org.nuvola.mobile.prixpascher.business.UserSessionManager.PRIVATE_MODE;
import static org.nuvola.mobile.prixpascher.business.UserSessionManager.SHARED_PREF_DATA;

public class MarketApp extends Application {

    private static MarketApp mInstance;

    public static synchronized MarketApp getInstance() {
        return mInstance;
    }

    private LocalBroadcastManager broadcaster;

    @Override
    public void onCreate() {
        super.onCreate();

        broadcaster = LocalBroadcastManager.getInstance(this);

        mInstance = this;

        TypefaceUtil.overrideFont(mInstance.getApplicationContext(), "SERIF", "assets/fonts/Roboto-Regular.ttf");

        OneSignal.startInit(this)
                .setNotificationReceivedHandler(new NotificationReceivedHandler())
                .setNotificationOpenedHandler(new NotificationOpenedHandler())
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }

    public void setConnectivityListener(ConnectivityReceiverListener listener) {
        NetworkStateReceiver.connectivityReceiverListener = listener;
    }

    public void setNotificationFiredListener(NotificationFiredListener listener) {
        NotificationFiredReceiver.notificationFiredListener = listener;
    }

    private class NotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {
        @Override
        public void notificationReceived(OSNotification notification) {
            JSONObject data = notification.payload.additionalData;
            String notificationID = notification.payload.notificationID;
            String title = notification.payload.title;
            String body = notification.payload.body;
            String smallIcon = notification.payload.smallIcon;
            String largeIcon = notification.payload.largeIcon;
            String bigPicture = notification.payload.bigPicture;
            String smallIconAccentColor = notification.payload.smallIconAccentColor;
            String sound = notification.payload.sound;
            String ledColor = notification.payload.ledColor;
            int lockScreenVisibility = notification.payload.lockScreenVisibility;
            String groupKey = notification.payload.groupKey;
            String groupMessage = notification.payload.groupMessage;
            String fromProjectNumber = notification.payload.fromProjectNumber;
            String rawPayload = notification.payload.rawPayload;

            String customKey;

            Log.i("OneSignalExample", "NotificationID received: " + notificationID);

        /*if (data != null) {
            customKey = data.optString("customkey", null);
            if (customKey != null)
                Log.i("OneSignalExample", "customkey set with value: " + customKey);
        }*/

            sendNotification(fromProjectNumber, title, body, smallIcon);
        }

        public void sendNotification(String from, String title, String body, String icon) {
            Intent broadcastIntent = new Intent("inbox");

            SharedPreferences sharePre = getApplicationContext().getSharedPreferences(
                    SHARED_PREF_DATA, PRIVATE_MODE);
            Set<String> notifs = sharePre.getStringSet("PROMOS", new HashSet<String>());
            Set<String> devis = sharePre.getStringSet("DEVIS", new HashSet<String>());
            Set<String> offers = sharePre.getStringSet("OFFERS", new HashSet<String>());
            BadgeUtils.promos = new HashSet<>();
            BadgeUtils.devis = new HashSet<>();
            BadgeUtils.offers = new HashSet<>();

            if (title.toLowerCase().contains("promo")) {
                notifs.add(body);
            }

            if (title.toLowerCase().contains("devis")) {
                devis.add(body);
            }

            if (title.toLowerCase().contains("offre") || body.toLowerCase().contains("offre")) {
                offers.add(body.split(":")[1].trim());
            }

            SharedPreferences.Editor editor = sharePre.edit();

            editor.clear();
            editor.putStringSet("PROMOS", notifs);
            editor.putStringSet("DEVIS", devis);
            editor.putStringSet("OFFERS", offers);
            editor.apply();
            editor.commit();

            BadgeUtils.promos.addAll(notifs);
            BadgeUtils.devis.addAll(devis);
            BadgeUtils.offers.addAll(offers);

            broadcaster.sendBroadcast(broadcastIntent);
        }
    }

    private class NotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        // This fires when a notification is opened by tapping on it.
        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            OSNotificationAction.ActionType actionType = result.action.type;
            JSONObject data = result.notification.payload.additionalData;
            String launchUrl = result.notification.payload.launchURL;
            String title = result.notification.payload.title;
            String body = result.notification.payload.body;

            String customKey;
            String openURL = null;
            Object activityToLaunch = ProductActivity.class;

            if (title.toLowerCase().contains("devis")) {
                activityToLaunch = AnnounceActivity.class;
            }

            if (title.toLowerCase().contains("offre") || body.toLowerCase().contains("offre")) {
                activityToLaunch = InNotificationActivity.class;
            }

            if (data != null) {
                customKey = data.optString("customkey", null);
                openURL = data.optString("openURL", null);

                if (customKey != null)
                    Log.i("OneSignalExample", "customkey set with value: " + customKey);

                if (openURL != null)
                    Log.i("OneSignalExample", "openURL to webview with URL value: " + openURL);
            }

            if (actionType == OSNotificationAction.ActionType.ActionTaken) {
                Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);

                if (result.action.actionID.equals("id1")) {
                    Log.i("OneSignalExample", "button id called: " + result.action.actionID);
                } else
                    Log.i("OneSignalExample", "button id called: " + result.action.actionID);
            }
            // The following can be used to open an Activity of your choice.
            // Replace - getApplicationContext() - with any Android Context.
            // Intent intent = new Intent(getApplicationContext(), YourActivity.class);
            Intent intent = new Intent(getApplicationContext(), (Class<?>) activityToLaunch);
            intent.putExtra(constants.COMMON_KEY, body);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("openURL", openURL);
            Log.i("OneSignalExample", "openURL = " + openURL);
            // startActivity(intent);
            startActivity(intent);
        }
    }
}
