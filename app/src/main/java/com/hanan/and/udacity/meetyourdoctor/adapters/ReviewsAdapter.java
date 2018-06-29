package com.hanan.and.udacity.meetyourdoctor.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hanan.and.udacity.meetyourdoctor.R;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewHolder> {
    Context mContext;
    List<String> mReviewsList;

    public ReviewsAdapter(Context context, List<String> reviewsList){
        mContext = context;
        mReviewsList = reviewsList;
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_review_item, parent, false);
        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        holder.ratingBar.setRating(3f);
    }

    @Override
    public int getItemCount() {
        return mReviewsList.size();
    }

    class ReviewHolder extends RecyclerView.ViewHolder{
        TextView userNameTextView, reviewDateTextView, reviewTextView;
        RatingBar ratingBar;

        public ReviewHolder(View itemView) {
            super(itemView);

            userNameTextView = itemView.findViewById(R.id.username_tv);
            reviewDateTextView = itemView.findViewById(R.id.review_date_tv);
            reviewTextView = itemView.findViewById(R.id.review_tv);
            ratingBar = itemView.findViewById(R.id.rating);
        }
    }
}
