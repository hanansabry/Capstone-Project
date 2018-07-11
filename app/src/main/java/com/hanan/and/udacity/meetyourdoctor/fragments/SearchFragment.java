package com.hanan.and.udacity.meetyourdoctor.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanan.and.udacity.meetyourdoctor.R;
import com.hanan.and.udacity.meetyourdoctor.adapters.SpecialistsAdapter;
import com.hanan.and.udacity.meetyourdoctor.model.Doctor;
import com.hanan.and.udacity.meetyourdoctor.model.Specialist;
import com.hanan.and.udacity.meetyourdoctor.utilities.DoctorOnline;

import java.util.ArrayList;
import java.util.List;

import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.ARABIC;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.AR_LOCALE;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.CITY;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.DOCTORS;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.DOCTORS_NODE;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.EN_LOCALE;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.LOCALE;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.SPECIALIST;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.SPECIALISTS_NODE;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.getLocale;


public class SearchFragment extends Fragment implements SpecialistsAdapter.SpecialistAdapterCallback {
    private ArrayList<Specialist> specialists;
    private ArrayList<Doctor> doctors;
    private ActionBar actionBar;
    private RecyclerView specialistRecyclerView;
    private SpecialistsAdapter specialistsAdapter;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    private FirebaseDatabase firebaseDatabase;
    private String selectedCity;
    private String[] cities;

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.most_common_specialists));

        selectedCity = getSelectedCity();
        cities = getResources().getStringArray(R.array.city_list);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        //setup the Specialists Recycler View
        specialistRecyclerView = rootView.findViewById(R.id.specialists_list);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 3);

        specialistRecyclerView.setLayoutManager(layoutManager);
        specialistRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //get all data from firebase database
        getData();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        databaseReference.removeEventListener(valueEventListener);
    }

    //get all data from firebase
    public void getData() {
        if (getLocale().equals(ARABIC)) {
            databaseReference = firebaseDatabase.getReference(AR_LOCALE);
        } else {
            databaseReference = firebaseDatabase.getReference(EN_LOCALE);
        }
        databaseReference.keepSynced(true);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    specialists = new ArrayList<>();
                    doctors = new ArrayList<>();

                    //get specialists list
                    for (DataSnapshot specialistsNode : dataSnapshot.child(SPECIALISTS_NODE).getChildren()) {
                        Specialist specialist = specialistsNode.getValue(Specialist.class);
                        specialist.setId(specialistsNode.getKey());
                        specialists.add(specialist);
                    }
                    specialistsAdapter = new SpecialistsAdapter(getContext(), specialists, SearchFragment.this);
                    specialistRecyclerView.setAdapter(specialistsAdapter);
                    specialistsAdapter.notifyDataSetChanged();

                    //get doctors list
                    for (DataSnapshot doctorsNode : dataSnapshot.child(DOCTORS_NODE).getChildren()) {
                        Doctor doctor = doctorsNode.getValue(Doctor.class);

                        Iterable<DataSnapshot> children = doctorsNode.child(SPECIALIST).getChildren();
                        for (DataSnapshot specialist : children) {
                            doctor.setSpecialist(getSpecialistByKey(specialist.getKey()));
                        }
                        doctor.setId(doctorsNode.getKey());

                        //get doctors of only selected city, if all cities is selected get all doctors
                        if(selectedCity.equals(cities[0])){
                            doctors.add(doctor);
                        }else{
                            if(doctor.getCity().toLowerCase().equals(selectedCity.toLowerCase())){
                                doctors.add(doctor);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.addListenerForSingleValueEvent(valueEventListener);
    }


    public Specialist getSpecialistByKey(final String key) {
        for (Specialist specialist : specialists) {
            if (specialist.getId().equals(key)) {
                return specialist;
            }
        }
        return null;
    }

    @Override
    public void onSpecialistClick(int position) {
        Specialist specialist = specialists.get(position);
        actionBar.setTitle(specialist.getName());

        //create bundle to pass specialist object to doctors fragment
        Bundle bundle = new Bundle();
        bundle.putParcelable(SPECIALIST, specialist);
        bundle.putParcelableArrayList(DOCTORS, doctors);
        //initiate DoctorsFragment
        DoctorsFragment doctorsFragment = DoctorsFragment.newInstance();
        doctorsFragment.setArguments(bundle);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, doctorsFragment).addToBackStack("search_fragment");
        transaction.commit();
    }

    public String getSelectedCity(){
        int cityPosition = getActivity().getApplicationContext()
                .getSharedPreferences(getResources().getString(R.string.pref_file), 0)
                .getInt(CITY, 0);
        String city = getResources().getStringArray(R.array.city_list)[cityPosition];
        return city;
    }
}
