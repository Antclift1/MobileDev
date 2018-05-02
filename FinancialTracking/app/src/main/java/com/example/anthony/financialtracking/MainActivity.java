package com.example.anthony.financialtracking;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends AppCompatActivity
        implements HomeFragment.OnFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener,
        SubmitRecordFragment.OnFragmentInteractionListener{
    String username = "";
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    static final int REGISTER_REQUEST_CODE = 1;

    RecordViewModel mRecordViewModel;

    //Stuff for the multiple pages on main screen
    ViewPager viewPager;
    ViewPageAdapter viewPagerAdapter;

    //The 3 fragments on the main activity, and their names
    private HomeFragment homeFragment;
    private SubmitRecordFragment submitRecordFragment;
    private SettingsFragment settingsFragment;
    private static final String TAG_HOME_FRAGMENT = "home fragment";
    private static final String TAG_ADD_FRAGMENT = "add fragment";
    private static final String TAG_SETTINGS_FRAGMENT = "settings fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Makes the view pager and sets adapter
        viewPager= (ViewPager)findViewById(R.id.viewPager);
        if(viewPager!=null){
            viewPagerAdapter=new ViewPageAdapter(getSupportFragmentManager());
            viewPager.setAdapter(viewPagerAdapter);
        }
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        username = prefs.getString("username", "");
        if(username == ""){
            //startlogin();
        }
        else {
            Toast.makeText(this, "Welcome " + username, Toast.LENGTH_SHORT).show();
        }
        //Finds the fragments from the manager and sets the variables
        android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
        homeFragment = (HomeFragment)fragmentManager.findFragmentByTag(TAG_HOME_FRAGMENT);
        submitRecordFragment = (SubmitRecordFragment)fragmentManager.findFragmentByTag(TAG_ADD_FRAGMENT);
        settingsFragment = (SettingsFragment)fragmentManager.findFragmentByTag(TAG_SETTINGS_FRAGMENT);
        //TODO should these be &&'s? what if only 1 is null for some reason
        //creates the fragments, and makes the transactions, used for??
        if (homeFragment == null && submitRecordFragment == null && settingsFragment == null) {
            homeFragment = new HomeFragment();
            submitRecordFragment = new SubmitRecordFragment();
            settingsFragment = new SettingsFragment();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(homeFragment, TAG_HOME_FRAGMENT);
            fragmentTransaction.add(submitRecordFragment, TAG_ADD_FRAGMENT);
            fragmentTransaction.add(settingsFragment, TAG_SETTINGS_FRAGMENT);
            fragmentTransaction.commit();
        }
        if (savedInstanceState != null) {
            homeFragment = (HomeFragment)getSupportFragmentManager().getFragment(savedInstanceState, "homeFragment");
            submitRecordFragment = (SubmitRecordFragment)getSupportFragmentManager().getFragment(savedInstanceState,"submitRecordFragment");
            settingsFragment = (SettingsFragment)getSupportFragmentManager().getFragment(savedInstanceState, "settingsFragment");
        }
        viewPager.setOffscreenPageLimit(2);
        // Sets up a tabLayout to navigate between screens
        TabLayout tabLayout = new TabLayout(this);
        tabLayout.setupWithViewPager(viewPager);
        //gets the view model which stores info independently of the view lifecycle
        mRecordViewModel = ViewModelProviders.of(this).get(RecordViewModel.class);

    }
    
    public void startlogin() {

        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivityForResult(loginIntent, REGISTER_REQUEST_CODE);
    }

    public String getLogin() {
        return username;
    }

    public void logout(View view) {
        username = "";
        SharedPreferences settings = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        settings.edit().putString("username", "");
        startlogin();
    }

    public void updateBudget(View view) {

        Intent budgetIntent = new Intent(this, BudgetUpdateActivity.class);
        budgetIntent.putExtra("Username", username);
        startActivity(budgetIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REGISTER_REQUEST_CODE && resultCode == RESULT_OK) {
            username = data.getExtras().getString("Username");
            SharedPreferences settings = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            settings.edit().putString("username", username);
        }
        Toast.makeText(this, username, Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
