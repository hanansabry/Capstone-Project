package com.hanan.and.udacity.meetyourdoctor.utilities;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import com.google.firebase.database.FirebaseDatabase;
import com.hanan.and.udacity.meetyourdoctor.R;

import java.util.Locale;

import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.AR_LOCALE;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.CITY;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.EN_LOCALE;

public class DoctorOnline extends Application {
    private static Context context;
    private static String city;

    public static Context getContext() {
        return context;
    }

    public static String getSelectedCity() {
        return city;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        context = getApplicationContext();
        int cityPosition = context.getSharedPreferences(getResources().getString(R.string.pref_file), 0)
                .getInt(CITY, 0);
        city = getResources().getStringArray(R.array.city_list)[cityPosition];
        setLocale();
    }

    public void setLocale() {
        int langIndex = context.getSharedPreferences(getResources().getString(R.string.pref_file), 0)
                .getInt(Constants.LANGUAGE, 1);
        String lang = getResources().getStringArray(R.array.language_list)[langIndex];
        Locale locale = null;
        if (lang.equals(getResources().getString(R.string.arabic))) {
            locale = new Locale(AR_LOCALE);
        } else if (lang.equals(getResources().getString(R.string.english))) {
            locale = new Locale(EN_LOCALE);
        }

        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getApplicationContext().createConfigurationContext(configuration);
        } else {
            resources.updateConfiguration(configuration, displayMetrics);
        }
    }
}
