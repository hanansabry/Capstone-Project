package com.hanan.and.udacity.meetyourdoctor.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hanan.and.udacity.meetyourdoctor.R;

public class SplashScreen extends AppCompatActivity {

    private static final long SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, StartScreen.class);
                startActivity(i);

                // close this activity
//                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void openSignupActivity(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

}