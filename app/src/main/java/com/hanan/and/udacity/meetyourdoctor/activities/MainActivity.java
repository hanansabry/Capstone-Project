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

import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.NOT_SIGNED;

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
        //------------------------------------------------------------------------------------------
        //initiate the SearchView
        initiateSearchView();
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
                                transaction.replace(R.id.frame_layout, selectedFragment);
                                transaction.commit();
                                searchView.setVisibility(View.VISIBLE);
                                searchViewEnabled = true;
                                searchView.setSuggestions(getResources().getStringArray(R.array.specialists_suggestions));
                                break;
                            case R.id.action_favourites:
                                if(!login){
                                    finish();
                                    //start login activity
                                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                    intent.putExtra(NOT_SIGNED, true);
                                    startActivity(intent);
                                }else {
                                    actionBar.setTitle(getResources().getString(R.string.favourites));
                                    selectedFragment = DoctorsFragment.newInstance();
                                    transaction.replace(R.id.frame_layout, selectedFragment);
                                    transaction.commit();
                                    searchView.setVisibility(View.VISIBLE);
                                    searchViewEnabled = true;
                                    searchView.setSuggestions(getResources().getStringArray(R.array.doctors_suggestions));
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
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, SearchFragment.newInstance());
        transaction.commit();
        actionBar.setTitle(getResources().getString(R.string.most_common_specialists));
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
            }

            @Override
            public void onSearchViewClosed() {
                Toast.makeText(MainActivity.this, "SearchView is closed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}