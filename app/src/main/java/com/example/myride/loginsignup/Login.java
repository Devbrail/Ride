package com.example.myride.loginsignup;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myride.Home;
import com.example.myride.R;

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

                login.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                name=username.getText().toString();
                pass=password.getText().toString();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(getApplicationContext(), Home.class));
                    }
                },3000);


            }
        });



    }
}
