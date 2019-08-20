package com.rider.myride.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public final class ConnectionUtil {

    /**
     * Default constructor.
     */
    private ConnectionUtil() {

    }

    /**
     * updateNetworkStatus: This method have network state.
     */
    public static void updateNetworkStatus(Context context) {
        AppUtil.setNetworkState(NetworkConnectionState.CONNECTING);
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                AppUtil.setNetworkState(NetworkConnectionState.CONNECTED);
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                AppUtil.setNetworkState(NetworkConnectionState.CONNECTED);
            }
        } else {
            // not connected to the internet
            AppUtil.setNetworkState(NetworkConnectionState.DISCONNECTED);
        }
    }
}
