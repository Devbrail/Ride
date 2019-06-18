package com.example.myride;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.example.myride.basic.Driver;
import com.example.myride.basic.Insurance;
import com.example.myride.basic.VehicleDetails;
import com.example.myride.model.CardetailsPOJO;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Mycar extends AppCompatActivity {
    EditText carRegno, carMakeyeaer, carModel, carColor, SeatingCapacity;
    ImageView imageViewRound;


    LinearLayout carlayout, driverlayout, insurancelayout;
    EditText company, expiry;

    EditText drivername, nin, lno, ldate, dob, email;

    RadioGroup gender;
    ArrayList<CardetailsPOJO> cardetailsPOJOArrayList = new ArrayList<>();

    boolean drive=false;
    boolean insu=false;
    boolean vehicle=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mycar_fragmentlayout);

        initView();

        Intent intent = getIntent();
        if (intent.hasExtra("cardetails")) ;

        {
            try {
                String carde = intent.getStringExtra("cardetails");

                JSONObject jsonObject = new JSONObject(carde);

                if (jsonObject.has("carName") && jsonObject.get("carName") != null) {


                    String carId = jsonObject.getString("carId");
                    String carName = jsonObject.getString("carName");
                    String carNumber = jsonObject.getString("carNumber");
                    String carModel = jsonObject.getString("carModel");
                    String carColor = jsonObject.getString("carColor");
                    String seatNumber = jsonObject.getString("seatNumber");
                    String userId = jsonObject.getString("userId");
                    String carImage = jsonObject.getString("carImage");

                    if (jsonObject.has("insurance") && !jsonObject.get("insurance") .equals( "null")) {


                        JSONObject insurance = jsonObject.getJSONObject("insurance");
                        String insuranceId = insurance.getString("insuranceId");
                        String insuranceCompany = insurance.getString("insuranceCompany");
                        String expiryDate = insurance.getString("expiryDate");
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
                            String userPic = driver.getString("userIamge");
                            String drivngLicence = driver.getString("drivngLicence");
                            String drivngLicenceExpiry = driver.getString("drivngLicenceExpiry");
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
                            }else {
                                showalertforadd("please add driver  details.", Insurance.class);

                            }

                        } else {
                            showalertforadd("please add driver  details.", Driver.class);
                        }

                    } else {


                        showalertforadd("please add insurance  details.", Insurance.class);
                    }

                } else {


                    showalertforadd("please add car  details.", VehicleDetails.class);


                }





            } catch (Exception e) {
                e.printStackTrace();
            }


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

        imageViewRound = findViewById(R.id.carimageview);
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

// ============  Driver details =========//
    }
}
