package com.example.myride.basic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.myride.Home;
import com.example.myride.R;
import com.example.myride.Services.ApiCall;
import com.example.myride.adpter.AutoSuggestAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Profilecreate extends AppCompatActivity {

    EditText fName,lName,nin,town,dob,email;

    String namestring,stringnin,stringgender,stringdob,stringemail,stringtown;
    Boolean privacychecked=false;
    Button continu;
    ImageView profile;
    private AutoCompleteTextView autoCompleteTextView;
    private AutoSuggestAdapter autoSuggestAdapter;
    private Handler handler;

    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    RadioGroup gender;
    private RadioButton radioSexButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilecreate);

         fName=findViewById(R.id.firstName);
        lName=findViewById(R.id.lastName);
        nin=findViewById(R.id.natid);
        town=findViewById(R.id.town);
        dob=findViewById(R.id.dob);
        email=findViewById(R.id.email);
        profile=findViewById(R.id.profile1);
        autoCompleteTextView =
                findViewById(R.id.town);
        gender=findViewById(R.id.gender);



        autoCompleteTextView.setThreshold(3);

        autoSuggestAdapter = new AutoSuggestAdapter(this,
                android.R.layout.simple_dropdown_item_1line);
        
        autoCompleteTextView.setAdapter(autoSuggestAdapter);
        


    profile.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto , 0);//one can be replaced with any action code
        }
    });
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(autoCompleteTextView.getText())) {
                        makeApiCall(autoCompleteTextView.getText().toString(),true);
                    }

                }
                return false;
            }
        });

    }

    private static final String TAG = "Profilecreate";

    private void makeApiCall(String text,Boolean b) {
        ApiCall.make(this, text,b, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
                List<String> stringList = new ArrayList<>();
                List<String> paceid=new ArrayList<>();
                List<String> result = new ArrayList<>();
                String value;
                String[] val;
                try {
                    JSONObject responseObject = new JSONObject(response);
                    JSONArray array = responseObject.getJSONArray("predictions");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject row = array.getJSONObject(i);
                        //String list = row.getString("description")+","+row.getString("place_id");
                        // val = list.split(",");
                        //result.add(val[1]);
                        stringList.add(row.getString("description"));

                        Log.d(TAG, "onResponse: "+row.getString("description"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //IMPORTANT: set data here and notify
                autoSuggestAdapter.setData(stringList);
                autoSuggestAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e(TAG, "onErrorResponse: "+error.getMessage() );
            }
        });
    }

    public void onCheckboxClicked(View view) {


        privacychecked=((CheckBox) view).isChecked();


    }

    public void onContinueclicked(View view) {

        int selectedId=gender.getCheckedRadioButtonId();
        radioSexButton=(RadioButton)findViewById(selectedId);
        namestring=fName.getText().toString()+" "+lName.getText().toString();
        stringnin=nin.getText().toString();
     //   stringgender=gender.getText().toString();
        stringtown=town.getText().toString();
        stringdob=town.getText().toString();
        stringemail=email.getText().toString();

        if(privacychecked){



            if(namestring!=null||stringnin!=null||stringgender!=null||stringdob!=null||stringtown!=null){

                SharedPreferences prefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                Set<String> set = new HashSet<String>();
                set.add(namestring);
                set.add(stringnin);
                set.add(stringgender);
                set.add(stringtown);
                set.add(stringdob);
                set.add(stringemail);
                editor.putStringSet("profile",set);
                editor.commit();

                Snackbar.make(view, "Profile created succesfully", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                startActivity(new Intent(getApplicationContext(), Home.class));

            }else {

                Snackbar.make(view, "Fields cannot be empty", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }

        }else {
            Snackbar.make(view, "You must agree the privacy policy", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){


                    try {
                        Uri selectedImage = null;
                        selectedImage = data.getData();

                        Bitmap bitmap =Bitmap.createScaledBitmap( MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage), 200,200, false);

profile.setImageBitmap(bitmap );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
//                    Uri selectedImage = imageReturnedIntent.getData();
//                    imageview.setImageURI(selectedImage);
                }
                break;
        }
    }

}
