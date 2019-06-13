package com.example.myride.Services;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;



import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myride.R;
import com.example.myride.Utils.AppConstants;
import com.example.myride.Utils.AppUtil;
import com.example.myride.Utils.ConnectivityHelper;
import com.example.myride.Utils.NetworkConnectionState;
import com.example.myride.Utils.VolleyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
                public void onErrorResponse(VolleyError error) {

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
                public void retry( VolleyError error) throws VolleyError {

                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(jsonObjReq);
        } else {
            Toast.makeText(context, DEVICE_OFFLINE_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }
    public void makeJSONObejctPostRequestMultipart( final HashMap<String, String> postParam, final File imageFile,
                                                   final String filename,  final Request.Priority priority) {
        if (ConnectivityHelper.isConnectedToNetwork(context)) {
            // Tag used to cancel the request
            String tag_json_obj = "json_obj_req";
            if (isProgressDialogShow) {
                pDialog = new ProgressDialog(context);
                pDialog.setMessage("Loading...");
                pDialog.setCancelable(false);
                pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                pDialog.setIndeterminate(true);
                pDialog.setCanceledOnTouchOutside(false);
                if (isProgressDialogShow) {
                    pDialog.show();
                    //pDialog.setContentView(R.layout.my_progress);
                }
            }
            final String url="http://3.89.37.241:8080/api/Values/SaveInsurance";
            VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    if (isProgressDialogShow) {
                        pDialog.dismiss();
                    }
                    String resultResponse = new String(response.data);
                    try {
                        JSONObject result = new JSONObject(resultResponse);
                        //AppLog.d("MediaSent Response", result + "");
                        listener.onJSONObjectResponse(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                    error.printStackTrace();
                    if (isProgressDialogShow) {
                        pDialog.dismiss();
                    }
                    listener.onErrorResponse(error);
                }
            }) {
                @Override
                protected Map<String, String> getParams() {

                    return postParam;
                }

                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                     // file name could found file base or direct access from real path
                    // for now just get bitmap data from ImageView
                    params.put("file", new DataPart(filename, getFileFromVideo(imageFile), "image/jpeg"));


                    return params;
                }

                @Override
                public Priority getPriority() {
                    return priority;
                }
            };
            multipartRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 50000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 50000;
                }

                @Override
                public void retry( VolleyError error) throws VolleyError {

                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(multipartRequest);

          //`  AiassisApp.getInstance().addToRequestQueue(multipartRequest, tag_json_obj);

        } else {
            throw new RuntimeException(DEVICE_OFFLINE_MESSAGE);
        }
    }


    public static byte[] getFileFromVideo(File filevideo) {
        File file = new File(String.valueOf(filevideo));
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {
             e.printStackTrace();
        }
        return bytes;
    }
}
