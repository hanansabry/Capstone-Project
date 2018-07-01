package com.hanan.and.udacity.meetyourdoctor.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hanan.and.udacity.meetyourdoctor.R;
import com.hanan.and.udacity.meetyourdoctor.adapters.DoctorsAdapter;
import com.hanan.and.udacity.meetyourdoctor.data.DoctorsRetrieval;
import com.hanan.and.udacity.meetyourdoctor.model.Doctor;
import com.hanan.and.udacity.meetyourdoctor.model.Specialist;

import java.util.List;

import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.SPECIALIST;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorsFragment extends Fragment {
    List<Doctor> doctors;
    private Specialist currentSpecialist;

    public static DoctorsFragment newInstance() {
        DoctorsFragment fragment = new DoctorsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //get specialist argument else we are in favourite doctors fragment
        if(getArguments()!=null){
            currentSpecialist = getArguments().getParcelable(SPECIALIST);
        }

        //get doctors list
        DoctorsRetrieval retrieval = new DoctorsRetrieval();
        if(currentSpecialist == null){
            doctors = retrieval.getFavouriteDoctors();
        }else{
            doctors = retrieval.getDoctorsBySpecialist(currentSpecialist.getId());
        }

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_doctors, container, false);

        //setup the Specialists Recycler View
        RecyclerView doctorsRecyclerView = rootView.findViewById(R.id.doctors_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        doctorsRecyclerView.setLayoutManager(layoutManager);
        doctorsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //fill the recycler view with data
        DoctorsAdapter doctorsAdapter = new DoctorsAdapter(getContext(), doctors);
        doctorsRecyclerView.setAdapter(doctorsAdapter);
        return rootView;
    }
}
