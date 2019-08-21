package com.example.zhomart.oqyrman;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.zhomart.oqyrman.Fragments.LibraryFragment;
import com.example.zhomart.oqyrman.Fragments.DiscoverFragment;
import com.example.zhomart.oqyrman.Fragments.SettingsFragment;

import java.io.File;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LibraryFragment.OnFragmentInteractionListener,
        DiscoverFragment.OnFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener {

    private static final String LOCALE_KEY = "localekey";
    private static final String KAZAKH_LOCALE = "kaz";
    private static final String ENGLISH_LOCALE = "en_US";
    private static final String LOCALE_PREF_KEY = "localePref";

    FrameLayout frameLayout;
    private ActionBar toolbar;
    private String language = "en";
    private Intent intent;
    private BottomNavigationView navigation;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = getSupportActionBar();

        File outputDir = new File(getString(R.string.app_dir));
        outputDir.mkdirs();
        if (!outputDir.exists()){
            outputDir = new File(getString(R.string.app_dir_alter));
            outputDir.mkdirs();
        }

        frameLayout = (FrameLayout) findViewById(R.id.frame_container);

        intent = getIntent();
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (intent != null){
            Log.d("TEST", "Entered intent");
            language = intent.getStringExtra("language");
            if (language != null && !language.equals("")) {
                Log.d("TEST", "Entered language: " + language);

                changeLanguage();

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("main_language", language);
                editor.apply();

                finish();
                startActivity(new Intent(this, MainActivity.class));
            }
        }

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        language = preferences.getString("main_language", "en");

        changeLanguage();

        if (toolbar != null) {
            toolbar.setTitle(getResources().getString(R.string.title_library));
        }
        loadFragment(new LibraryFragment());
    }

    private void changeLanguage() {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.locale = locale;
        res.updateConfiguration(config, res.getDisplayMetrics());
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_library:
                    toolbar.setTitle(getResources().getString(R.string.title_library));
                    fragment = new LibraryFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_discover:
                    toolbar.setTitle(getResources().getString(R.string.title_discover));
                    Bundle bundle = new Bundle();
                    bundle.putString("params", language);
                    fragment = new DiscoverFragment();
                    fragment.setArguments(bundle);
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_profile:
                    toolbar.setTitle(getResources().getString(R.string.title_settings));
                    fragment = new SettingsFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
