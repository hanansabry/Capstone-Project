package com.hanan.and.udacity.meetyourdoctor.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hanan.and.udacity.meetyourdoctor.R;
import com.hanan.and.udacity.meetyourdoctor.adapters.SpecialistsAdapter;
import com.hanan.and.udacity.meetyourdoctor.data.SpecialistsRetrieval;
import com.hanan.and.udacity.meetyourdoctor.model.Specialist;

import java.util.ArrayList;
import java.util.List;

import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.SPECIALIST;


public class SearchFragment extends Fragment implements SpecialistsAdapter.SpecialistAdapterCallback {
    private List<Specialist> specialists;
    private ActionBar actionBar;

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
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.most_common_specialists));

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        //setup the Specialists Recycler View
        RecyclerView specialistRecyclerView = rootView.findViewById(R.id.specialists_list);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 3);

        specialistRecyclerView.setLayoutManager(layoutManager);
        specialistRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //get Specialist list
        specialists = new SpecialistsRetrieval(getContext()).getSpecialists();

        //fill the recycler view with data
        SpecialistsAdapter specialistsAdapter = new SpecialistsAdapter(getContext(), specialists, this);
        specialistRecyclerView.setAdapter(specialistsAdapter);
        return rootView;
    }

    @Override
    public void onSpecialistClick(int position) {
        Specialist specialist = specialists.get(position);
        String specialistName = specialist.getSpecialistName();
        actionBar.setTitle(specialistName);

        //create bundle to pass specialist object to doctors fragment
        Bundle bundle = new Bundle();
        bundle.putParcelable(SPECIALIST, specialist);
        //initiate DoctorsFragment
        DoctorsFragment doctorsFragment = DoctorsFragment.newInstance();

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, doctorsFragment).addToBackStack("search_fragment");
        transaction.commit();
    }
}
