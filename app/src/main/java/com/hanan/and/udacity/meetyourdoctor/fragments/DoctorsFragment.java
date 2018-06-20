package com.hanan.and.udacity.meetyourdoctor.fragments;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hanan.and.udacity.meetyourdoctor.R;
import com.hanan.and.udacity.meetyourdoctor.adapters.DoctorsAdapter;
import com.hanan.and.udacity.meetyourdoctor.adapters.SpecialistsAdapter;
import com.hanan.and.udacity.meetyourdoctor.utilities.GridSpacingItemDecoration;
import com.hanan.and.udacity.meetyourdoctor.utilities.MyDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.SPECIALIST;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorsFragment extends Fragment {
    List<String> doctors;
    String currentSpecialist;

    public static DoctorsFragment newInstance() {
        DoctorsFragment fragment = new DoctorsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(getArguments() != null){
            currentSpecialist = getArguments().getString(SPECIALIST);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(currentSpecialist);
        }

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_doctors, container, false);

        //setup the Specialists Recycler View
        RecyclerView doctorsRecyclerView = rootView.findViewById(R.id.doctors_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        doctorsRecyclerView.setLayoutManager(layoutManager);
        doctorsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //fill the recycler view with data
        DoctorsAdapter doctorsAdapter = new DoctorsAdapter(getContext(), getDoctorsList());
        doctorsRecyclerView.setAdapter(doctorsAdapter);
        return rootView;
    }

    public void setDoctorsList(List<String> doctors){
        this.doctors = doctors;
    }

    public List<String> getDoctorsList(){
        return doctors;
    }
}
