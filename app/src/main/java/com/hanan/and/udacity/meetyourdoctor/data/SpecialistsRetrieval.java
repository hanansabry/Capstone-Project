package com.hanan.and.udacity.meetyourdoctor.data;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

import com.hanan.and.udacity.meetyourdoctor.R;
import com.hanan.and.udacity.meetyourdoctor.model.Specialist;

import java.util.ArrayList;
import java.util.List;

public class SpecialistsRetrieval {

    private Context context;

    public SpecialistsRetrieval(Context context) {
        this.context = context;
    }

    public List<Specialist> getSpecialists() {
        ArrayList<Specialist> specialists = new ArrayList<>();
        String[] names = getSpecialistsNames();
        TypedArray icons = getSpecialistsIcons();


        for (int i = 0; i < names.length; i++) {
            Specialist s = new Specialist(i, names[i], null, icons.getDrawable(i));
            specialists.add(s);
        }

        return specialists;
    }

    private String[] getSpecialistsNames() {
        return context.getResources().getStringArray(R.array.specialists);
    }

    private TypedArray getSpecialistsIcons() {
        return context.getResources().obtainTypedArray(R.array.specialists_icons);
    }
}
