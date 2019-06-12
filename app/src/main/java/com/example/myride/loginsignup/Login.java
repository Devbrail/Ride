package com.example.myride.loginsignup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.example.myride.Home;
import com.example.myride.R;
import com.example.myride.Services.NetworkServiceCall;
import com.example.myride.Services.ServicesCallListener;
import com.example.myride.Utils.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    EditText username,password;
    String name,pass;
    TextView singup,forgot;
    Button login;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        singup=findViewById(R.id.singup);
        login=findViewById(R.id.signin);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        progressBar=findViewById(R.id.progressdialog);
        forgot=findViewById(R.id.forgot);

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Forgot.class));
            }
        });


        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Signup.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    login.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);

                    name=username.getText().toString();
                    pass=password.getText().toString();

                    JSONObject logindetails=new JSONObject();
                    logindetails.put("userName",name);
                    logindetails.put("password",pass);
                    logindetails.put("rememberMe",true);


                    NetworkServiceCall serviceCall = new NetworkServiceCall(getApplicationContext(), false);
                    serviceCall.setOnServiceCallCompleteListener(new onServiceCallCompleteListene());

                    serviceCall.makeJSONObjectPostRequest(AppConstants.URL+AppConstants.USERLOGIN,  logindetails, Request.Priority.IMMEDIATE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });






    }

    private static final String TAG = "Login";
    private class onServiceCallCompleteListene implements ServicesCallListener {

        @Override
        public void onJSONObjectResponse(JSONObject jsonObject) {

            Log.d(TAG, "onJSONObjectResponse: ");


            try {
                if(jsonObject!=null) {


                    SharedPreferences sharedpreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("userid", jsonObject.getString("userId"));
                    editor.putString("phone", jsonObject.getString("userName"));
                    editor.putString("password", pass);

                    editor.apply();
                    Toast.makeText(Login.this, "Succes", Toast.LENGTH_SHORT).show();


                }else {
                    login.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Login.this, "Incorrect password", Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d(TAG, "onErrorResponse: ");
            Toast.makeText(Login.this, "Incorrect password", Toast.LENGTH_SHORT).show();

            login.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);


        }

        @Override
        public void onStringResponse(String string) {
            Log.d(TAG, "onStringResponse: ");

        }
    }
}
