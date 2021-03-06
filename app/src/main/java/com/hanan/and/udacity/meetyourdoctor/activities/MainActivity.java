package com.hanan.and.udacity.meetyourdoctor.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanan.and.udacity.meetyourdoctor.R;
import com.hanan.and.udacity.meetyourdoctor.fragments.DoctorsFragment;
import com.hanan.and.udacity.meetyourdoctor.fragments.MoreFragment;
import com.hanan.and.udacity.meetyourdoctor.fragments.SpecialistsFragment;
import com.hanan.and.udacity.meetyourdoctor.model.Doctor;
import com.hanan.and.udacity.meetyourdoctor.model.Specialist;
import com.hanan.and.udacity.meetyourdoctor.utilities.ObjectSerializer;
import com.hanan.and.udacity.meetyourdoctor.widget.SpecialistsListRemoteViewsFactroy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.mauker.materialsearchview.MaterialSearchView;

import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.ARABIC;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.AR_LOCALE;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.CITY;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.DATA;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.DOCTORS_FAVOURITES_TAG;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.DOCTORS_NODE;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.DOCTORS_SPECIALIST_TAG;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.EN_LOCALE;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.NAVIGATION_ID;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.NOT_SIGNED;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.SPECIALIST;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.SPECIALISTS_NODE;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.getLocale;

public class MainActivity extends AppCompatActivity {

    private MaterialSearchView searchView;
    private Toolbar toolbar;
    private LinearLayout loadingLayout;
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
    private BottomNavigationView bottomNavigationView;
    private MaterialDialog searchProgressDialog;
    private FragmentManager fm;

    private int selectedItemSaveState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initiate vars
        fm = getSupportFragmentManager();
        //get firebase database instance
        firebaseDatabase = FirebaseDatabase.getInstance();
        //------------------------------------------------------------------------------------------
        //get selected city from shared preferences
        selectedCity = getSelectedCity();
        cities = getResources().getStringArray(R.array.city_list);

        //------------------------------------------------------------------------------------------
        //setup the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.most_common_specialists));

        loadingLayout = findViewById(R.id.loading_layout);
        loadingLayout.setVisibility(View.VISIBLE);
        //------------------------------------------------------------------------------------------
        //initiate the SearchView
        initiateSearchView();
        //------------------------------------------------------------------------------------------
        //check if the user is signed
        isSigned = FirebaseAuth.getInstance().getCurrentUser() != null;
        //------------------------------------------------------------------------------------------
        //setup the Bottom Navigation View
        setupBottomNavigation();

        if (savedInstanceState != null) {
            loadingLayout.setVisibility(View.INVISIBLE);
            dataBundle = savedInstanceState.getBundle(DATA);
            Fragment dfragment = getSupportFragmentManager().findFragmentByTag(DOCTORS_SPECIALIST_TAG);
            selectedItemSaveState = savedInstanceState.getInt(NAVIGATION_ID);
            if (dfragment != null && selectedItemSaveState != R.id.action_favourites) {
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.frame_layout, dfragment);
                transaction.commit();
            } else {
                bottomNavigationView.setSelectedItemId(selectedItemSaveState);
            }
        } else {
            getData();
            bottomNavigationView.getMenu().getItem(0).setChecked(true);
            Intent widgetIntent = SpecialistsListRemoteViewsFactroy.updateWidgetList(getApplicationContext());
            sendBroadcast(widgetIntent);
        }
    }


    private void setupBottomNavigation() {
        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        FragmentTransaction transaction = fm.beginTransaction();
                        Fragment selectedFragment = fm.findFragmentById(R.id.frame_layout);
                        switch (item.getItemId()) {
                            case R.id.action_search:
                                actionBar.setTitle(getResources().getString(R.string.most_common_specialists));
                                //check if specialist docotors fragment is already opened
                                if (fm.findFragmentByTag(DOCTORS_SPECIALIST_TAG) != null) {
                                    selectedFragment = fm.findFragmentByTag(DOCTORS_SPECIALIST_TAG);
                                    transaction.replace(R.id.frame_layout, selectedFragment);
                                } else if (selectedFragment == null || !(selectedFragment instanceof SpecialistsFragment)) {
                                    selectedFragment = SpecialistsFragment.newInstance();
                                    selectedFragment.setArguments(dataBundle);
                                    transaction.replace(R.id.frame_layout, selectedFragment);
                                } else {
                                    transaction.replace(R.id.frame_layout, selectedFragment);
                                }
                                transaction.commit();
                                searchViewEnabled = true;
                                break;
                            case R.id.action_favourites:
                                loadingLayout.setVisibility(View.INVISIBLE);
                                if (isSigned) {
                                    //if the user is signed start favourites doctors screen
                                    actionBar.setTitle(getResources().getString(R.string.favourites));
                                    if (selectedFragment == null || !(selectedFragment instanceof DoctorsFragment)
                                            || !selectedFragment.getTag().equals(DOCTORS_FAVOURITES_TAG)) {
                                        selectedFragment = DoctorsFragment.newInstance();
                                        selectedFragment.setArguments(dataBundle);
                                    }
                                    transaction.replace(R.id.frame_layout, selectedFragment, DOCTORS_FAVOURITES_TAG);
                                    transaction.commit();
                                    searchViewEnabled = false;
                                } else {
                                    finish();
                                    //start login activity
                                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                    intent.putExtra(NOT_SIGNED, true);
                                    startActivity(intent);
                                }
                                break;
                            case R.id.action_more:
                                loadingLayout.setVisibility(View.INVISIBLE);
                                actionBar.setTitle(getResources().getString(R.string.more_action_string));
                                if (selectedFragment == null || !(selectedFragment instanceof MoreFragment)) {
                                    selectedFragment = MoreFragment.newInstance();
                                }
                                transaction.replace(R.id.frame_layout, selectedFragment);
                                transaction.commit();
                                searchViewEnabled = false;
                                break;
                        }
                        invalidateOptionsMenu();
                        return true;
                    }
                });
    }

    public void displaySearchFragment() {
        actionBar.setTitle(getResources().getString(R.string.most_common_specialists));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        SpecialistsFragment specialistsFragment = SpecialistsFragment.newInstance();
        dataBundle = new Bundle();
        dataBundle.putParcelableArrayList(DOCTORS_NODE, doctors);
        dataBundle.putParcelableArrayList(SPECIALISTS_NODE, specialists);
        specialistsFragment.setArguments(dataBundle);
        transaction.replace(R.id.frame_layout, specialistsFragment);
        transaction.commit();
        loadingLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        if (searchViewEnabled) {
            searchItem.setVisible(true);
        } else {
            searchItem.setVisible(false);
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        searchView.clearSuggestions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchView.activityResumed();
        bottomNavigationView.setSelectedItemId(selectedItemSaveState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            searchView.openSearch();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        int selectedItemId = bottomNavigationView.getMenu().findItem(bottomNavigationView.getSelectedItemId()).getItemId();
        if (searchView.isOpen()) {
            searchView.closeSearch();
        } else if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
            searchViewEnabled = true;
            invalidateOptionsMenu();
        } else if (selectedItemId == R.id.action_favourites || selectedItemId == R.id.action_more) {
            bottomNavigationView.setSelectedItemId(R.id.action_search);
        } else {
            searchViewEnabled = true;
            invalidateOptionsMenu();
            super.onBackPressed();
        }
    }

    public void initiateSearchView() {
        searchView = findViewById(R.id.search_view);
        searchView.setHint(getResources().getString(R.string.search_hint));
        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Do something when the suggestion list is clicked.
                String suggestion = searchView.getSuggestionAtPosition(position);
                searchView.setQuery(suggestion, true);
            }
        });
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchForDoctor(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }


    //get all data from firebase
    public void getData() {
        final List<String> doctorsNames = new ArrayList<>();
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
                        if (selectedCity.equals(cities[0])) {
                            doctors.add(doctor);
                        } else {
                            if (doctor.getCity().equalsIgnoreCase(selectedCity.toLowerCase())) {
                                doctors.add(doctor);
                            }
                        }
                        doctorsNames.add(doctor.getName());
                    }
                }
                displaySearchFragment();
                searchView.addSuggestions(doctorsNames);
                saveSpecialistsList();
                if (MainActivity.this.getIntent().hasExtra(SPECIALIST)) {
                    Specialist specialist = getIntent().getParcelableExtra(SPECIALIST);
                    showWidgetSpecialistDoctors(specialist);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, getString(R.string.error_occurred), Toast.LENGTH_SHORT).show();
            }
        };
        databaseReference.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (valueEventListener != null) {
            databaseReference.removeEventListener(valueEventListener);
        }
    }

    public Specialist getSpecialistByKey(final String key) {
        for (Specialist specialist : specialists) {
            if (specialist.getId().equals(key)) {
                return specialist;
            }
        }
        return null;
    }

    public String getSelectedCity() {
        int cityPosition = getApplicationContext()
                .getSharedPreferences(getResources().getString(R.string.pref_file), 0)
                .getInt(CITY, 0);
        return getResources().getStringArray(R.array.city_list)[cityPosition];
    }


    public void searchForDoctor(final String searchQuery) {
        new AsyncTask<String, Void, ArrayList<Doctor>>() {
            @Override
            protected void onPreExecute() {
                searchProgressDialog = new MaterialDialog.Builder(MainActivity.this)
                        .progress(true, 0)
                        .progressIndeterminateStyle(true)
                        .show();
            }

            @Override
            protected ArrayList<Doctor> doInBackground(String... strings) {
                String searchQuery = strings[0];
                ArrayList<Doctor> doctorsResult = new ArrayList<>();
                for (Doctor doctor : doctors) {
                    if (doctor.getName().contains(searchQuery)) {
                        doctorsResult.add(doctor);
                    }
                }
                return doctorsResult;
            }

            @Override
            protected void onPostExecute(ArrayList<Doctor> doctors) {
                searchProgressDialog.dismiss();
                showSearchResult(searchQuery, doctors);
            }
        }.execute(searchQuery);
    }

    public void showSearchResult(String searchQuery, ArrayList<Doctor> doctorsResults) {
        actionBar.setTitle(getResources().getString(R.string.search_results_str, searchQuery));
        DoctorsFragment doctorsFragment = DoctorsFragment.newInstance();
        Bundle searchResultsBundle = new Bundle();
        searchResultsBundle.putParcelableArrayList(DOCTORS_NODE, doctorsResults);
        searchResultsBundle.putBoolean("SEARCH", true);
        doctorsFragment.setArguments(searchResultsBundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, doctorsFragment).addToBackStack(getString(R.string.search_results_fragment));
        transaction.commit();
        searchViewEnabled = false;
        invalidateOptionsMenu();
    }

    public void showWidgetSpecialistDoctors(Specialist specialist) {
        //create bundle to pass specialist object to doctors fragment
        Bundle bundle = new Bundle();
        bundle.putParcelable(SPECIALIST, specialist);
        bundle.putParcelableArrayList(DOCTORS_NODE, doctors);
        //initiate DoctorsFragment
        DoctorsFragment doctorsFragment = DoctorsFragment.newInstance();
        doctorsFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, doctorsFragment).addToBackStack(getString(R.string.specialist_fragment));
        transaction.commit();
    }

    public void saveSpecialistsList() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(getString(R.string.pref_file), 0);
        SharedPreferences.Editor editor = prefs.edit();
        try {
            editor.putString(SPECIALISTS_NODE, ObjectSerializer.serialize(specialists));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.apply();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        selectedItemSaveState = bottomNavigationView.getSelectedItemId();
        outState.putInt(NAVIGATION_ID, selectedItemSaveState);
        outState.putBundle(DATA, dataBundle);
    }
}