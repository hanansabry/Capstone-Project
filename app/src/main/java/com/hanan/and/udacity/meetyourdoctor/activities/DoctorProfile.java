package com.hanan.and.udacity.meetyourdoctor.activities;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hanan.and.udacity.meetyourdoctor.R;
import com.hanan.and.udacity.meetyourdoctor.model.Doctor;

import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.DOCTOR;

public class DoctorProfile extends AppCompatActivity {
    private Doctor doctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initCollapsingToolbar(getResources().getString(R.string.amany_sabry));

        //get doctor object from activity intent
        if(getIntent().getParcelableExtra(DOCTOR) != null){
            doctor = getIntent().getParcelableExtra(DOCTOR);
        }

        TextView doctorName = findViewById(R.id.doctor_name);
        TextView doctorSpecialist = findViewById(R.id.doctor_specialist);
        TextView fees = findViewById(R.id.fees_tv);
        TextView address = findViewById(R.id.address_tv);
        TextView days = findViewById(R.id.days_tv);
        TextView times = findViewById(R.id.times_tv);
        RatingBar ratingBar = findViewById(R.id.rating);

        doctorName.setText(doctor.getName());
        doctorSpecialist.setText(doctor.getSpecialist().getName());
        fees.setText(doctor.getFees());
        address.setText(doctor.getAddress());
        days.setText(doctor.getDays());
        times.setText(doctor.getTimes());
        ratingBar.setRating(doctor.getRating());

        ImageButton call = findViewById(R.id.call_button);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DoctorProfile.this, "Call now on this number : 01034344829", Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton favourite = findViewById(R.id.favourite_button);
        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DoctorProfile.this, "Added to Favourites", Toast.LENGTH_SHORT).show();
            }
        });
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
        overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
    }
}
