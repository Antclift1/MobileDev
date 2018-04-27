package com.example.anthony.financialtracking;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements HomeFragment.OnFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener,
        AddFragment.OnFragmentInteractionListener{
    String username = "";
    static final int REGISTER_REQUEST_CODE = 1;
    TextView text;
    Button button;
    RecordViewModel mRecordViewModel;

    ViewPager viewPager;
    ViewPageAdapter viewPagerAdapter;

    private HomeFragment homeFragment;
    private AddFragment addFragment;
    private SettingsFragment settingsFragment;
    private static final String TAG_HOME_FRAGMENT = "home fragment";
    private static final String TAG_ADD_FRAGMENT = "add fragment";
    private static final String TAG_SETTINGS_FRAGMENT = "settings fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager= (ViewPager)findViewById(R.id.viewPager);
        if(viewPager!=null){
            viewPagerAdapter=new ViewPageAdapter(getSupportFragmentManager());
            viewPager.setAdapter(viewPagerAdapter);
        }
        android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
        homeFragment = (HomeFragment)fragmentManager.findFragmentByTag(TAG_HOME_FRAGMENT);
        addFragment = (AddFragment)fragmentManager.findFragmentByTag(TAG_ADD_FRAGMENT);
        settingsFragment = (SettingsFragment)fragmentManager.findFragmentByTag(TAG_SETTINGS_FRAGMENT);
        if (homeFragment == null && addFragment == null && settingsFragment == null) {
            homeFragment = new HomeFragment();
            addFragment = new AddFragment();
            settingsFragment = new SettingsFragment();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(homeFragment, TAG_HOME_FRAGMENT);
            fragmentTransaction.add(addFragment, TAG_ADD_FRAGMENT);
            fragmentTransaction.add(settingsFragment, TAG_SETTINGS_FRAGMENT);
            fragmentTransaction.commit();
        }
        if (savedInstanceState != null) {
            homeFragment = (HomeFragment)getSupportFragmentManager().getFragment(savedInstanceState, "homeFragment");
            addFragment = (AddFragment)getSupportFragmentManager().getFragment(savedInstanceState,"addFragment");
            settingsFragment = (SettingsFragment)getSupportFragmentManager().getFragment(savedInstanceState, "settingsFragment");
        }
      
       text = findViewById(R.id.textView2);
        mRecordViewModel = ViewModelProviders.of(this).get(RecordViewModel.class);
        mRecordViewModel.getAllRecords().observe(this, new Observer<List<Record>>() {
            @Override
            public void onChanged(@Nullable final List<Record> words) {
                // Update the cached copy of the words in the adapter.
                Record[] foo2 = new Record[10];
               Record[] foo =(Record[]) words.toArray(foo2);
               String records="";
               for(Record r: foo){
                   if(r!=null) {
                       records += "Name: " + r.getName() + " $:" + r.getAmount() + "\n";
                   }
               }
               text.setText(records);
            }
        });
    }
    
    public void startlogin(View view) {

        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivityForResult(loginIntent, REGISTER_REQUEST_CODE);
    }
         private void showEditDialog() {
            FragmentManager fm = getSupportFragmentManager();
            SubmitDialogFragment submitDialogFragment = SubmitDialogFragment.newInstance("Some Title");
            submitDialogFragment.show(fm, "fragment_edit_name");
        }



    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
