package com.example.android.topmovies;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        Handler handler= new Handler();
//        handler.postDelayed(
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
//                        startActivity(mainIntent);
//                        finish();
//                    }//run
//                },3000);

    }//on create
}//class
