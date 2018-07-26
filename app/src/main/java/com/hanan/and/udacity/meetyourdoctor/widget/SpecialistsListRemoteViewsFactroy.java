package com.hanan.and.udacity.meetyourdoctor.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanan.and.udacity.meetyourdoctor.R;
import com.hanan.and.udacity.meetyourdoctor.model.Specialist;
import com.hanan.and.udacity.meetyourdoctor.utilities.ObjectSerializer;

import java.io.IOException;
import java.util.ArrayList;

import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.SPECIALIST;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.SPECIALISTS_NODE;

public class SpecialistsListRemoteViewsFactroy implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private ValueEventListener valueEventListener;
    private ArrayList<Specialist> specialists;

    SpecialistsListRemoteViewsFactroy(Context context) {
        mContext = context;
    }

    public static Intent updateWidgetList(Context context) {
        Intent intent = new Intent(context, MyAppWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, MyAppWidgetProvider.class));

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds);
        return intent;
    }

    @Override
    public void onCreate() {
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    public void onDataSetChanged() {
//        get specialists list from the firebase
//        if (getLocale().equals(ARABIC)) {
//            databaseReference = firebaseDatabase.getReference(AR_LOCALE);
//        } else {
//            databaseReference = firebaseDatabase.getReference(EN_LOCALE);
//        }
//        databaseReference.keepSynced(true);
//
//        valueEventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    specialists = new ArrayList<>();
//
//                    //get specialists list
//                    for (DataSnapshot specialistsNode : dataSnapshot.child(SPECIALISTS_NODE).getChildren()) {
//                        Specialist specialist = specialistsNode.getValue(Specialist.class);
//                        specialist.setId(specialistsNode.getKey());
//                        specialists.add(specialist);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        };
//        databaseReference.addListenerForSingleValueEvent(valueEventListener);
//        populateListItems();
        getSpecialistsList();
    }

    public void getSpecialistsList() {
        SharedPreferences prefs = mContext.getSharedPreferences(mContext.getString(R.string.pref_file), Context.MODE_PRIVATE);

        try {
            specialists = (ArrayList<Specialist>) ObjectSerializer.deserialize(prefs.getString(SPECIALISTS_NODE, ObjectSerializer.serialize(new ArrayList<Specialist>())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void populateListItems() {
        specialists = new ArrayList<>();
        Specialist s1 = new Specialist();
        s1.setId("allergy");
        s1.setName("Allergy and Immunology");
        s1.setIconUrl("https://firebasestorage.googleapis.com/v0/b/doctor-online-d16a3.appspot.com/o/ic_allergy.png?alt=media&token=341bf238-0f1c-4141-a892-ce0e8e487a8e");

        Specialist s2 = new Specialist();
        s2.setId("dentistry");
        s2.setName("Dentistry");
        s2.setIconUrl("https://firebasestorage.googleapis.com/v0/b/doctor-online-d16a3.appspot.com/o/ic_dentistry.png?alt=media&token=95353552-ff11-4ced-acf6-e65aa965809e");

        Specialist s3 = new Specialist();
        s3.setId("allergy");
        s3.setName("Allergy and Immunology");
        s3.setIconUrl("https://firebasestorage.googleapis.com/v0/b/doctor-online-d16a3.appspot.com/o/ic_allergy.png?alt=media&token=341bf238-0f1c-4141-a892-ce0e8e487a8e");

        Specialist s4 = new Specialist();
        s4.setId("new_born");
        s4.setName("Pediatrics and New Born");
        s4.setIconUrl("https://firebasestorage.googleapis.com/v0/b/doctor-online-d16a3.appspot.com/o/ic_newborn.png?alt=media&token=85804f28-1c6b-44ae-a9b8-22545b558940");
        specialists.add(s1);
        specialists.add(s2);
        specialists.add(s3);
        specialists.add(s4);
    }

    @Override
    public void onDestroy() {
        if (valueEventListener != null) {
            databaseReference.removeEventListener(valueEventListener);
        }
    }

    @Override
    public int getCount() {
        return specialists == null ? 0 : specialists.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(mContext, MyAppWidgetProvider.class));

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.specialist_item_layout);

        Specialist specialist = specialists.get(position);
        //fill specialist icon
        try {
            Bitmap bitmap = Glide.with(mContext)
                    .load(specialist.getIconUrl())
                    .asBitmap()
                    .placeholder(R.drawable.ic_specialist_placeholder)
                    .into(100, 100)
                    .get();

            views.setImageViewBitmap(R.id.specialist_icon, bitmap);
        } catch (Exception e) {
            views.setImageViewResource(R.id.specialist_icon, R.drawable.ic_specialist_placeholder);
        }
        //fill specialist name
        views.setTextViewText(R.id.specialist_name, specialist.getName());

        Bundle bundle = new Bundle();
        bundle.putParcelable(SPECIALIST, specialist);
        Intent fillIntent = new Intent();
        fillIntent.putExtras(bundle);
        views.setOnClickFillInIntent(R.id.specialist_icon, fillIntent);
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
