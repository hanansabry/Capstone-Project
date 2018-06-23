package com.hanan.and.udacity.meetyourdoctor.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.hanan.and.udacity.meetyourdoctor.R;

import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.NOT_SIGNED;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.SIGNIN;

public class LoginActivity extends AppCompatActivity {

    private boolean notSigned;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.sign_in));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //check if the activity starts from favourties screen
        if (getIntent().getExtras() != null) {
            notSigned = getIntent().getBooleanExtra(NOT_SIGNED, false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (notSigned) {
            finish();
            startMainActivity();
        } else {
            super.onBackPressed();
        }
    }

    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openSignupActivity(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
}
