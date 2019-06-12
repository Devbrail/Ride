package com.example.myride.Utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class AppUtil {

    /**
     * networkState: This is global variable contain network state.
     */
    private static NetworkConnectionState networkState;

    public static void setNetworkState(NetworkConnectionState networkConnectionState) {
        networkState = networkConnectionState;
    }

    public static NetworkConnectionState getNetworkState() {
        return networkState;
    }




    public static String converttoBase64(ImageView profile) {
        BitmapDrawable drawable = (BitmapDrawable) profile.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();


        return  Base64.encodeToString(byteArray, Base64.DEFAULT);
    }


    public static String getuserid(Context applicationContext) {

        SharedPreferences sharedpreferences = applicationContext.getSharedPreferences("Login", Context.MODE_PRIVATE);

       return sharedpreferences.getString("userId",null);

    }
}
