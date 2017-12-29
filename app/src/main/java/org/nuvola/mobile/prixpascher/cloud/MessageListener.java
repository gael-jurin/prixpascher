package org.nuvola.mobile.prixpascher.cloud;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;

import org.nuvola.mobile.prixpascher.HomeActivity;

public class MessageListener extends GcmListenerService {
    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        /*
         * retrieve our message bundle from data bundle argument under
         * "notification" key.
         */
        Bundle bundle = data.getBundle("notification");
        String title = bundle.getString("title");
        String body = bundle.getString("body");
        String icon = bundle.getString("icon");

        /*
         * alert user of device
         */
        sendNotification(title, body, icon);
    }
    private void sendNotification(String title, String body, String icon) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        /*
         * We want to use the icon you specify in the message sent as the
         * notification icon. But it comes as a string of the image name. We
         * shall assume its inside the "drawable" directory and obtain its
         * resourceId from the supplied name, if this image is not found, the
         * notification sound will play but nothing shows on status bar.
         */
        Resources resources = getResources();
        int resourceId = resources.getIdentifier(icon, "drawable",
                getPackageName());
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(resourceId).setContentTitle(title)
                .setContentText(body).setAutoCancel(true)
                .setSound(defaultSoundUri).setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */,
                notificationBuilder.build());
    }
}
