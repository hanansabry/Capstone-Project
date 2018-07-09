package com.hanan.and.udacity.meetyourdoctor.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hanan.and.udacity.meetyourdoctor.R;
import com.hanan.and.udacity.meetyourdoctor.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.EDIT_PROFILE;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.USER;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.displaySnackMessage;

public class SignupActivity extends AppCompatActivity {

    @BindView(R.id.fullname_edittext)
    EditText nameEditText;
    @BindView(R.id.mail_edittext)
    EditText mailEditText;
    @BindView(R.id.mobile_edittext)
    EditText mobileEditText;
    @BindView(R.id.password_edittext)
    EditText passwordEditText;
    @BindView(R.id.gender_group)
    RadioGroup genderRadio;
    @BindView(R.id.signup_scrollview)
    ScrollView scrollView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private boolean signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //if the activity opened from edit_profile button, set the activity title to "Edit Profile"
        if(getIntent().getStringExtra(EDIT_PROFILE)!=null){
            getSupportActionBar().setTitle(getResources().getString(R.string.edit_profile));
            ((Button)findViewById(R.id.signup_button)).setText(getResources().getString(R.string.save_button));
        }else{
            getSupportActionBar().setTitle(getResources().getString(R.string.sign_up));
            signup = true;
        }

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.signup_button)
    public void onSignupClicked(View view){
        progressBar.setVisibility(View.VISIBLE);
        final String name = nameEditText.getText().toString().trim();
        final String email = mailEditText.getText().toString().trim();
        final String mobile = mobileEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString().trim();

        int selectedGenderId = genderRadio.getCheckedRadioButtonId();
        final String gender = ((RadioButton)findViewById(selectedGenderId)).getText().toString();

        //validate data
        if(!isValidData(name, email, mobile, password)){
            progressBar.setVisibility(View.INVISIBLE);
            displaySnackMessage(scrollView, "Please enter all fields!");
            return;
        }
        if(password.length()<6){
            progressBar.setVisibility(View.INVISIBLE);
            displaySnackMessage(scrollView, "Password is too short, enter minimum 6 characters");
            return;
        }

        //create new user
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.INVISIBLE);
                if(!task.isSuccessful()){
                    displaySnackMessage(scrollView, "Authentication Failed!");
                }else{
                    //create new user to save in the database
                    writeNewUser(auth.getUid(), name, email, mobile, password, gender);
                    displaySnackMessage(scrollView, "User is created successfully!");
                    startMainActivity();
                }
            }
        });
    }

    public boolean isValidData(String name, String mail, String mobile, String password){
        if(TextUtils.isEmpty(name)||
                TextUtils.isEmpty(mail)||
                TextUtils.isEmpty(mobile)||
                TextUtils.isEmpty(password)){
            return false;
        }else{
            return true;
        }
    }

    public void startMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void writeNewUser(String id, String name, String mail, String mobile, String password, String gender){
        //save in database
        User user = new User(name, mail, mobile, password, gender);
        databaseReference.child("users").child(id).setValue(user);

        //save in shared preferences
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(
                getResources().getString(R.string.pref_file), 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER, id);
        editor.apply();
    }
}
