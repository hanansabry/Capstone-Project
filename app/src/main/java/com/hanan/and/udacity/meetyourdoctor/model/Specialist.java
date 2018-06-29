package com.hanan.and.udacity.meetyourdoctor.model;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

public class Specialist implements Parcelable{
    
    private int specialistId;
    private String specialistName;
    private String specialistDesc;
    private Drawable specialistImageDrawable;

    public Specialist(int specialistId, String specialistName, String specialistDesc, Drawable specialistImageDrawable){
        this.specialistId = specialistId;
        this.specialistName = specialistName;
        this.specialistDesc = specialistDesc;
        this.specialistImageDrawable = specialistImageDrawable;
    }

    protected Specialist(Parcel in) {
        specialistId = in.readInt();
        specialistName = in.readString();
        specialistDesc = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(specialistId);
        dest.writeString(specialistName);
        dest.writeString(specialistDesc);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Specialist> CREATOR = new Creator<Specialist>() {
        @Override
        public Specialist createFromParcel(Parcel in) {
            return new Specialist(in);
        }

        @Override
        public Specialist[] newArray(int size) {
            return new Specialist[size];
        }
    };

    public int getSpecialistId() {
        return specialistId;
    }

    public void setSpecialistId(int specialistId) {
        this.specialistId = specialistId;
    }

    public String getSpecialistName() {
        return specialistName;
    }

    public void setSpecialistName(String specialistName) {
        this.specialistName = specialistName;
    }

    public String getSpecialistDesc() {
        return specialistDesc;
    }

    public void setSpecialistDesc(String specialistDesc) {
        this.specialistDesc = specialistDesc;
    }

    public Drawable getSpecialistImageDrawable() {
        return specialistImageDrawable;
    }

    public void setSpecialistImageDrawable(Drawable specialistImageDrawable) {
        this.specialistImageDrawable = specialistImageDrawable;
    }
}
