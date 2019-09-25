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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.crashlytics.android.Crashlytics;
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


                                try {
                                    JSONObject jsonObject = new JSONObject(carde);
                                    formatJson(jsonObject);
                                } catch (JSONException e) {
            Crashlytics.logException(e);
progressBar.dismiss();

                                    e.printStackTrace();
                                }
                            }

    }

    private void formatJson(JSONObject jsonObject) throws JSONException {

        if (jsonObject.has("carName") && !jsonObject.isNull("carName")) {


            String carId = jsonObject.getString("carId");
            String carName = jsonObject.getString("carName");
            String carNumber = jsonObject.getString("carNumber");
            String carModel = jsonObject.getString("carModel");
            String carColor = jsonObject.getString("carColor");
            String seatNumber = jsonObject.getString("seatNumber");
            String userId = jsonObject.getString("userId");
            final String carImage = jsonObject.getString("carImagePath");
            if(carImage!=null&&carImage.contains(".jpg")) {


                Glide.with(getApplicationContext())
                        .load(AppConstants.host+AppConstants.Car + carImage)
                        .thumbnail(0.5f)
                        .placeholder(R.drawable.caricon)
                        .fallback(R.drawable.caricon)
                        .error(R.drawable.caricon)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(carimageview);

            }

            if (jsonObject.has("insurance") && !jsonObject.isNull("insurance")) {


                JSONObject insurance = jsonObject.getJSONObject("insurance");
                String insuranceId = insurance.getString("insuranceId");
                String insuranceCompany = insurance.getString("insuranceCompany");
                String expiryDate = insurance.getString("expiryDate");
                expiryDate=expiryDate.split("T")[0];
                if(insuranceCompany.equals("null")){
                    insu=true;




                }

                if (jsonObject.has("driver") && !jsonObject.isNull("driver")) {


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

                                if(userPic!=null&&userPic.contains(".jpg")) {
                                    drierImage.setVisibility(View.VISIBLE);

                                    Glide.with(getApplicationContext())
                                            .load(AppConstants.host+AppConstants.Driver + userPic)
                                            .placeholder(R.drawable.caricon)

                                            .thumbnail(0.5f)
                                            .crossFade()
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .into(drierImage);

                                }

                        progressBar.dismiss();


                    }else {
                        progressBar.dismiss();

                        showalertforadd("please add driver  details.", Driver.class);

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
