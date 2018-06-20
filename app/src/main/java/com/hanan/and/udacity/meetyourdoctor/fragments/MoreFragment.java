package com.hanan.and.udacity.meetyourdoctor.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hanan.and.udacity.meetyourdoctor.R;
import com.hanan.and.udacity.meetyourdoctor.activities.DoctorProfile;
import com.hanan.and.udacity.meetyourdoctor.activities.LoginActivity;
import com.hanan.and.udacity.meetyourdoctor.activities.SettingsActivity;
import com.hanan.and.udacity.meetyourdoctor.activities.SignupActivity;

import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.EDIT_PROFILE;


public class MoreFragment extends Fragment {

    public static MoreFragment newInstance() {
        MoreFragment fragment = new MoreFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_more, container, false);

        //open settings activity
        LinearLayout settingsLayout = rootView.findViewById(R.id.settings_layout);
        settingsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        //open login activity
        LinearLayout loginLayout = rootView.findViewById(R.id.login_layout);
        loginLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        //open profile Activity
        LinearLayout profileLayout = rootView.findViewById(R.id.profile_layout);
        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SignupActivity.class);
                intent.putExtra(EDIT_PROFILE, getContext().getResources().getString(R.string.edit_profile));
                startActivity(intent);
            }
        });

        return rootView;
    }
}
