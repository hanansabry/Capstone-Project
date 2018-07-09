package com.hanan.and.udacity.meetyourdoctor.utilities;

import android.app.Application;
import android.content.Context;

import com.google.firebase.database.FirebaseDatabase;

public class DoctorOnline extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
