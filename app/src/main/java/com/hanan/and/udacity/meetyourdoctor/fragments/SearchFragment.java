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
import com.google.firebase.database.GenericTypeIndicator;
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
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.PHONES;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.SPECIALIST;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.SPECIALISTS_NODE;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.getLocale;


public class SearchFragment extends Fragment implements SpecialistsAdapter.SpecialistAdapterCallback {
    private ArrayList<Specialist> specialists;
    private ArrayList<Doctor> doctors;
    private ActionBar actionBar;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.most_common_specialists));

        //get data from fragment arguments
        Bundle bundle = getArguments();
        doctors = bundle.getParcelableArrayList(DOCTORS_NODE);
        specialists = bundle.getParcelableArrayList(SPECIALISTS_NODE);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        //setup the Specialists Recycler View
        RecyclerView specialistRecyclerView = rootView.findViewById(R.id.specialists_list);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 3);

        specialistRecyclerView.setLayoutManager(layoutManager);
        specialistRecyclerView.setItemAnimator(new DefaultItemAnimator());
        SpecialistsAdapter specialistsAdapter = new SpecialistsAdapter(getContext(), specialists, SearchFragment.this);
        specialistRecyclerView.setAdapter(specialistsAdapter);

        return rootView;
    }

    @Override
    public void onSpecialistClick(int position) {
        Specialist specialist = specialists.get(position);
        actionBar.setTitle(specialist.getName());

        //create bundle to pass specialist object to doctors fragment
        Bundle bundle = new Bundle();
        bundle.putParcelable(SPECIALIST, specialist);
        bundle.putParcelableArrayList(DOCTORS_NODE, doctors);
        //initiate DoctorsFragment
        DoctorsFragment doctorsFragment = DoctorsFragment.newInstance();
        doctorsFragment.setArguments(bundle);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, doctorsFragment).addToBackStack("search_fragment");
        transaction.commit();
    }
}
