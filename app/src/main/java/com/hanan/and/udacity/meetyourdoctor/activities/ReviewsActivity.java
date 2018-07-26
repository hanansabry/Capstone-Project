package com.hanan.and.udacity.meetyourdoctor.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanan.and.udacity.meetyourdoctor.R;
import com.hanan.and.udacity.meetyourdoctor.adapters.ReviewsAdapter;
import com.hanan.and.udacity.meetyourdoctor.model.Doctor;
import com.hanan.and.udacity.meetyourdoctor.model.Review;
import com.hanan.and.udacity.meetyourdoctor.model.User;
import com.hanan.and.udacity.meetyourdoctor.utilities.GridSpacingItemDecoration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.ANONYMOUS;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.DOCTOR;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.REVIEWS_NODE;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.USERS;

public class ReviewsActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference doctorReviewsRef;
    private DatabaseReference userRef;
    private ChildEventListener childEventListener;
    private ValueEventListener usersEventListener;
    private String reviewerName;
    private String reviewContent;
    private float ratingValue;
    private Doctor doctor;
    private RecyclerView reviewsRecyclerView;
    private List<Review> reviews;
    private FirebaseUser currentUser;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.patients_reviews));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getExtras() != null) {
            doctor = getIntent().getParcelableExtra(DOCTOR);
        }

        database = FirebaseDatabase.getInstance();
        doctorReviewsRef = database.getReference().child(REVIEWS_NODE).child(doctor.getId());
        doctorReviewsRef.keepSynced(true);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        reviews = new ArrayList<>();

        //setup the Reviews Recycler View
        reviewsRecyclerView = findViewById(R.id.reviews_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        reviewsRecyclerView.addItemDecoration(new GridSpacingItemDecoration(1, 2, true));
        reviewsRecyclerView.setLayoutManager(layoutManager);
        //fill the recycler view with data
        addReviewsToReyclerView();
        if (currentUser != null) {
            getCurrentUserName();
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
    protected void onStop() {
        super.onStop();
        if (doctorReviewsRef != null) {
            doctorReviewsRef.removeEventListener(childEventListener);
        }
        if (userRef != null) {
            userRef.removeEventListener(usersEventListener);
        }
    }

    public void addReviewsToReyclerView() {
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Review review = dataSnapshot.getValue(Review.class);
                reviews.add(review);
                ReviewsAdapter reviewsAdapter = new ReviewsAdapter(ReviewsActivity.this, reviews);
                reviewsRecyclerView.setAdapter(reviewsAdapter);
                reviewsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Toast.makeText(ReviewsActivity.this, getString(R.string.review_deleted), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        doctorReviewsRef.addChildEventListener(childEventListener);
    }

    public void addNewReview(View view) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title(getString(R.string.add_review))
                .customView(R.layout.review_dialog, true)
                .positiveText(getString(R.string.add))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        View dialogView = dialog.getCustomView();
                        EditText reviewerET = dialogView.findViewById(R.id.reviewer_name);
                        EditText reviewContentET = dialogView.findViewById(R.id.review);
                        RatingBar reviewerRating = dialogView.findViewById(R.id.reviewer_rating);

                        if (currentUser != null) {
                            reviewerET.setText(userName);
                        }

                        reviewerName = (reviewerET.getText().toString()).equals("") ? ANONYMOUS : reviewerET.getText().toString();
                        reviewContent = reviewContentET.getText().toString();
                        ratingValue = reviewerRating.getRating();
//
//                        if (TextUtils.isEmpty(reviewContent)) {
//                            Toast.makeText(ReviewsActivity.this, getString(R.string.write_review), Toast.LENGTH_SHORT).show();
//                        } else {
                        String timestamp = getDateCurrentTime();

                        Review review = new Review(reviewerName, reviewContent, ratingValue, timestamp, doctor.getId());
                        doctorReviewsRef.push().setValue(review)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(
                                                ReviewsActivity.this,
                                                getString(R.string.success_adding_review), Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(
                                        ReviewsActivity.this,
                                        getString(R.string.failed_adding_review), Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
//                        }
                    }
                });
        MaterialDialog dialog = builder.build();
        View dialogView = dialog.getCustomView();
        EditText usernameEditText = dialogView.findViewById(R.id.reviewer_name);
        usernameEditText.setText(userName);
        dialog.show();
    }

    public String getDateCurrentTime() {
        try {
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.getTimeInMillis();
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date currentTimeZone = (Date) calendar.getTime();
            return sdf.format(currentTimeZone);
        } catch (Exception e) {
        }
        return "";
    }

    public void getCurrentUserName() {
        userRef = database.getReference().child(USERS).child(currentUser.getUid());
        usersEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                userName = user.getName();
                Log.d("User Name", userName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        userRef.addListenerForSingleValueEvent(usersEventListener);
    }
}
