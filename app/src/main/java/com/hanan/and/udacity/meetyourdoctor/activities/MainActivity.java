package com.hanan.and.udacity.meetyourdoctor.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanan.and.udacity.meetyourdoctor.R;
import com.hanan.and.udacity.meetyourdoctor.adapters.SpecialistsAdapter;
import com.hanan.and.udacity.meetyourdoctor.fragments.DoctorsFragment;
import com.hanan.and.udacity.meetyourdoctor.fragments.MoreFragment;
import com.hanan.and.udacity.meetyourdoctor.fragments.SearchFragment;
import com.hanan.and.udacity.meetyourdoctor.model.Doctor;
import com.hanan.and.udacity.meetyourdoctor.model.Specialist;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.ANONYMOUS;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.ARABIC;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.AR_LOCALE;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.CITY;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.DOCTORS;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.DOCTORS_NODE;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.EN_LOCALE;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.NOT_SIGNED;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.SPECIALIST;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.SPECIALISTS_NODE;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.USER;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.getLocale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private MaterialSearchView searchView;
    private CollapsingToolbarLayout collapsingToolbar;
    private Toolbar toolbar;
    private boolean searchViewEnabled = true;
    private ActionBar actionBar;
    private boolean isSigned = false;
    private String user;

    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    private FirebaseDatabase firebaseDatabase;

    private ArrayList<Specialist> specialists;
    private ArrayList<Doctor> doctors;
    private String selectedCity;
    private String[] cities;
    private Bundle dataBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get firebase database instance
        firebaseDatabase = FirebaseDatabase.getInstance();
        //------------------------------------------------------------------------------------------
        //get selected city from shared preferences
        selectedCity = getSelectedCity();
        cities = getResources().getStringArray(R.array.city_list);

        getData();
        //------------------------------------------------------------------------------------------
        //setup the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        //------------------------------------------------------------------------------------------
        //initiate the SearchView
        initiateSearchView();
        //------------------------------------------------------------------------------------------
        //check if the user is signed
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            isSigned = false;
        } else {
            isSigned = true;
        }
        //------------------------------------------------------------------------------------------
        //setup the Bottom Navigation View
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_search:
                                actionBar.setTitle(getResources().getString(R.string.most_common_specialists));
                                selectedFragment = SearchFragment.newInstance();
                                selectedFragment.setArguments(dataBundle);
                                transaction.replace(R.id.frame_layout, selectedFragment, "specialist");
                                transaction.commit();
                                searchView.setVisibility(View.VISIBLE);
                                searchViewEnabled = true;
                                searchView.setSuggestions(getResources().getStringArray(R.array.specialists_suggestions));
                                break;
                            case R.id.action_favourites:
                                if (isSigned) {
                                    //if the user is signed start favourites doctors screen
                                    actionBar.setTitle(getResources().getString(R.string.favourites));
                                    selectedFragment = DoctorsFragment.newInstance();
                                    selectedFragment.setArguments(dataBundle);
                                    transaction.replace(R.id.frame_layout, selectedFragment);
                                    transaction.commit();
                                    searchView.setVisibility(View.VISIBLE);
                                    searchViewEnabled = true;
                                    searchView.setSuggestions(getResources().getStringArray(R.array.doctors_suggestions));
                                } else {
                                    finish();
                                    //start login activity
                                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                    intent.putExtra(NOT_SIGNED, true);
                                    startActivity(intent);
                                }
                                break;
                            case R.id.action_more:
                                actionBar.setTitle(getResources().getString(R.string.more_action_string));
                                selectedFragment = MoreFragment.newInstance();
                                transaction.replace(R.id.frame_layout, selectedFragment);
                                transaction.commit();
                                searchView.setVisibility(View.INVISIBLE);
                                searchViewEnabled = false;
                                break;
                        }
                        invalidateOptionsMenu();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        bottomNavigationView.getMenu().getItem(0).setChecked(true);
        //Used to select an item programmatically
        //bottomNavigationView.getMenu().getItem(2).setChecked(true);
        //------------------------------------------------------------------------------------------

    }

    public void displaySearchFragment(){
        actionBar.setTitle(getResources().getString(R.string.most_common_specialists));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        SearchFragment searchFragment = SearchFragment.newInstance();
        dataBundle = new Bundle();
        dataBundle.putParcelableArrayList(DOCTORS_NODE, doctors);
        dataBundle.putParcelableArrayList(SPECIALISTS_NODE, specialists);
        searchFragment.setArguments(dataBundle);
        transaction.replace(R.id.frame_layout, searchFragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView.setMenuItem(searchItem);

        if (searchViewEnabled) {
            searchItem.setVisible(true);
        } else {
            searchItem.setVisible(false);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public void initiateSearchView() {
        searchView = findViewById(R.id.search_view);
        searchView.setEllipsize(true);
        searchView.setVoiceSearch(true);
        searchView.setSuggestions(getResources().getStringArray(R.array.specialists_suggestions));
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(MainActivity.this, newText, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                Toast.makeText(MainActivity.this, "SearchView is shown", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSearchViewClosed() {
                Toast.makeText(MainActivity.this, "SearchView is closed", Toast.LENGTH_SHORT).show();
            }
        });
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
                    };

                    //get doctors list
                    for (DataSnapshot doctorsNode : dataSnapshot.child(DOCTORS_NODE).getChildren()) {
                        Doctor doctor = doctorsNode.getValue(Doctor.class);

                        //get doctor specialist
                        Iterable<DataSnapshot> children = doctorsNode.child(SPECIALIST).getChildren();
                        for (DataSnapshot specialist : children) {
                            doctor.setSpecialist(getSpecialistByKey(specialist.getKey()));
                        }

                        doctor.setId(doctorsNode.getKey());

                        //get doctors of only selected city, if all cities is selected get all doctors
                        if(selectedCity.equals(cities[0])){
                            doctors.add(doctor);
                        }else{
                            if(doctor.getCity().equalsIgnoreCase(selectedCity.toLowerCase())){
                                doctors.add(doctor);
                            }
                        }
                    }
                }
                displaySearchFragment();
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

    public String getSelectedCity(){
        int cityPosition = getApplicationContext()
                .getSharedPreferences(getResources().getString(R.string.pref_file), 0)
                .getInt(CITY, 0);
        String city = getResources().getStringArray(R.array.city_list)[cityPosition];
        return city;
    }
}