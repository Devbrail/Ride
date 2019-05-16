package com.example.myride.basic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.example.myride.Home;
import com.example.myride.R;

public class Driver extends AppCompatActivity {

    ImageView profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        profile=findViewById(R.id.profile1);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

    }

    boolean privacychecked=false;
    public void onCheckboxClicked(View view) {

        privacychecked=((CheckBox) view).isChecked();

    }

    public void onContinueclicked(View view) {
        if(privacychecked){

        }else {
            Snackbar.make(view, "You have to agree the terms and conditions", Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    public void uploadImageClicked(View view) {

        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 0);

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
    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(getApplicationContext(), Home.class));
        finish();
        return true;
    }
}
