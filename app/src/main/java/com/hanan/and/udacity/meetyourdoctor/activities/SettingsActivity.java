package com.hanan.and.udacity.meetyourdoctor.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.hanan.and.udacity.meetyourdoctor.R;

import java.util.Locale;

import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.AR_LOCALE;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.CITY;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.EN_LOCALE;
import static com.hanan.and.udacity.meetyourdoctor.utilities.Constants.LANGUAGE;

public class SettingsActivity extends AppCompatActivity {

    private boolean isUserInteraction;
    private String user;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.settings));

        //initialize shared preferences
        preferences = getApplicationContext().getSharedPreferences(
                getResources().getString(R.string.pref_file), 0);
        editor = preferences.edit();

        //City Spinner Setup
        Spinner citySpinner = findViewById(R.id.choose_city_spinner);
        final ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(this, R.array.city_list, R.layout.spinner_item);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);
        citySpinner.setSelection(getCityPosition());

        //set the city
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                if (isUserInteraction) {
                    setCityValue(i);
                    restartApplication();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(SettingsActivity.this, "Nothing is selected", Toast.LENGTH_SHORT).show();
            }
        });

        //Language Spinner Setup
        final Spinner languageSpinner = findViewById(R.id.language_spinner);
        final ArrayAdapter<CharSequence> languageAdapter = ArrayAdapter.createFromResource(this, R.array.language_list, R.layout.spinner_item);
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(languageAdapter);
        languageSpinner.setSelection(getLanguagePosition());

        //set language of the application
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {
                if (isUserInteraction) {
                    final String language = (String) languageAdapter.getItem(i);
                    if (language.equals(getResources().getString(R.string.arabic))) {
                        setLocale(AR_LOCALE);
                    } else if (language.equals(getResources().getString(R.string.english))) {
                        setLocale(EN_LOCALE);
                    }
                    setLanguageValue(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(SettingsActivity.this, "Nothing is selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        isUserInteraction = true;
    }

    public void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getApplicationContext().createConfigurationContext(configuration);
        } else {
            resources.updateConfiguration(configuration, displayMetrics);
        }

        restartApplication();
    }

    public void restartApplication(){
//        Intent i = getBaseContext().getPackageManager()
//                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    public void setCityValue(int position){
        editor.putInt(CITY, position);
        editor.apply();
    }

    public void setLanguageValue(int position){
        editor.putInt(LANGUAGE, position);
        editor.apply();
    }

    public int getCityPosition(){
        return preferences.getInt(CITY, 0);
    }

    public int getLanguagePosition(){
        return preferences.getInt(LANGUAGE, 1);
    }

    public MaterialDialog.Builder confirmationDialog(){
        //create confirmation Dialog
        MaterialDialog.Builder builder = new MaterialDialog.Builder(SettingsActivity.this);
        builder.title("Change Language")
                .content("You'll change the app's language, Continue?")
                .positiveText("Agree")
                .negativeText("Dismiss");
        return builder;
    }
}
