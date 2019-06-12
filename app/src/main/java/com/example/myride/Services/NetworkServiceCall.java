package com.example.myride.Services;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myride.Utils.ConnectivityHelper;

import org.json.JSONObject;

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

    public void makeJSONObjectGetRequest( final Request.Priority priority) {

        if (ConnectivityHelper.isConnectedToNetwork(context)) {
            // Tag used to cancel the request
            final String tag_json_obj = "json_obj_req";
            if (isProgressDialogShow) {
                pDialog = new ProgressDialog(context);
                pDialog.setMessage("Loading...");
                pDialog.setCancelable(false);
                pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                pDialog.setIndeterminate(true);
                pDialog.setCanceledOnTouchOutside(false);
                if (isProgressDialogShow) {
                    pDialog.show();
                    //pDialog.setContentView(R.layout.myprogress);
                }
            }
String url="";
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //AppLog.d(TAG, response.toString());
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
                public void onErrorResponse(VolleyError error) {

                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    // hide the progress dialog
                    if (isProgressDialogShow) {
                        pDialog.dismiss();
                    }
                    listener.onErrorResponse(error);
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(jsonObjReq);

            // Adding request to request queue

        } else {
//            throw new RuntimeException(DEVICE_OFFLINE_MESSAGE);
            Toast.makeText(context, DEVICE_OFFLINE_MESSAGE, Toast.LENGTH_SHORT).show();
        }
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
                public void onErrorResponse(VolleyError error) {

                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    //Toast.makeText(context, error.getMessage() + "", Toast.LENGTH_SHORT).show();
                    if (isProgressDialogShow) {
                        pDialog.dismiss();
                    }
                    listener.onErrorResponse(error);
                }
            });


            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(jsonObjReq);
        } else {
            Toast.makeText(context, DEVICE_OFFLINE_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }



    public void makeJSONStringGetRequest(String url, final Request.Priority priority) {

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
            final StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


                @Override
                public void onResponse(String response) {
                    try {
                        if (isProgressDialogShow) {
                            pDialog.dismiss();
                        }
                        listener.onStringResponse(response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    // hide the progress dialog
                    if (isProgressDialogShow) {
                        pDialog.dismiss();
                    }

                    listener.onErrorResponse(error);
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(request);
//        } else {
//            Toast.makeText(context, "Please chk your internet", Toast.LENGTH_SHORT).show();
//        }
        } else {
            throw new RuntimeException(DEVICE_OFFLINE_MESSAGE);
        }
    }


}
