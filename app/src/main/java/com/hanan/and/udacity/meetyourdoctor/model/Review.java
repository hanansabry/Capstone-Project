package com.hanan.and.udacity.meetyourdoctor.model;

public class Review {

    private int reviewId;
    private String reviewerName;
    private StringBuffer review;
    private String reviewDate;
    private String doctorName;

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

    public StringBuffer getReview() {
        return review;
    }

    public void setReview(StringBuffer review) {
        this.review = review;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }
}
