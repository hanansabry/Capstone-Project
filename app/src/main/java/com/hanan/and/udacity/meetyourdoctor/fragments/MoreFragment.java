package com.hanan.and.udacity.meetyourdoctor.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.hanan.and.udacity.meetyourdoctor.R;
import com.hanan.and.udacity.meetyourdoctor.activities.DoctorProfile;
import com.hanan.and.udacity.meetyourdoctor.activities.LoginActivity;
import com.hanan.and.udacity.meetyourdoctor.activities.MainActivity;
import com.hanan.and.udacity.meetyourdoctor.activities.SettingsActivity;
import com.hanan.and.udacity.meetyourdoctor.activities.SignupActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.ANONYMOUS;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.EDIT_PROFILE;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.USER;


public class MoreFragment extends Fragment {
    @BindView(R.id.login_tv)
    TextView loginTv;

    Unbinder unbinder;
    private boolean signed;

    public MoreFragment() {
    }

    public static MoreFragment newInstance() {
        return new MoreFragment();
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
        unbinder = ButterKnife.bind(this, rootView);

        //check if the user is signed
        if(getUser().equals(ANONYMOUS)){
            loginTv.setText(getResources().getString(R.string.login));
            signed = false;
        }else{
            String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            loginTv.setText(getResources().getString(R.string.logout, userEmail));
            signed = true;
        }

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
                if(signed) {
                    //so the button is logout, set the user to anonymous
                    setAnonymousUser();
                    //start main activity
                    startActivity(new Intent(getActivity(), MainActivity.class));
                }else{
                    //if not signed so it's login, start LoginActivity
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
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

    private String getUser(){
        SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences(
                getResources().getString(R.string.pref_file), 0);
        return preferences.getString(USER, ANONYMOUS);
    }

    private void setAnonymousUser(){
        SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences(
                getResources().getString(R.string.pref_file), 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER, ANONYMOUS);
        editor.apply();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
