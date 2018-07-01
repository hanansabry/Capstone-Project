package com.hanan.and.udacity.meetyourdoctor.utilities;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class DoctorOnline extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
