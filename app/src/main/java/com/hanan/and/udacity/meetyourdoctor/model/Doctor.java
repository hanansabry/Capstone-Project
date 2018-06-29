package com.hanan.and.udacity.meetyourdoctor.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Doctor implements Parcelable{

    private int doctorId;
    private String doctorName;
    private String specialist;
    private String study;
    private String fees;
    private String clinicDays;
    private String clinicTimes;
    private String address;
    private String langitude;
    private String latitiude;
    private String[] clinicServices;
    private String city;
    private String profilePicture;
    private float rating;

    public Doctor(){}

    protected Doctor(Parcel in) {
        doctorId = in.readInt();
        doctorName = in.readString();
        specialist = in.readString();
        study = in.readString();
        fees = in.readString();
        clinicDays = in.readString();
        clinicTimes = in.readString();
        address = in.readString();
        langitude = in.readString();
        latitiude = in.readString();
        clinicServices = in.createStringArray();
        city = in.readString();
        profilePicture = in.readString();
        rating = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(doctorId);
        dest.writeString(doctorName);
        dest.writeString(specialist);
        dest.writeString(study);
        dest.writeString(fees);
        dest.writeString(clinicDays);
        dest.writeString(clinicTimes);
        dest.writeString(address);
        dest.writeString(langitude);
        dest.writeString(latitiude);
        dest.writeStringArray(clinicServices);
        dest.writeString(city);
        dest.writeString(profilePicture);
        dest.writeFloat(rating);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getSpecialist() {
        return specialist;
    }

    public void setSpecialist(String specialist) {
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

    public String getClinicTimes() {
        return clinicTimes;
    }

    public void setClinicTimes(String clinicTimes) {
        this.clinicTimes = clinicTimes;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLangitude() {
        return langitude;
    }

    public void setLangitude(String langitude) {
        this.langitude = langitude;
    }

    public String getLatitiude() {
        return latitiude;
    }

    public void setLatitiude(String latitiude) {
        this.latitiude = latitiude;
    }

    public String[] getClinicServices() {
        return clinicServices;
    }

    public void setClinicServices(String[] clinicServices) {
        this.clinicServices = clinicServices;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getClinicDays() {
        return clinicDays;
    }

    public void setClinicDays(String clinicDays) {
        this.clinicDays = clinicDays;
    }
}
