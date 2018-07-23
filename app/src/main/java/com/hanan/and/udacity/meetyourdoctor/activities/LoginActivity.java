package com.hanan.and.udacity.meetyourdoctor.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.hanan.and.udacity.meetyourdoctor.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.NOT_SIGNED;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.USER;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.displaySnackMessage;

public class LoginActivity extends AppCompatActivity {

    private boolean notSigned;
    private FirebaseAuth auth;
    private SharedPreferences sharedPreferences;
    @BindView(R.id.email_edittext)
    EditText emailEditText;
    @BindView(R.id.password_edittext)
    EditText passwordEditText;
    @BindView(R.id.login_scroll_view)
    ScrollView scrollViewLogin;
    @BindView(R.id.progress_bar_login)
    ProgressBar progressBarLogin;
    @BindView(R.id.signin_button)
    Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.sign_in));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //check if the activity starts from favourites screen
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
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    public void startMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.signup_button)
    public void onSignupClicked(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.signin_button)
    public void onSigninClicked(View view){
        progressBarLogin.setVisibility(View.VISIBLE);
        signInButton.setVisibility(View.GONE);

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        //check if mail is valid
        if(isEmailValid(email)){
            progressBarLogin.setVisibility(View.GONE);
            signInButton.setVisibility(View.VISIBLE);
            displaySnackMessage(scrollViewLogin, getString(R.string.not_valid_mail));
        }

        if(!isValidData(email, password)){
            progressBarLogin.setVisibility(View.GONE);
            signInButton.setVisibility(View.VISIBLE);
            displaySnackMessage(scrollViewLogin, getResources().getString(R.string.enter_mail_password));
            return;
        }

        //authenticate user
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBarLogin.setVisibility(View.GONE);
                signInButton.setVisibility(View.VISIBLE);
                if(task.isSuccessful()){
                    //save user in shared preferences
                    setUser(auth.getCurrentUser().getUid());
                    startMainActivity();
                }else{
                    displaySnackMessage(scrollViewLogin, getResources().getString(R.string.login_failed));
                }
            }
        });
    }

    public boolean isValidData(String email, String password){
        if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password)){
            return false;
        }else{
            return true;
        }
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void setUser(String userId){
        sharedPreferences = getApplicationContext().getSharedPreferences(
                getResources().getString(R.string.pref_file), 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER, userId);
        editor.apply();
    }
}
