package com.hanan.and.udacity.meetyourdoctor.utilities;

import android.support.design.widget.Snackbar;
import android.view.View;

public class Constants {
    public static final String SETTINGS = "Settings";
    public static final String SPECIALIST = "specialist";
    public static final String PHONES = "phones";
    public static final String DOCTORS = "all_doctors";
    public static final String DOCTOR = "doctor";
    public static final String EDIT_PROFILE = "edit_profile";
    public static final String AR_LOCALE = "ar";
    public static final String EN_LOCALE = "en";
    public static final String NOT_SIGNED = "login";
    public static final String ARABIC = "Arabic";
    public static final String ENGLISH = "English";
    public static final String ANONYMOUS = "anonymous";
    public static final String USER = "user";
    public static final String USERS = "users";
    public static final String SPECIALISTS_NODE = "specialists";
    public static final String DOCTORS_NODE = "doctors";
    public static final String REVIEWS_NODE = "reviews";
    public static final String CITY = "city";
    public static final String LANGUAGE = "language";
    public static final String TRUE = "true";
    public static final String PREV_STARTED = "PREV_STARTED";
    public static String LOCALE = DoctorOnline.getContext().getResources().getConfiguration().locale.getDisplayLanguage();

    public static void displaySnackMessage(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
    }

    public static String getLocale() {
        return DoctorOnline.getContext().getResources().getConfiguration().locale.getDisplayLanguage();
    }
}
