package com.example.myride.Services;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyLog;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myride.Utils.AppConstants;
import com.example.myride.Utils.ConnectivityHelper;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class NetworkServiceCall {


    public Context context;
    private static final String TAG = NetworkServiceCall.class.getSimpleName();
    private ServicesCallListener listener;
    private boolean isProgressDialogShow;
    private ProgressDialog pDialog;

    private static final String DEVICE_OFFLINE_MESSAGE = "Please check your internet connection";

    public void setOnServiceCallCompleteListener(ServicesCallListener listener) {
        this.listener = listener;
    }

    public NetworkServiceCall(Context context, boolean isProgressDialogShow) {
        this.context = context;
        this.isProgressDialogShow = isProgressDialogShow;
    }

    public static boolean isOnline(Context context) {
        return true;
    }


    public void makeJSONObjectPostRequest(String url,JSONObject jsonObject, final Request.Priority priority) {


        if (ConnectivityHelper.isConnectedToNetwork(context)) {

            if (isProgressDialogShow) {
                pDialog = new ProgressDialog(context);
                pDialog.setMessage("Please wait...");
                pDialog.setCancelable(false);
                pDialog.setIndeterminate(true);
                pDialog.setCanceledOnTouchOutside(false);
                if (isProgressDialogShow) {
                    pDialog.show();
                    //pDialog.setContentView(R.layout.myprogress);
                }
            }

            final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                    new Response.Listener<JSONObject>() {





                        @Override
                        public void onResponse(JSONObject response) {
                            //AppLog.d(TAG, response.toString());
                            try {
                                if (isProgressDialogShow) {
                                    pDialog.dismiss();
                                }
                                    listener.onJSONObjectResponse(response);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(com.android.volley.error.VolleyError error) {

                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    //Toast.makeText(context, error.getMessage() + "", Toast.LENGTH_SHORT).show();
                    if (isProgressDialogShow) {
                        pDialog.dismiss();
                    }
                    listener.onErrorResponse(error);
                }
            });
            jsonObjReq.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 50000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 50000;
                }

                @Override
                public void retry( VolleyError error) throws com.android.volley.error.VolleyError {

                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(jsonObjReq);
        } else {
            Toast.makeText(context, DEVICE_OFFLINE_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }
public  void makeFileUplaod(Context context, File file){

    SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, AppConstants.URL,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("Response", response);
                 }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {



         }
    });
    smr.addStringParam("param string", " data text");
    smr.addFile("param file", file.getPath());
    RequestQueue mRequestQueue = Volley.newRequestQueue(context);
    mRequestQueue.add(smr);
    }



}
