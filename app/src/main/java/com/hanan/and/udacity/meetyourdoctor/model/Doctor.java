package com.hanan.and.udacity.meetyourdoctor.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Doctor implements Parcelable{

    private String id;
    private String name;
    private Specialist specialist;
    private String study;
    private String fees;
    private String days;
    private String times;
    private String address;
    private String services;
    private String city;
    private String photoUrl;
    private float rating;

    public Doctor() {
    }

    protected Doctor(Parcel in) {
        id = in.readString();
        name = in.readString();
        specialist = in.readParcelable(Specialist.class.getClassLoader());
        study = in.readString();
        fees = in.readString();
        days = in.readString();
        times = in.readString();
        address = in.readString();
        services = in.readString();
        city = in.readString();
        photoUrl = in.readString();
        rating = in.readFloat();
    }

    public static final Creator<Doctor> CREATOR = new Creator<Doctor>() {
        @Override
        public Doctor createFromParcel(Parcel in) {
            return new Doctor(in);
        }

        @Override
        public Doctor[] newArray(int size) {
            return new Doctor[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Specialist getSpecialist() {
        return specialist;
    }

    public void setSpecialist(Specialist specialist) {
        this.specialist = specialist;
    }

    public String getStudy() {
        return study;
    }

    public void setStudy(String study) {
        this.study = study;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeParcelable(specialist, flags);
        dest.writeString(study);
        dest.writeString(fees);
        dest.writeString(days);
        dest.writeString(times);
        dest.writeString(address);
        dest.writeString(services);
        dest.writeString(city);
        dest.writeString(photoUrl);
        dest.writeFloat(rating);
    }
}
