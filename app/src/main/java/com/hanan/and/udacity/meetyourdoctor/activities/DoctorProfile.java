package com.hanan.and.udacity.meetyourdoctor.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanan.and.udacity.meetyourdoctor.R;
import com.hanan.and.udacity.meetyourdoctor.model.Doctor;
import com.hanan.and.udacity.meetyourdoctor.utilities.FloatingActionImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.DOCTOR;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.DOCTORS_NODE;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.TRUE;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.USER;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.USERS;

public class DoctorProfile extends AppCompatActivity {
    private Doctor doctor;
    @BindView(R.id.doctor_name)
    TextView doctorName;
    @BindView(R.id.doctor_specialist)
    TextView doctorSpecialist;
    @BindView(R.id.fees_tv)
    TextView fees;
    @BindView(R.id.address_tv)
    TextView address;
    @BindView(R.id.days_tv)
    TextView days;
    @BindView(R.id.times_tv)
    TextView times;
    @BindView(R.id.rating)
    RatingBar ratingBar;
    @BindView(R.id.doctor_profile_image)
    FloatingActionImageView doctorProfileImage;
    @BindView(R.id.favourite_button)
    ImageButton favouriteBtn;
    List<String> phones;

    private DatabaseReference database;
    private FirebaseUser currentUser;
    private boolean isFavourite;
    private ValueEventListener favouriteDoctorsEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference().child(USERS).child(currentUser.getUid());
        if (currentUser != null) {
            favouriteBtn.setVisibility(View.VISIBLE);
            isDoctorFavourite();
        } else {
            favouriteBtn.setVisibility(View.GONE);
        }

        //get doctor object from activity intent
        if (getIntent().getParcelableExtra(DOCTOR) != null) {
            doctor = getIntent().getParcelableExtra(DOCTOR);
        }
        initCollapsingToolbar(doctor.getName());
        if (doctor.getGender().equalsIgnoreCase(getResources().getString(R.string.female))) {
            doctorProfileImage.setImageDrawable(getResources().getDrawable(R.drawable.doctor_female_profile));
        } else {
            doctorProfileImage.setImageDrawable(getResources().getDrawable(R.drawable.doctor_male_profile));
        }

        doctorName.setText(doctor.getName());
        doctorSpecialist.setText(doctor.getSpecialist().getName());
        fees.setText(doctor.getFees());
        address.setText(doctor.getAddress());
        days.setText(doctor.getDays());
        times.setText(doctor.getTimes());
        ratingBar.setRating(doctor.getRating());
        phones = doctor.getPhones();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    private void initCollapsingToolbar(final String doctorName) {
        final CollapsingToolbarLayout collapsingToolbar =
                findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(doctorName);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    public void openPatientsReviewsActivity(View view) {
        Intent intent = new Intent(this, ReviewsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }

    @OnClick(R.id.call_button)
    public void onCallClicked(View view) {
        final String[] selectedPhone = new String[1];
        selectedPhone[0] = phones.get(0);
        new MaterialDialog.Builder(this)
                .title(getResources().getString(R.string.clinic_phones))
                .items(phones)
                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        selectedPhone[0] = phones.get(which);
                        return true;
                    }
                })
                .alwaysCallSingleChoiceCallback()
                .positiveText(R.string.call_now_str)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + selectedPhone[0]));
                        startActivity(intent);

                    }
                })
                .show();
    }

    @OnClick(R.id.favourite_button)
    public void onFavouriteClicked(View view) {
        if (isFavourite) {
            //delete from favourites
            Toast.makeText(this, "Deleted from favourites", Toast.LENGTH_SHORT).show();
            isFavourite = false;
            favouriteBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_favourite_border));
            database.child(DOCTORS_NODE).child(doctor.getId()).removeValue();
        } else {
            //add to favourites
            Toast.makeText(this, "Added to favourites", Toast.LENGTH_SHORT).show();
            isFavourite = true;
            favouriteBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_favourite_fill));
            //add current doctor as favourite to current user
            database.child(DOCTORS_NODE).child(doctor.getId()).setValue(TRUE);
        }
    }

    public void isDoctorFavourite() {
//        database = database.child(DOCTORS_NODE);
        favouriteDoctorsEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot favDoctor : dataSnapshot.child(DOCTORS_NODE).getChildren()) {
                    if (favDoctor.getKey().equals(doctor.getId())) {
                        favouriteBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_favourite_fill));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        database.addListenerForSingleValueEvent(favouriteDoctorsEventListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (favouriteDoctorsEventListener != null) {
            database.removeEventListener(favouriteDoctorsEventListener);
        }
    }
}
