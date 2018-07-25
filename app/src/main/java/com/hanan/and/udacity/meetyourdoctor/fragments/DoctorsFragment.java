package com.hanan.and.udacity.meetyourdoctor.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanan.and.udacity.meetyourdoctor.R;
import com.hanan.and.udacity.meetyourdoctor.adapters.DoctorsAdapter;
import com.hanan.and.udacity.meetyourdoctor.model.Doctor;
import com.hanan.and.udacity.meetyourdoctor.model.Specialist;
import com.hanan.and.udacity.meetyourdoctor.utilities.DoctorOnline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.ARABIC;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.AR_LOCALE;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.DOCTORS;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.DOCTORS_NODE;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.EN_LOCALE;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.LOCALE;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.SPECIALIST;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.USERS;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorsFragment extends Fragment {
    private Specialist currentSpecialist;
    private ArrayList<Doctor> allDoctors;
    private FirebaseUser currentUser;
    private DatabaseReference ref;
    private ValueEventListener favouriteDoctorsValueListener;
    private List<Doctor> doctors;
    private DoctorsAdapter doctorsAdapter;
    private RecyclerView doctorsRecyclerView;
    private boolean search;
    ActionBar actionBar;

    public static DoctorsFragment newInstance() {
        return new DoctorsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        Bundle bundle = getArguments();
        if (bundle != null) {
            doctors = bundle.getParcelableArrayList(DOCTORS_NODE);
            //get current specialist if user comes from most common specialists fragment
            if (bundle.getParcelable(SPECIALIST) != null) {
                currentSpecialist = bundle.getParcelable(SPECIALIST);
            } else if (bundle.getBoolean("SEARCH")) {
                search = bundle.getBoolean("SEARCH");
            }
        }

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_doctors, container, false);

        //setup the Specialists Recycler View
        doctorsRecyclerView = rootView.findViewById(R.id.doctors_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        doctorsRecyclerView.setLayoutManager(layoutManager);
        doctorsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        if (currentSpecialist != null) {
            actionBar.setTitle(currentSpecialist.getName());
            doctors = getDoctorsBySpecialist();
            //fill the recycler view with data
            doctorsAdapter = new DoctorsAdapter(getContext(), doctors);
            doctorsRecyclerView.setAdapter(doctorsAdapter);
        } else if (search) {
            //showing search result
            doctorsAdapter = new DoctorsAdapter(getContext(), doctors);
            doctorsRecyclerView.setAdapter(doctorsAdapter);
        }
        return rootView;
    }

    public List<Doctor> getDoctorsBySpecialist() {
        ArrayList<Doctor> specDoctors = new ArrayList<>();
        for (Doctor doctor : doctors) {
            if (doctor.getSpecialist().getId().equals(currentSpecialist.getId())) {
                specDoctors.add(doctor);
            }
        }
        return specDoctors;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (currentSpecialist == null && currentUser != null && !search) {
            getFavouriteDoctors();
        }
    }

    public void getFavouriteDoctors() {
        actionBar.setTitle(getResources().getString(R.string.favourites));
        ref = FirebaseDatabase.getInstance().getReference().child(USERS).child(currentUser.getUid()).child(DOCTORS_NODE);
        favouriteDoctorsValueListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList favDoctors = new ArrayList<>();
                for (DataSnapshot favDoctor : dataSnapshot.getChildren()) {
                    Doctor doctor = getDoctorById(favDoctor.getKey());
                    if (doctor != null)
                        favDoctors.add(doctor);
                }
                doctorsAdapter = new DoctorsAdapter(getContext(), favDoctors);
                doctorsRecyclerView.setAdapter(doctorsAdapter);
                doctorsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        ref.addListenerForSingleValueEvent(favouriteDoctorsValueListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (favouriteDoctorsValueListener != null) {
            ref.removeEventListener(favouriteDoctorsValueListener);
        }
    }

    public Doctor getDoctorById(String id) {
        for (Doctor doc : doctors) {
            if (doc.getId().equalsIgnoreCase(id)) {
                return doc;
            }
        }
        return null;
    }
}
