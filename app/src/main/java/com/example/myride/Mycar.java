package com.example.myride;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.example.myride.Utils.AppConstants;
import com.example.myride.Utils.AppUtil;
import com.example.myride.basic.Driver;
import com.example.myride.basic.Insurance;
import com.example.myride.basic.VehicleDetails;
import com.example.myride.model.CardetailsPOJO;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Mycar extends AppCompatActivity {
    EditText carRegno, carMakeyeaer, carModel, carColor, SeatingCapacity;
    ImageView carimageview,drierImage;


    LinearLayout carlayout, driverlayout, insurancelayout;
    EditText company, expiry;

    EditText drivername, nin, lno, ldate, dob, email;

    RadioGroup gender;
    ArrayList<CardetailsPOJO> cardetailsPOJOArrayList = new ArrayList<>();

    boolean drive=false;
    boolean insu=false;
    boolean vehicle=true;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mycar_fragmentlayout);

        initView();


        progressBar = new ProgressDialog(Mycar.this,R.style.Theme_MaterialComponents_Dialog);
        progressBar.setCancelable(false);

        progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//you can cancel it by pressing back button
//        progressBar.setMessage("Please wait ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progressBar.show();
        final Intent intent = getIntent();
        if (intent.hasExtra("cardetails")) ;

        {

                final String carde = intent.getStringExtra("cardetails");



                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject jsonObject = new JSONObject(carde);
                                    formatJson(jsonObject);
                                } catch (JSONException e) {
                                    progressBar.dismiss();

                                    e.printStackTrace();
                                }
                            }
                        });

                    }
                },500);







        }
    }

    private void formatJson(JSONObject jsonObject) throws JSONException {

        if (jsonObject.has("carName") && jsonObject.get("carName") != null) {


            String carId = jsonObject.getString("carId");
            String carName = jsonObject.getString("carName");
            String carNumber = jsonObject.getString("carNumber");
            String carModel = jsonObject.getString("carModel");
            String carColor = jsonObject.getString("carColor");
            String seatNumber = jsonObject.getString("seatNumber");
            String userId = jsonObject.getString("userId");
            final String carImage = jsonObject.getString("carImage");

            if (jsonObject.has("insurance") && !jsonObject.get("insurance") .equals( "null")) {


                JSONObject insurance = jsonObject.getJSONObject("insurance");
                String insuranceId = insurance.getString("insuranceId");
                String insuranceCompany = insurance.getString("insuranceCompany");
                String expiryDate = insurance.getString("expiryDate");
                expiryDate=expiryDate.split("T")[0];
                if(insuranceCompany.equals("null")){
                    insu=true;




                }

                if (jsonObject.has("driver") && !jsonObject.get("driver").equals(null)) {


                    JSONObject driver = jsonObject.getJSONObject("driver");


                    String driverId = driver.getString("driverId");
                    String driverName = driver.getString("firstName") + " " + driver.getString("lastName");
                    String nin = driver.getString("nin");
                    String gender = driver.getString("gender");
                    String email = driver.getString("email");
                    String dob = driver.getString("dob");
                    dob=dob.split("T")[0];
                    final String userPic = driver.getString("userIamge");
                    String drivngLicence = driver.getString("drivngLicence");
                    String drivngLicenceExpiry = driver.getString("drivngLicenceExpiry");
                    drivngLicenceExpiry = drivngLicenceExpiry.split("T")[0];
                    if(gender.equals("null")){
                        drive=true;



                    }


                    CardetailsPOJO cardetailsPOJO = new CardetailsPOJO(carId, carName, carNumber,
                            carModel, carColor, seatNumber, userId, carImage, insuranceId, insuranceCompany, expiryDate, driverId,
                            driverName, nin, gender, email, dob, userPic, drivngLicence, drivngLicenceExpiry);


                    cardetailsPOJOArrayList.add(cardetailsPOJO);
                    carRegno.setText(cardetailsPOJOArrayList.get(0).getCarNumber());
                    this.carModel.setText(cardetailsPOJOArrayList.get(0).getCarModel());
                    this.carColor.setText(cardetailsPOJOArrayList.get(0).getCarColor());
                    SeatingCapacity.setText(cardetailsPOJOArrayList.get(0).getSeatNumber());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);

                            if(carImage!=null&&carImage.contains(".jpg")) {
                                Bitmap bmp = AppUtil.getbmpfromURL(AppConstants.host+AppConstants.Car + carImage);
                                if(bmp!=null) {
                                    carimageview.setVisibility(View.VISIBLE);

                                    carimageview.setImageBitmap(bmp);
                                }

                            }

                        }
                    });

                    if(!insu) {
                        company.setText(cardetailsPOJOArrayList.get(0).getInsuranceCompany());
                        expiry.setText(cardetailsPOJOArrayList.get(0).getExpiryDate());
                    }else {
                        showalertforadd("please add insurance  details.", Insurance.class);
                    }



                    if(!drive) {
                        drivername.setText(cardetailsPOJOArrayList.get(0).getDriverName());
                        this.nin.setText(cardetailsPOJOArrayList.get(0).getNin());
                        lno.setText(cardetailsPOJOArrayList.get(0).getDrivngLicence());
                        ldate.setText(cardetailsPOJOArrayList.get(0).getExpiryDate());
                        this.email.setText(cardetailsPOJOArrayList.get(0).getEmail());
                        this.dob.setText(cardetailsPOJOArrayList.get(0).getDob());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                StrictMode.setThreadPolicy(policy);

                                if(userPic!=null&&userPic.contains(".jpg")) {
                                    Bitmap bmp = AppUtil.getbmpfromURL(AppConstants.host+AppConstants.Driver + userPic);
                                    if(bmp!=null) {
                                        drierImage.setVisibility(View.VISIBLE);

                                        drierImage.setImageBitmap(bmp);
                                    }

                                }

                            }
                        });
                        progressBar.dismiss();


                    }else {
                        progressBar.dismiss();

                        showalertforadd("please add driver  details.", Insurance.class);

                    }

                } else {
                    progressBar.dismiss();

                    showalertforadd("please add driver  details.", Driver.class);
                }

            } else {
                progressBar.dismiss();


                showalertforadd("please add insurance  details.", Insurance.class);
            }

        } else {

            progressBar.dismiss();

            showalertforadd("please add car  details.", VehicleDetails.class);


        }


    }

    private void showalertforadd(String s, final Class clas) {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this, R.style.Theme_MaterialComponents_Light_Dialog_Alert);

        builder.setMessage(s);
        builder.setCancelable(false);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                startActivity(new Intent(getApplicationContext(),clas));




            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        builder.show();

    }

    private void initView() {
        // ============  vehicle detals =========//

        carimageview = findViewById(R.id.carimageview);
        carlayout = findViewById(R.id.carlayout);

        carRegno = findViewById(R.id.regno);
        carMakeyeaer = findViewById(R.id.carmake);
        carModel = findViewById(R.id.carmodel);
        carColor = findViewById(R.id.carcolor);
        SeatingCapacity = findViewById(R.id.capacity);

        // ============  vehicle detals =========//

// ============  Insurance details =========//

        insurancelayout = findViewById(R.id.insurancelayout);
        company = findViewById(R.id.company);
        expiry = findViewById(R.id.expiry);


// ============  Insurance details =========//


// ============  Driver details =========//

        drivername = findViewById(R.id.drivername);
        driverlayout = findViewById(R.id.driverlayout);
        nin = findViewById(R.id.natid);
        lno = findViewById(R.id.liscence);
        ldate = findViewById(R.id.liscenceExpiry);
        email = findViewById(R.id.email);
        gender = findViewById(R.id.gender);

        dob = findViewById(R.id.dob);
        drierImage = findViewById(R.id.drierImage);

// ============  Driver details =========//
    }
}
