package org.nuvola.mobile.prixpascher.cloud;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.TimeZone;

import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectCallback;
import io.cloudboost.CloudPush;

public class InstanceIDListenerService extends FirebaseInstanceIdService {
    private static final String REG_TOKEN = "REG_TOKEN";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(REG_TOKEN, "Refreshed token: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {
        String[] channels = {"global"};
        try {
            new CloudPush().addDevice(refreshedToken, TimeZone.getDefault().getDisplayName(), channels, "prixpascher",
                    new CloudObjectCallback() {
                        @Override
                        public void done(CloudObject x, CloudException t) throws CloudException {
                        }
                    });
        } catch (CloudException e) {
            Log.e("Error", e.getMessage());
        }
    }
}
