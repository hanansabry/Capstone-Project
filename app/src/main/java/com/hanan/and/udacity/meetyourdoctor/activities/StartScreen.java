package com.hanan.and.udacity.meetyourdoctor.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.hanan.and.udacity.meetyourdoctor.R;

import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.ANONYMOUS;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.USER;

public class StartScreen extends AppCompatActivity {

    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_2);

        //check if the user is signed
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(
                getResources().getString(R.string.pref_file), 0);
        user = preferences.getString(USER, ANONYMOUS);
        if(!user.equals(ANONYMOUS)){
            finish();
            //start the main activity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void skipLogin(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openSignInActivity(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void openSignupActivity(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
}
