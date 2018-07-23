package com.hanan.and.udacity.meetyourdoctor.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hanan.and.udacity.meetyourdoctor.R;

import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.ANONYMOUS;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.PREV_STARTED;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.USER;

public class StartScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_2);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            finish();
            //start the main activity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        checkFirstTimeRunning();
    }

    public void checkFirstTimeRunning(){
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(getResources().getString(R.string.pref_file), 0);
        boolean previouslyStarted = prefs.getBoolean(PREV_STARTED, false);
        if(!previouslyStarted) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean(PREV_STARTED, Boolean.TRUE);
            edit.apply();
            //show main activity
        }else{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
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
