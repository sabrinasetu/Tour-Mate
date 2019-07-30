package com.example.maruf.tourMateApplication.Adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.example.maruf.tourMateApplication.Fragments.ExpensesFragment;
import com.example.maruf.tourMateApplication.Fragments.MemorablePlacesFragment;


public class EventPagerAdapter extends FragmentPagerAdapter {
    private int numOfTab;
    Bundle bundle;
    public EventPagerAdapter(FragmentManager fm, int numOfTab, Bundle bundle) {
        super(fm);
        this.numOfTab = numOfTab;
        this.bundle = bundle;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case  0:
                Fragment expensesFragment = new ExpensesFragment();
                expensesFragment.setArguments(bundle);
                return  expensesFragment;

            case 1:
                Fragment memorablePlacesFragment = new MemorablePlacesFragment();
                memorablePlacesFragment.setArguments(bundle);
                return  memorablePlacesFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTab;
    }
}
