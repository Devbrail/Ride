package com.example.myride.Services;

import com.android.volley.VolleyError;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public interface ServicesCallListener {


    void onJSONObjectResponse(JSONObject jsonObject);



    void onErrorResponse(VolleyError error);

    void onStringResponse(String string);





}