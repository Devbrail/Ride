package com.rider.myride.Services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.net.URLEncoder;


public class ApiCall {
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyDP8iWw5hhVODKoi06-BycGaDkOna8W7So";
    private static final String TAG = "ApiCall";
    private static ApiCall mInstance;
    private static Context mCtx;
    private RequestQueue mRequestQueue;

    public ApiCall(Context ctx) {
        mCtx = ctx;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized ApiCall getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ApiCall(context);

        }
        return mInstance;
    }

    public static void make(Context ctx, String query, Boolean b, Response.Listener<String>
            listener, Response.ErrorListener errorListener) {
        try {
            String url = null;
            if (b) {
                SharedPreferences sharedpreferences = ctx.getSharedPreferences(ctx.getPackageName(), Context.MODE_PRIVATE);
                String countrycode = sharedpreferences.getString("countrycode", "ke");
                countrycode = countrycode.toLowerCase();
                Log.d("Hiii", countrycode);

                url = PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON + "?key=" +
                        API_KEY + "&components=country:" + countrycode + "&input=" +
                        URLEncoder.encode(query, "utf8");

            } else {
                url = "https://maps.googleapis.com/maps/api/geocode/json?address=" +
                        URLEncoder.encode(query, "utf8") + "&key=" + API_KEY;
            }
            Log.d(TAG, "make: "+url);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    listener, errorListener);
            ApiCall.getInstance(ctx).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            Log.d(TAG, "make: " + e.getMessage());
        }

    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
