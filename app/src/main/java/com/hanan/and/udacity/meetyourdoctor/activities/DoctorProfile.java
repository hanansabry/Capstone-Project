package com.hanan.and.udacity.meetyourdoctor.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.hanan.and.udacity.meetyourdoctor.model.Review;
import com.hanan.and.udacity.meetyourdoctor.utilities.FloatingActionImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.DOCTOR;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.DOCTORS_NODE;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.REVIEWS_NODE;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.TRUE;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.USERS;

public class DoctorProfile extends AppCompatActivity {
    @BindView(R.id.doctor_name)
    TextView doctorName;
    @BindView(R.id.doctor_study)
    TextView doctorStudy;
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
    @BindView(R.id.about_doctor_tv)
    TextView aboutDoctor;
    @BindView(R.id.services_tv)
    TextView servicesTv;
    @BindView(R.id.doctor_profile_image)
    FloatingActionImageView doctorProfileImage;
    List<String> phones;
    //    @BindView(R.id.favourite_button)
//    ImageButton favouriteBtn;
    private Doctor doctor;
    private FirebaseDatabase database;
    private DatabaseReference userRef;
    private DatabaseReference reviewRef;
    private FirebaseUser currentUser;
    private boolean isFavourite;
    private ValueEventListener favouriteDoctorsEventListener;
    private ValueEventListener reviewsEventListener;
    private float reviewsValue = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        database = FirebaseDatabase.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            isDoctorFavourite();
        }
        invalidateOptionsMenu();

        //get doctor object from activity intent
        Intent intent = getIntent();
        if (intent != null && intent.getParcelableExtra(DOCTOR) != null) {
            doctor = getIntent().getParcelableExtra(DOCTOR);
        }
        initCollapsingToolbar(doctor.getName());
        if (doctor.getGender().equalsIgnoreCase(getResources().getString(R.string.female))) {
            doctorProfileImage.setImageDrawable(getResources().getDrawable(R.drawable.doctor_female_profile));
        } else {
            doctorProfileImage.setImageDrawable(getResources().getDrawable(R.drawable.doctor_male_profile));
        }

        doctorName.setText(doctor.getName());
        doctorStudy.setText(doctor.getStudy());
        fees.setText(doctor.getFees());
        address.setText(doctor.getAddress());
        days.setText(doctor.getDays());
        times.setText(doctor.getTimes());
        aboutDoctor.setText(doctor.getAbout());
        servicesTv.setText(doctor.getServices());
        ratingBar.setRating(doctor.getRating());
        phones = doctor.getPhones();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.doctor_profile_menu, menu);
        MenuItem favItem = menu.findItem(R.id.action_fav);
        if (currentUser != null) {
            favItem.setVisible(true);
        } else {
            favItem.setVisible(false);
        }
        if (isFavourite) {
            favItem.setIcon(getResources().getDrawable(R.drawable.ic_favourite_white));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // close this activity and return to preview activity (if there is any)
        } else if (item.getItemId() == R.id.action_fav) {
            onFavouriteClicked(item);
        } else if (item.getItemId() == R.id.action_call) {
            onCallClicked();
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
        intent.putExtra(DOCTOR, doctor);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }

    //    @OnClick(R.id.call_button)
    public void onCallClicked() {
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

    //    @OnClick(R.id.favourite_button)
    public void onFavouriteClicked(MenuItem item) {
        if (isFavourite) {
            //delete from favourites
            Toast.makeText(this, getResources().getString(R.string.remove_from_fav), Toast.LENGTH_SHORT).show();
            isFavourite = false;
            item.setIcon(getResources().getDrawable(R.drawable.ic_favourite_border_white));
            userRef.child(DOCTORS_NODE).child(doctor.getId()).removeValue();
        } else {
            //add to favourites
            Toast.makeText(this, getResources().getString(R.string.add_to_fav), Toast.LENGTH_SHORT).show();
            isFavourite = true;
            item.setIcon(getResources().getDrawable(R.drawable.ic_favourite_white));
            //add current doctor as favourite to current user
            userRef.child(DOCTORS_NODE).child(doctor.getId()).setValue(TRUE);
        }
    }

    public void isDoctorFavourite() {
        userRef = database.getReference().child(USERS).child(currentUser.getUid());
        favouriteDoctorsEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot favDoctor : dataSnapshot.child(DOCTORS_NODE).getChildren()) {
                    if (favDoctor.getKey().equals(doctor.getId())) {
                        isFavourite = true;
//                        favouriteBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_favourite_fill));
                        invalidateOptionsMenu();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        userRef.addListenerForSingleValueEvent(favouriteDoctorsEventListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (favouriteDoctorsEventListener != null) {
            userRef.removeEventListener(favouriteDoctorsEventListener);
        }
        if (reviewsEventListener != null) {
            reviewRef.removeEventListener(reviewsEventListener);
        }
    }

    public void getDoctorRating() {
        reviewsValue = 0;
        reviewRef = database.getReference().child(REVIEWS_NODE).child(doctor.getId());
        reviewsEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot reviewChild : dataSnapshot.getChildren()) {
                    Review review = reviewChild.getValue(Review.class);
                    reviewsValue += review.getRatingValue();
                }
                //get doctor rating value
                float ratingValue = reviewsValue / dataSnapshot.getChildrenCount();
                ratingBar.setRating(ratingValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        reviewRef.addListenerForSingleValueEvent(reviewsEventListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDoctorRating();
    }
}
