package com.rider.myride.Services;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.rider.myride.Utils.AppConstants;
import com.rider.myride.Utils.ConnectivityHelper;
import com.rider.myride.Utils.MyJsonArrayRequest;
import com.rider.myride.Utils.VolleyMultipartRequest;

import org.json.JSONArray;
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


    private static final String TAG = NetworkServiceCall.class.getSimpleName();
    private static final String DEVICE_OFFLINE_MESSAGE = "Please check your internet connection";
    public Context context;
    private ServicesCallListener listener;
    private boolean isProgressDialogShow;
    private ProgressDialog pdialog;

    public NetworkServiceCall(Context context, boolean isProgressDialogShow) {
        this.context = context;
        this.isProgressDialogShow = isProgressDialogShow;
    }

     static byte[] getFileFromVideo(File filevideo) {
        File file = new File(String.valueOf(filevideo));
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(filevideo));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public void setOnServiceCallCompleteListener(ServicesCallListener listener) {
        this.listener = listener;
    }

    public void makeJSONObjectPostRequest(String url, final JSONObject jsonObject, final Request.Priority priority) {

        try {
            if (ConnectivityHelper.isConnectedToNetwork(context)) {

                if (isProgressDialogShow) {
                    pdialog = new ProgressDialog(context);
                    pdialog.setMessage("Please wait...");
                    pdialog.setCancelable(false);
                    pdialog.setIndeterminate(true);
                    pdialog.setCanceledOnTouchOutside(false);
                    if (isProgressDialogShow) {
                        pdialog.show();
                        //pdialog.setContentView(R.layout.myprogress);
                    }
                }

                Log.d(TAG, "makeJSONObjectPostRequest: "+jsonObject);
                final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                        new Response.Listener<JSONObject>() {


                            @Override
                            public void onResponse(JSONObject response) {
                                //AppLog.d(TAG, response.toString());
                                Log.d(TAG, "onResponse: "+response.toString());
                                try {
                                    if (isProgressDialogShow) {
                                        pdialog.dismiss();
                                        pdialog.dismiss();
                                    }
                                    listener.onJSONObjectResponse(response);
                                } catch (Exception e) {
                                    Crashlytics.logException(e);
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Crashlytics.logException(error);

                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        //Toast.makeText(context, error.getMessage() + "", Toast.LENGTH_SHORT).show();
                        if (isProgressDialogShow) {
                            pdialog.dismiss();
                        }
                        listener.onErrorResponse(error);
                    }
                });
                jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(jsonObjReq);
            } else {
                Toast.makeText(context, DEVICE_OFFLINE_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.d(TAG, e.getMessage());
        }
    }

    public void makeJSONObejctPostRequestMultipart(final HashMap<String, String> postParam, final File imageFile,
                                                   final String filename, final Request.Priority priority) {
        if (ConnectivityHelper.isConnectedToNetwork(context)) {
            // Tag used to cancel the request
            String tag_json_obj = "json_obj_req";
            if (isProgressDialogShow) {
                pdialog = new ProgressDialog(context);
                pdialog.setMessage("Loading...");
                pdialog.setCancelable(false);
                pdialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                pdialog.setIndeterminate(true);
                pdialog.setCanceledOnTouchOutside(false);
                if (isProgressDialogShow) {
                    pdialog.show();
                    //pdialog.setContentView(R.layout.my_progress);
                }
            }
            final String url = AppConstants.URL + "/SaveInsurance";
            VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    if (isProgressDialogShow) {
                        pdialog.dismiss();
                    }
                    String resultResponse = new String(response.data);
                    try {
                        JSONObject result = new JSONObject(resultResponse);
                        Log.d(TAG, "onResponse: "+result.toString());

                        //AppLog.d("MediaSent Response", result + "");
                        listener.onJSONObjectResponse(result);
                    } catch (JSONException e) {
                        Crashlytics.logException(e);
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Crashlytics.logException(error);
                    error.printStackTrace();
                    if (isProgressDialogShow) {
                        pdialog.dismiss();
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
                    params.put("file", new DataPart(filename, getFileFromVideo(imageFile), "application/pdf"));


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
                public void retry(VolleyError error) throws VolleyError {

                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(multipartRequest);

            //`  AiassisApp.getInstance().addToRequestQueue(multipartRequest, tag_json_obj);

        } else {
            throw new RuntimeException(DEVICE_OFFLINE_MESSAGE);
        }
    }

    public void makeGetrequest(String url) {

        Log.d(TAG, "makeGetrequest: "+url);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d(TAG, response.toString());
                        listener.onJSONObjectResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Crashlytics.logException(error);
                        Log.d("Error.Response", error.getMessage());
                        listener.onErrorResponse(error);
                    }
                }
        );

        getRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        RequestQueue queue = Volley.newRequestQueue(context);


// add it to the RequestQueue
        queue.add(getRequest);
    }

    public void makeJSONObjectGetRequest(String url, JSONObject jsonObject, final Request.Priority priority) {

        if (ConnectivityHelper.isConnectedToNetwork(context)) {
            // Tag used to cancel the request
            final String tag_json_obj = "json_obj_req";
            if (isProgressDialogShow) {
                pdialog = new ProgressDialog(context);
                pdialog.setMessage("Loading...");
                pdialog.setCancelable(false);
                pdialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                pdialog.setIndeterminate(true);
                pdialog.setCanceledOnTouchOutside(false);
                if (isProgressDialogShow) {
                    pdialog.show();
                    // pdialog.setContentView(R.layout.myprogress);
                }
            }

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //AppLog.d(TAG, response.toString());
                        if (isProgressDialogShow) {
                            pdialog.dismiss();
                        }

                        listener.onJSONObjectResponse(response);

                    } catch (Exception e) {
                        Crashlytics.logException(e);
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Crashlytics.logException(error);
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    // hide the progress dialog
                    if (isProgressDialogShow) {
                        pdialog.dismiss();
                    }
                    listener.onErrorResponse(error);
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(jsonObjReq);
        } else {
//            throw new RuntimeException(DEVICE_OFFLINE_MESSAGE);
            Toast.makeText(context, DEVICE_OFFLINE_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }


    public void makearrayrequest(String url) {


        MyJsonArrayRequest request = new MyJsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {


                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String carId = jsonObject.getString("carId");
                        String carName = jsonObject.getString("carName");
                        String carNumber = jsonObject.getString("carNumber");
                        String carModel = jsonObject.getString("carModel");
                        String carColor = jsonObject.getString("carColor");
                        String seatNumber = jsonObject.getString("seatNumber");
                        String userId = jsonObject.getString("userId");
                        String carImage = jsonObject.getString("carImage");
                        JSONObject insurance = jsonObject.getJSONObject("insurance");
                        String insuranceId = jsonObject.getString("insuranceId");
                        String insuranceCompany = jsonObject.getString("insuranceCompany");
                        String expiryDate = jsonObject.getString("expiryDate");
                        JSONObject driver = jsonObject.getJSONObject("driver");
                        String driverId = jsonObject.getString("driverId");
                        String driverName = jsonObject.getString("driverName");
                        String nin = jsonObject.getString("nin");
                        String gender = jsonObject.getString("gender");
                        String email = jsonObject.getString("email");
                        String dob = jsonObject.getString("dob");
                        String userPic = jsonObject.getString("userPic");
                        String drivngLicence = jsonObject.getString("drivngLicence");
                        String drivngLicenceExpiry = jsonObject.getString("drivngLicenceExpiry");


                    }


                } catch (Exception e) {
                    Crashlytics.logException(e);
                    e.printStackTrace();
                    Log.d(TAG, "onResponse: " + e.getMessage());
                }

                Log.d("onResponse", "" + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Crashlytics.logException(error);
                error.printStackTrace();
                Log.d("onErrorResponse", "Error");
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        requestQueue.add(request);
    }
}
