package com.rider.myride.Services;


import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface ServicesCallListener {


    void onJSONObjectResponse(JSONObject jsonObject);


    void onErrorResponse(VolleyError error);

    void onStringResponse(String string);


}