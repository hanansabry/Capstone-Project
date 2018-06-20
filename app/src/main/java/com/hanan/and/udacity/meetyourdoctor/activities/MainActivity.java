package com.hanan.and.udacity.meetyourdoctor.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.hanan.and.udacity.meetyourdoctor.R;
import com.hanan.and.udacity.meetyourdoctor.fragments.DoctorsFragment;
import com.hanan.and.udacity.meetyourdoctor.fragments.MoreFragment;
import com.hanan.and.udacity.meetyourdoctor.fragments.SearchFragment;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.FAVOURITE;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.MORE;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.MOST_COMMON_SPECIALISTS;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.SIGNIN;

public class MainActivity extends AppCompatActivity {

    private MaterialSearchView searchView;
    private CollapsingToolbarLayout collapsingToolbar;
    private Toolbar toolbar;
    private boolean searchViewEnabled = true;
    private ActionBar actionBar;
    private boolean login = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //------------------------------------------------------------------------------------------
        //setup the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
//        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
//        initCollapsingToolbar();
//        ------------------------------------------------------------------------------------------
        //initiate the SearchView
        initiateSearchView();
        //------------------------------------------------------------------------------------------
        //setup the Bottom Navigation View
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_search:
                                actionBar.setTitle(MOST_COMMON_SPECIALISTS);
                                selectedFragment = SearchFragment.newInstance();
                                searchView.setVisibility(View.VISIBLE);
                                searchViewEnabled = true;
                                searchView.setSuggestions(getResources().getStringArray(R.array.specialists_suggestions));
                                invalidateOptionsMenu();
                                break;
                            case R.id.action_favourites:
                                if(login){
                                    //start login activity
                                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                }else {
                                    actionBar.setTitle(FAVOURITE);
                                    selectedFragment = DoctorsFragment.newInstance();
                                    ((DoctorsFragment) selectedFragment).setDoctorsList(getDoctorsFavouriteList());
                                    searchView.setVisibility(View.VISIBLE);
                                    searchViewEnabled = true;
                                    searchView.setSuggestions(getResources().getStringArray(R.array.doctors_suggestions));
                                    invalidateOptionsMenu();
                                }
                                break;
                            case R.id.action_more:
                                actionBar.setTitle(MORE);
                                selectedFragment = MoreFragment.newInstance();
                                searchView.setVisibility(View.INVISIBLE);
                                searchViewEnabled = false;
                                invalidateOptionsMenu();
                                break;
                        }
                        if(!login) {
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.frame_layout, selectedFragment);
                            transaction.commit();
                        }
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, SearchFragment.newInstance());
        transaction.commit();
        actionBar.setTitle(MOST_COMMON_SPECIALISTS);
        //Used to select an item programmatically
        //bottomNavigationView.getMenu().getItem(2).setChecked(true);
        //------------------------------------------------------------------------------------------

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView.setMenuItem(searchItem);

        if(searchViewEnabled){
            searchItem.setVisible(true);
        }else{
            searchItem.setVisible(false);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if(searchView.isSearchOpen()){
            searchView.closeSearch();
        }else if(getFragmentManager().getBackStackEntryCount() > 0){
            getFragmentManager().popBackStack();
        }else{
            super.onBackPressed();
        }
    }

    public void initiateSearchView(){
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
//                collapsingToolbar.setCollapsedTitleTextColor(Color.TRANSPARENT);
            }

            @Override
            public void onSearchViewClosed() {
                Toast.makeText(MainActivity.this, "SearchView is closed", Toast.LENGTH_SHORT).show();
//                collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
            }
        });
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    //fake doctors list
    public List<String> getDoctorsFavouriteList(){
        List<String> doctors = new ArrayList();
        doctors.add("Amany Sabry");
        doctors.add("Amany Sabry");
        return doctors;
    }
}