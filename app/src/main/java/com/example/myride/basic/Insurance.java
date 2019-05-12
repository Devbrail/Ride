package com.example.myride.basic;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myride.Home;
import com.example.myride.R;

import java.io.File;
import java.util.Objects;

public class Insurance extends AppCompatActivity {
    private static final int REQUEST_PERMISSIONS =123 ;
    ImageView choosePdf;

    TextView fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        choosePdf=findViewById(R.id.choosepdf);
        fileName=findViewById(R.id.fileName);
        choosePdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    startActivityForResult(
                            Intent.createChooser(intent, "Select PDF file"),
                            REQUEST_PERMISSIONS);
                } catch (android.content.ActivityNotFoundException ex) {
                    // Potentially direct the user to the Market with a Dialog
                    Log.wtf("Insurance",ex.getMessage());


                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_PERMISSIONS){
            assert data != null;
            Uri uri = data.getData();
            File myFile = new File(Objects.requireNonNull(uri).toString());


            fileName.setText(String.format("%s.pdf", myFile.getName()));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(getApplicationContext(), Home.class));
        finish();
        return true;
    }
}
