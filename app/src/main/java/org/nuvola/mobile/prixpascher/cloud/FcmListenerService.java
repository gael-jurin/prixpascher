package org.nuvola.mobile.prixpascher.cloud;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.nuvola.mobile.prixpascher.ActionBarParentActivity;
import org.nuvola.mobile.prixpascher.AnnounceActivity;
import org.nuvola.mobile.prixpascher.InNotificationActivity;
import org.nuvola.mobile.prixpascher.ProductActivity;
import org.nuvola.mobile.prixpascher.business.BadgeUtils;
import org.nuvola.mobile.prixpascher.confs.constants;

import java.util.HashSet;
import java.util.Set;

import static org.nuvola.mobile.prixpascher.business.UserSessionManager.PRIVATE_MODE;
import static org.nuvola.mobile.prixpascher.business.UserSessionManager.SHARED_PREF_DATA;

public class FcmListenerService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage message){
        String from = message.getFrom();
        RemoteMessage.Notification data = message.getNotification();

        String title = data.getTitle();
        String body = data.getBody();
        String icon = "ic_logo";

        sendNotification(from, title, body, icon);
    }

    public void sendNotification(String from, String title, String body, String icon) {
        Intent intent = new Intent(this, ProductActivity.class);

        if (title.toLowerCase().contains("devis")) {
            intent = new Intent(this, AnnounceActivity.class);
        }

        if (title.toLowerCase().contains("offre")) {
            intent = new Intent(this, InNotificationActivity.class);
        }

        intent.putExtra(constants.COMMON_KEY, body);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Resources resources = getResources();
        int resourceId = resources.getIdentifier(icon, "drawable",
                getPackageName());

        Intent intentBar = new Intent(this, ActionBarParentActivity.class);
        intentBar.putExtra(constants.COMMON_KEY, body);

        int unOpenCount = BadgeUtils.counter;
        unOpenCount = unOpenCount+1;

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                this, from).setSmallIcon(resourceId).setContentTitle(title)
                .setContentText(body).setAutoCancel(true)
                .setNumber(unOpenCount)
                .setOnlyAlertOnce(true)
                .setSound(defaultSoundUri).setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(unOpenCount, notificationBuilder.build());

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

        if (title.toLowerCase().contains("offre")) {
            offers.add(body);
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

        Integer count = BadgeUtils.promos.size() + BadgeUtils.devis.size() +
                BadgeUtils.offers.size();
        BadgeUtils.setBadge(FcmListenerService.this, count);
    }

    @Override
    public void handleIntent(Intent intent) {
        try {
            if (intent.getExtras() != null) {
                RemoteMessage.Builder builder = new RemoteMessage.Builder("FcmListenerService");

                for (String key : intent.getExtras().keySet()) {
                    builder.addData(key, intent.getExtras().get(key).toString());
                }
                onMessageReceived(builder.build());
            } else {
                super.handleIntent(intent);
            }
        } catch (Exception e) {
            super.handleIntent(intent);
        }
    }
}
