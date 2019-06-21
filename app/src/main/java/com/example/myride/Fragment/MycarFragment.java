package com.example.myride.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.example.myride.R;
import com.example.myride.model.CardetailsPOJO;

import java.util.ArrayList;
import java.util.Objects;

public class MycarFragment extends DialogFragment {

    public  MycarFragment(){};
    ArrayList<CardetailsPOJO> cardetailsPOJOArrayList;

    @SuppressLint("ValidFragment")
    public MycarFragment(ArrayList<CardetailsPOJO> cardetailsPOJOArrayList) {
        this.cardetailsPOJOArrayList= cardetailsPOJOArrayList;

    }

    EditText carRegno,carMakeyeaer,carModel,carColor,SeatingCapacity;
    ImageView imageViewRound;


    EditText company, expiry;

    EditText drivername, nin, lno, ldate, dob, email;

    RadioGroup gender;



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        Objects.requireNonNull(dialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // set "origin" to top left corner, so to speak
        dialog.getWindow().setGravity(Gravity.TOP);


        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.setContentView(R.layout.mycar_fragmentlayout);

        // ============  vehicle detals =========//

        imageViewRound=dialog.findViewById(R.id.carimageview);

        carRegno=dialog.findViewById(R.id.regno);
        carMakeyeaer=dialog.findViewById(R.id.carmake);
        carModel=dialog.findViewById(R.id.carmodel);
        carColor=dialog.findViewById(R.id.carcolor);
        SeatingCapacity=dialog.findViewById(R.id.capacity);

        // ============  vehicle detals =========//

// ============  Insurance details =========//

        company =  dialog.  findViewById(R.id.company);
        expiry = dialog.findViewById(R.id.expiry);


// ============  Insurance details =========//


// ============  Driver details =========//

        drivername =dialog. findViewById(R.id.drivername);
        nin = dialog.findViewById(R.id.natid);
        lno = dialog.findViewById(R.id.liscence);
        ldate =dialog. findViewById(R.id.liscenceExpiry);
        email = dialog.findViewById(R.id.email);
        gender = dialog.findViewById(R.id.gender);

        dob =dialog. findViewById(R.id.dob);

// ============  Driver details =========//





        carRegno.setText(cardetailsPOJOArrayList.get(0).getCarNumber());
        carModel.setText(cardetailsPOJOArrayList.get(0).getCarModel());
        carColor.setText(cardetailsPOJOArrayList.get(0).getCarColor());
        SeatingCapacity.setText(cardetailsPOJOArrayList.get(0).getSeatNumber());


        company.setText(cardetailsPOJOArrayList.get(0).getInsuranceCompany());
        expiry.setText(cardetailsPOJOArrayList.get(0).getExpiryDate());


        drivername.setText(cardetailsPOJOArrayList.get(0).getDriverName());
        nin.setText(cardetailsPOJOArrayList.get(0).getNin());
        lno.setText(cardetailsPOJOArrayList.get(0).getDrivngLicence());
        ldate.setText(cardetailsPOJOArrayList.get(0).getExpiryDate());
        email.setText(cardetailsPOJOArrayList.get(0).getEmail());
        dob.setText(cardetailsPOJOArrayList.get(0).getDob());




        return dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
