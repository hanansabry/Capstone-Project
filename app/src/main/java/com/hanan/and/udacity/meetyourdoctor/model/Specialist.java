package com.hanan.and.udacity.meetyourdoctor.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Specialist implements Parcelable, Serializable{
    private String id;
    private String name;
    private String desc;
    private String iconUrl;

    public Specialist() {
    }

    public Specialist(String id, String name, String desc, String iconUrl) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.iconUrl = iconUrl;
    }

    protected Specialist(Parcel in) {
        name = in.readString();
        desc = in.readString();
        iconUrl = in.readString();
        id = in.readString();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(desc);
        dest.writeString(iconUrl);
        dest.writeString(id);
    }
}
