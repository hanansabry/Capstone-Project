package com.hanan.and.udacity.meetyourdoctor.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hanan.and.udacity.meetyourdoctor.R;
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
    @BindView(R.id.profile_layout)
    LinearLayout editProfileLayout;
    @BindView(R.id.settings_layout)
    LinearLayout settingLayout;
    @BindView(R.id.login_layout)
    LinearLayout loginLayout;
    @BindView(R.id.line2)
    View line;

    Unbinder unbinder;
    private boolean signed;
    private FirebaseAuth auth;
    private FirebaseUser user;

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
        auth = FirebaseAuth.getInstance();

        //check if the user is signed
        user = auth.getCurrentUser();
        if(user == null){
            loginTv.setText(getResources().getString(R.string.login));
            editProfileLayout.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
            signed = false;
        }else{
            loginTv.setText(getResources().getString(R.string.logout, user.getEmail()));
            editProfileLayout.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
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
                    signOut();
                    //start main activity
//                    startActivity(new Intent(getActivity(), MainActivity.class));
                }else{
                    //if not signed so it's login, start LoginActivity
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        //open edit profile Activity
        editProfileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SignupActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }


    private void signOut(){
        FirebaseAuth.getInstance().signOut();
        SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences(
                getResources().getString(R.string.pref_file), 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER, ANONYMOUS);
        editor.apply();

        //restart app
        restartApplication();
    }

    public void restartApplication(){
        Intent i = new Intent(getActivity(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
