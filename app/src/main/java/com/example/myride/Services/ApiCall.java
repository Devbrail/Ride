package com.example.myride.Services;
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
    private static final String API_KEY = "AIzaSyAhgOKhJsKlAvZ_2PCWgu-2ysY79fI54G4";

    private static ApiCall mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    public ApiCall(Context ctx) {
       this.mCtx = ctx;
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
                SharedPreferences sharedpreferences = ctx.getSharedPreferences(ctx.getPackageName(), Context.MODE_PRIVATE);
                String countrycode=sharedpreferences.getString("countrycode","ke");
                countrycode=countrycode.toLowerCase();
                Log.wtf("Hiii",countrycode);

                  url = PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON + "?key=" +
                        API_KEY + "&components=country:"+countrycode+ "&input=" +
                        URLEncoder.encode(query, "utf8");
            }else {
                url="https://maps.googleapis.com/maps/api/geocode/json?address="+
                        URLEncoder.encode(query, "utf8")+"&key="+API_KEY;
            }
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    listener, errorListener);
            ApiCall.getInstance(ctx).addToRequestQueue(stringRequest);
        }catch (Exception e){
            Log.wtf(TAG, "make: "+e.getMessage());
        }

    }

    private static final String TAG = "ApiCall";
}
