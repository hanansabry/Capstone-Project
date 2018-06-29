package com.hanan.and.udacity.meetyourdoctor.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.hanan.and.udacity.meetyourdoctor.R;
import com.hanan.and.udacity.meetyourdoctor.adapters.DoctorsAdapter;
import com.hanan.and.udacity.meetyourdoctor.adapters.ReviewsAdapter;
import com.hanan.and.udacity.meetyourdoctor.utilities.GridSpacingItemDecoration;
import com.hanan.and.udacity.meetyourdoctor.utilities.MyDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class ReviewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.patients_reviews));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //setup the Reviews Recycler View
        RecyclerView reviewsRecyclerView = findViewById(R.id.reviews_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        reviewsRecyclerView.addItemDecoration(new GridSpacingItemDecoration(1, 2, true));
        reviewsRecyclerView.setLayoutManager(layoutManager);
        //fill the recycler view with data
        ReviewsAdapter reviewsAdapter = new ReviewsAdapter(this, getReviews());
        reviewsRecyclerView.setAdapter(reviewsAdapter);
    }

    public List<String> getReviews(){
        List<String> reviews = new ArrayList();
        reviews.add("some text reviews, some text reviewssome text reviewssome text reviewssome text reviewssome text reviewssome text reviewssome text reviewssome text reviews");
        reviews.add("some text reviews, some text reviewssome text reviewssome text reviewssome text reviewssome text reviewssome text reviewssome text reviewssome text reviews");
        reviews.add("some text reviews, some text reviewssome text reviewssome text reviewssome text reviewssome text reviewssome text reviewssome text reviewssome text reviews");
        reviews.add("some text reviews, some text reviewssome text reviewssome text reviewssome text reviewssome text reviewssome text reviewssome text reviewssome text reviews");

        return reviews;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}
