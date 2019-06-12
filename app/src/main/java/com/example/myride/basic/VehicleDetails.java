package com.example.myride.basic;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.example.myride.Home;
import com.example.myride.R;
import com.example.myride.Services.NetworkServiceCall;
import com.example.myride.Services.ServicesCallListener;
import com.example.myride.Utils.AppConstants;
import com.example.myride.Utils.AppUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class VehicleDetails extends AppCompatActivity {

    private static final int MEDIA_TYPE_IMAGE = 121;
    private static final int TAKE_PICTURE = 123;
    private static final int SELECT_PICTURE = 122;
    private static final String IMAGE_DIRECTORY_NAME = "MyRide";
ImageView imageViewRound;
    private String imgPath;
EditText carRegno,carMakeyeaer,carModel,carColor,SeatingCapacity;
String carNo,caryr,CarModel,carcolor,carCapacity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicledetails);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
        imageViewRound=findViewById(R.id.carimageview);
        imageViewRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        carRegno=findViewById(R.id.regno);
        carMakeyeaer=findViewById(R.id.carmake);
        carModel=findViewById(R.id.carmodel);
        carColor=findViewById(R.id.carcolor);
        SeatingCapacity=findViewById(R.id.capacity);






    }

    Uri fileUri;
    private void selectImage() {

        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        TextView title = new TextView(this);
        title.setText("Add Photo!");
        title.setBackgroundColor(Color.BLACK);
        title.setPadding(10, 15, 15, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(22);


        AlertDialog.Builder builder = new AlertDialog.Builder(
                VehicleDetails.this);



        builder.setCustomTitle(title);

        // builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    // Intent intent = new
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, TAKE_PICTURE);

                } else if (items[item].equals("Choose from Library")) {

                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto,SELECT_PICTURE );
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    Bitmap bitmap;
    File destination;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //inputStreamImg = null;
        if (requestCode == TAKE_PICTURE) {
            try {
                Uri selectedImage = data.getData();
                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

                Log.e("Activity", "Pick from Camera::>>> ");

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                destination = new File(Environment.getExternalStorageDirectory() + "/" +
                        getString(R.string.app_name), "IMG_" + timeStamp + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                imgPath = destination.getAbsolutePath();
                imageViewRound.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == SELECT_PICTURE) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                Log.e("Activity", "Pick from Gallery::>>> ");

                imgPath = getRealPathFromURI(selectedImage);
                destination = new File(imgPath.toString());
                imageViewRound.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(getApplicationContext(), Home.class));
        finish();
        return true;
    }

    public void onContinueclicked(View view) {


        carNo= carRegno.getText().toString();
        caryr= carMakeyeaer.getText().toString();
        CarModel= carModel.getText().toString();
        carcolor= carColor.getText().toString();
        carCapacity= SeatingCapacity.getText().toString();

        if(validateTextfield()){


            if(bitmap!=null){
                try {
                    String userid= AppUtil.getuserid(getApplicationContext());

                    v=view;
                    vehicledetails=new JSONObject();
                    vehicledetails.put("carName",CarModel);
                    vehicledetails.put("carNumber",carNo);
                    vehicledetails.put("carModel",CarModel);
                    vehicledetails.put("carColor",carcolor);
                    vehicledetails.put("seatNumber",carCapacity);
                    vehicledetails.put("userId",userid);
                    vehicledetails.put("carImage",AppUtil.converttoBase64(imageViewRound));


                    NetworkServiceCall serviceCall = new NetworkServiceCall(getApplicationContext(), false);
                    serviceCall.setOnServiceCallCompleteListener(new onServiceCallCompleteListene());
                    serviceCall.makeJSONObjectPostRequest( AppConstants.URL+AppConstants.PROFILE_CREATE,vehicledetails, Request.Priority.IMMEDIATE);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }else {
                showSnackbar("Please attach car image",view);
            }


        }


    }
    JSONObject vehicledetails;
    View v;
    private void showSnackbar(String s, View view) {

        Snackbar.make(view, s, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }



    private class onServiceCallCompleteListene implements ServicesCallListener {

        @Override
        public void onJSONObjectResponse(JSONObject jsonObject) {
            try {
                Log.d(TAG, "onJSONObjectResponse: ");



                if(jsonObject.getBoolean("saveStatus")) {



                    startActivity(new Intent(getApplicationContext(), Insurance.class));
                }

                else
                    showSnackbar("Something went occured! please try again",v);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onErrorResponse(VolleyError error) {

            Log.e(TAG, "onErrorResponse: ");
        }

        @Override
        public void onStringResponse(String string) {
            Log.d(TAG, "onStringResponse: ");
        }
    }

    private static final String TAG = "VehicleDetails";
    private boolean validateTextfield() {

        if(carNo.isEmpty())
        {
            carRegno.setError("Cannot be empty");
            return false;
        }
        else if (CarModel.isEmpty())
        {
            carModel.setError("Cannot be empty");
            return false;
        }
        else if(caryr.isEmpty())
        {
            carMakeyeaer.setError("Cannot be empty");
            return false;
        }
        else if(carcolor.isEmpty())
        {
            carColor.setError("Cannot be empty");
            return false;
        }
        else if(carCapacity.isEmpty())
        {
            SeatingCapacity.setError("Cannot be empty");
            return false;
        }
        else return true;



    }
}
