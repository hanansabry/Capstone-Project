package com.hanan.and.udacity.meetyourdoctor.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hanan.and.udacity.meetyourdoctor.R;
import com.hanan.and.udacity.meetyourdoctor.adapters.SpecialistsAdapter;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        //setup the Specialists Recycler View
        RecyclerView specialistRecyclerView = rootView.findViewById(R.id.specialists_list);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 3);

        specialistRecyclerView.setLayoutManager(layoutManager);
        specialistRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //fill the recycler view with data
        SpecialistsAdapter specialistsAdapter = new SpecialistsAdapter(getContext(), getSpecialists());
        specialistRecyclerView.setAdapter(specialistsAdapter);
        return rootView;
    }

    //fake date for the specialist list
    public List<String> getSpecialists(){
        List<String> specialistList = new ArrayList();

        specialistList.add("Dermatology");
        specialistList.add("Dentistry");
        specialistList.add("Psychiatry");
        specialistList.add("Pediatrics and New Born");
        specialistList.add("Neurology");
        specialistList.add("Orthopedics");
        specialistList.add("Gynaecology and Infertility");
        specialistList.add("Ear, Nose and Throat");
        specialistList.add("Cardiology and Vascular Disease");
        specialistList.add("Allergy and Immunology");

        return specialistList;
    }
}
