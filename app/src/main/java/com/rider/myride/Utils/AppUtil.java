package com.rider.myride.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AppUtil {

    /**
     * networkState: This is global variable contain network state.
     */
    private static NetworkConnectionState networkState;

    public static Bitmap getbmpfromURL(String surl) {
        try {
            URL url = new URL(surl);
            HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
            urlcon.setDoInput(true);
            urlcon.connect();
            InputStream in = urlcon.getInputStream();
            Bitmap mIcon = BitmapFactory.decodeStream(in);
            return mIcon;
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e("Error", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static NetworkConnectionState getNetworkState() {
        return networkState;
    }

    public static void setNetworkState(NetworkConnectionState networkConnectionState) {
        networkState = networkConnectionState;
    }

    public static Bitmap getResizedBitmap(Bitmap image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int maxWidth = 1024;
        int maxHeigh = 768;

        float bitmapRatio = (float) width / (float) height;
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
        byte[] byteArray = byteArrayOutputStream.toByteArray();


        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }


    public static String getuserid(Context applicationContext) {

        SharedPreferences sharedpreferences = applicationContext.getSharedPreferences(applicationContext.getPackageName(), Context.MODE_PRIVATE);

        return sharedpreferences.getString("userid", "0");

    }

    public static String getprofile(Context applicationContext) {

        SharedPreferences sharedpreferences = applicationContext.getSharedPreferences(applicationContext.getPackageName(), Context.MODE_PRIVATE);

        return sharedpreferences.getString("profile", "0");

    }

    public static String getdriverid(Context applicationContext) {

        SharedPreferences sharedpreferences = applicationContext.getSharedPreferences(applicationContext.getPackageName(), Context.MODE_PRIVATE);

        return sharedpreferences.getString("driverId", "0");

    }

    public static String parseUserid(String jsonString) {

        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            return jsonObject.getString("userId");
        } catch (JSONException e) {
            Crashlytics.logException(e);
            e.printStackTrace();
            return null;
        }

    }

    public static String getvehicledetails(Context applicationContext) {

        SharedPreferences sharedpreferences = applicationContext.getSharedPreferences(applicationContext.getPackageName(), Context.MODE_PRIVATE);

        return sharedpreferences.getString("vehicledetails", "0");

    }

    public static String parseVehicleinfo(String jsonString) {

        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            return jsonObject.getString("carId");
        } catch (JSONException e) {
            Crashlytics.logException(e);
            e.printStackTrace();
            return null;
        }

    }


}
