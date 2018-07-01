package com.hanan.and.udacity.meetyourdoctor.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanan.and.udacity.meetyourdoctor.R;
import com.hanan.and.udacity.meetyourdoctor.adapters.NewSpecialistsAdapter;
import com.hanan.and.udacity.meetyourdoctor.model.Specialist;

import java.util.ArrayList;
import java.util.List;

import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.ARABIC;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.SPECIALIST;


public class SearchFragment extends Fragment implements NewSpecialistsAdapter.SpecialistAdapterCallback {
    private List<Specialist> specialists;
    private ActionBar actionBar;
    private RecyclerView specialistRecyclerView;
    private NewSpecialistsAdapter specialistsAdapter;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    private ValueEventListener valueEventListener;
    private FirebaseDatabase firebaseDatabase;
    String locale;

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

        locale = getResources().getConfiguration().locale.getDisplayLanguage();

        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.most_common_specialists));

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        //setup the Specialists Recycler View
        specialistRecyclerView = rootView.findViewById(R.id.specialists_list);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 3);

        specialistRecyclerView.setLayoutManager(layoutManager);
        specialistRecyclerView.setItemAnimator(new DefaultItemAnimator());

        return rootView;
    }

    @Override
    public void onSpecialistClick(int position) {
        Specialist specialist = specialists.get(position);
        String specialistName;
        if(locale.equals(ARABIC)){
            specialistName = specialist.getNameAr();
        }else{
            specialistName = specialist.getName();
        }
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

    @Override
    public void onStart() {
        super.onStart();
        specialists = new ArrayList<>();
        databaseReference = firebaseDatabase.getReference("specialists");
        databaseReference.keepSynced(true);
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Specialist specialist = dataSnapshot.getValue(Specialist.class);
                specialists.add(specialist);
                specialistsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load data.", Toast.LENGTH_SHORT).show();
            }
        };

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                specialists = new ArrayList<>();
                for(DataSnapshot specialistSnapshot: dataSnapshot.getChildren()){
                    Specialist specialist = specialistSnapshot.getValue(Specialist.class);
                    specialist.setId(specialistSnapshot.getKey());
                    specialists.add(specialist);
                }
                specialistsAdapter = new NewSpecialistsAdapter(getContext(), specialists, locale, SearchFragment.this);
                specialistRecyclerView.setAdapter(specialistsAdapter);
                specialistsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load data.", Toast.LENGTH_SHORT).show();
            }
        };
//        databaseReference.addChildEventListener(childEventListener);
        databaseReference.addValueEventListener(valueEventListener);
//        specialistsAdapter = new NewSpecialistsAdapter(getContext(), specialists, locale, this);
//        specialistRecyclerView.setAdapter(specialistsAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
//        databaseReference.removeEventListener(childEventListener);
        databaseReference.removeEventListener(valueEventListener);
    }

//    public void getSpecialists(){
//
//    }

}
