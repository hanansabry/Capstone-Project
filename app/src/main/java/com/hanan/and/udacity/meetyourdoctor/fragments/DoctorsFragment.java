package com.hanan.and.udacity.meetyourdoctor.fragments;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorsFragment extends Fragment {

    public static DoctorsFragment newInstance() {
        DoctorsFragment fragment = new DoctorsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_doctors, container, false);

        //setup the Specialists Recycler View
        RecyclerView doctorsRecyclerView = rootView.findViewById(R.id.doctors_list);
        doctorsRecyclerView.addItemDecoration(new MyDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL, 0));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        doctorsRecyclerView.setLayoutManager(layoutManager);
        doctorsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //fill the recycler view with data
        DoctorsAdapter doctorsAdapter = new DoctorsAdapter(getContext(), getDoctors());
        doctorsRecyclerView.setAdapter(doctorsAdapter);
        return rootView;
    }

    public List<String> getDoctors(){
        List<String> doctors = new ArrayList();
        doctors.add("Amany Sabry");
        doctors.add("Amany Sabry");
        doctors.add("Amany Sabry");
        doctors.add("Amany Sabry");
        doctors.add("Amany Sabry");
        doctors.add("Amany Sabry");
        doctors.add("Amany Sabry");
        doctors.add("Amany Sabry");
        doctors.add("Amany Sabry");
        doctors.add("Amany Sabry");
        doctors.add("Amany Sabry");
        doctors.add("Amany Sabry");
        doctors.add("Amany Sabry");
        doctors.add("Amany Sabry");
        doctors.add("Amany Sabry");
        doctors.add("Amany Sabry");
        doctors.add("Amany Sabry");
        doctors.add("Amany Sabry");
        doctors.add("Amany Sabry");
        doctors.add("Amany Sabry");
        return doctors;
    }

}
