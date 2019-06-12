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


    public static  Bitmap getResizedBitmap(Bitmap image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int maxWidth=1024;
        int maxHeigh=768;

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxWidth;
            height = (int) (width / bitmapRatio);
        } else {
            height = 768;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static String converttoBase64(ImageView profile) {
        BitmapDrawable drawable = (BitmapDrawable) profile.getDrawable();
        Bitmap bitmap = getResizedBitmap(drawable.getBitmap());


        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();


        return  Base64.encodeToString(byteArray, Base64.DEFAULT);
    }


    public static String getuserid(Context applicationContext) {

        SharedPreferences sharedpreferences = applicationContext.getSharedPreferences(applicationContext.getPackageName(), Context.MODE_PRIVATE);

       return sharedpreferences.getString("userId","0");

    }
    public static String getcarid(Context applicationContext) {

        SharedPreferences sharedpreferences = applicationContext.getSharedPreferences(applicationContext.getPackageName(), Context.MODE_PRIVATE);

        return sharedpreferences.getString("userId","0");

    }

}
