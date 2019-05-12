package com.example.myride.model;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import java.net.URLEncoder;

/**
 * Created by MG on 04-03-2018.
 */

public class ApiCall {
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyAhgOKhJsKlAvZ_2PCWgu-2ysY79fI54G4";

    private static ApiCall mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

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

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public static void make(Context ctx, String query,Boolean b, Response.Listener<String>
            listener, Response.ErrorListener errorListener) {
        try {
            String url=null;
            if(b) {
                  url = PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON + "?key=" +
                        API_KEY + "&components=country:in" + "&input=" +
                        URLEncoder.encode(query, "utf8");
            }else {
                url="https://maps.googleapis.com/maps/api/geocode/json?address="+
                        URLEncoder.encode(query, "utf8")+"&key="+API_KEY;
            }
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    listener, errorListener);
            ApiCall.getInstance(ctx).addToRequestQueue(stringRequest);
        }catch (Exception e){
            Log.e(TAG, "make: "+e.getMessage());
        }

    }

    private static final String TAG = "ApiCall";
}
