package org.nuvola.mobile.prixpascher.cloud;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.onesignal.OneSignal;

public class InstanceIDListenerService extends FirebaseInstanceIdService {
    private static final String REG_TOKEN = "REG_TOKEN";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(REG_TOKEN, "Refreshed token: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {
        try {
            OneSignal.sendTag("deviceToken", refreshedToken);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
    }
}
