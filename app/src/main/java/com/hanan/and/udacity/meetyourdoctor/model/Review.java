package com.hanan.and.udacity.meetyourdoctor.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable{

    private int reviewId;
    private String reviewerName;
    private String review;
    private String reviewDate;
    private float ratingValue;
    private String doctorId;

    public Review() {
    }

    public Review(String reviewerName, String review, float ratingValue, String reviewDate, String doctorId) {
        this.reviewerName = reviewerName;
        this.review = review;
        this.ratingValue = ratingValue;
        this.reviewDate = reviewDate;
        this.doctorId = doctorId;
    }

    protected Review(Parcel in) {
        reviewId = in.readInt();
        reviewerName = in.readString();
        review = in.readString();
        ratingValue = in.readFloat();
        reviewDate = in.readString();
        doctorId = in.readString();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    public float getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(float ratingValue) {
        this.ratingValue = ratingValue;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(reviewId);
        dest.writeString(reviewerName);
        dest.writeString(review);
        dest.writeFloat(ratingValue);
        dest.writeString(reviewDate);
        dest.writeString(doctorId);
    }
}
