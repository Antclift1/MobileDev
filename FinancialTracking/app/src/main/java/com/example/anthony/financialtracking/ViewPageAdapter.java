package com.example.anthony.financialtracking;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPageAdapter extends FragmentPagerAdapter {

    public ViewPageAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public CharSequence getPageTitle(int position){
        switch(position){
            case 0:
                return "Home";
            case 1:
                return "Add";
            case 2:
                return "Settings";
            default:
                return super.getPageTitle(position);
        }
    }
    @Override
    public Fragment getItem(int position) {
        if (position==0){
            HomeFragment homeFragment = new HomeFragment();
            return homeFragment;
        }
        else if (position==1){
            AddFragment addFragment = new AddFragment();
            return addFragment;
        }
        else{
            SettingsFragment settingsFragment = new SettingsFragment();
            return settingsFragment;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
