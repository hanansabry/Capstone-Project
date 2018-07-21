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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanan.and.udacity.meetyourdoctor.R;
import com.hanan.and.udacity.meetyourdoctor.model.User;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.EDIT_PROFILE;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.USER;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.USERS;
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
    @BindView(R.id.signup_button)
    Button signupButton;
    @BindView(R.id.male_radio)
    RadioButton maleRadio;
    @BindView(R.id.female_radio)
    RadioButton femaleRadio;

    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private boolean signup;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference(USERS);

        //if the activity opened from edit_profile button, set the activity title to "Edit Profile"
        currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.sign_up));
            signup = true;
        } else {
            getSupportActionBar().setTitle(getResources().getString(R.string.edit_profile));
            signupButton.setText(getResources().getString(R.string.save_button));
            //get user data
            getUserData();
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

    @OnClick(R.id.signup_button)
    public void onSignupClicked(View view) {
        progressBar.setVisibility(View.VISIBLE);
        final String name = nameEditText.getText().toString().trim();
        final String email = mailEditText.getText().toString().trim();
        final String mobile = mobileEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString().trim();

        int selectedGenderId = genderRadio.getCheckedRadioButtonId();
        final String gender = ((RadioButton) findViewById(selectedGenderId)).getText().toString();

        //validate data
        if (!isValidData(name, email, mobile, password)) {
            progressBar.setVisibility(View.INVISIBLE);
            displaySnackMessage(scrollView, getResources().getString(R.string.enter_all_fields));
            return;
        }
        if (password.length() < 6) {
            progressBar.setVisibility(View.INVISIBLE);
            displaySnackMessage(scrollView, getResources().getString(R.string.password_length));
            return;
        }

        if (currentUser == null) {
            //create new user
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.INVISIBLE);
                    if (!task.isSuccessful()) {
                        displaySnackMessage(scrollView, getResources().getString(R.string.authentication_failed));
                    } else {
                        //create new user to save in the database
                        writeNewUser(auth.getUid(), name, email, mobile, password, gender);
                    }
                }
            });
        } else {
            updateUser(name, mobile, password, gender);
        }
    }

    public boolean isValidData(String name, String mail, String mobile, String password) {
        if (TextUtils.isEmpty(name) ||
                TextUtils.isEmpty(mail) ||
                TextUtils.isEmpty(mobile) ||
                TextUtils.isEmpty(password)) {
            return false;
        } else {
            return true;
        }
    }

    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void writeNewUser(String id, String name, String mail, String mobile, String password, String gender) {
        //save in database
        User user = new User(name, mail, mobile, password, gender);
        databaseReference.child(id).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                displaySnackMessage(scrollView, getResources().getString(R.string.create_user));
                startMainActivity();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                displaySnackMessage(scrollView, getResources().getString(R.string.create_user_failed));
            }
        });

        //save in shared preferences
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(
                getResources().getString(R.string.pref_file), 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER, id);
        editor.apply();
    }

    public void updateUser(String name, String mobile, final String password, String gender) {
        User user = new User(name, currentUser.getEmail(), mobile, password, gender);
        Map<String, Object>  children= new HashMap<>();
        children.put("name", name);
        children.put("gender", gender);
        children.put("mobileNumber", mobile);
        children.put("password", password);

        databaseReference.child(currentUser.getUid()).updateChildren(children).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
//                Toast.makeText(SignupActivity.this, "Data updated successfully", Toast.LENGTH_SHORT).show();
                displaySnackMessage(scrollView, getResources().getString(R.string.update_user));
                //update password in firebase auth
                currentUser.updatePassword(password);
                progressBar.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(SignupActivity.this, "Failed to update data, please try again!", Toast.LENGTH_SHORT).show();
                displaySnackMessage(scrollView, getResources().getString(R.string.update_user_failed));
            }
        });
    }

    public void getUserData() {
        final String uid = currentUser.getUid();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.child(uid).getValue(User.class);
                nameEditText.setText(user.getName());
                mobileEditText.setText(user.getMobileNumber());
                mailEditText.setText(user.getEmail());
                mailEditText.setEnabled(false);
                passwordEditText.setText(user.getPassword());
                String gender = user.getGender();
                if (gender.equals(getResources().getString(R.string.male))) {
                    genderRadio.check(R.id.male_radio);
                } else {
                    genderRadio.check(R.id.female_radio);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
