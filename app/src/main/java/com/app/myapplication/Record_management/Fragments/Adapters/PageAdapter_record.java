package com.app.myapplication.Record_management.Fragments.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app.myapplication.Record_management.Fragments.Add_EntryFragment;
import com.app.myapplication.Record_management.Fragments.Entries_DashboardFragment;
import com.app.myapplication.Record_management.Fragments.RequestFragment;

public class PageAdapter_record extends FragmentPagerAdapter {
    private int numCount;

    public PageAdapter_record(@NonNull FragmentManager fm, int numCount) {
        super(fm);
        this.numCount = numCount;

    }

    public PageAdapter_record(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                Add_EntryFragment add_employeeFragment=new Add_EntryFragment();
                return add_employeeFragment;
            case 1:
                Entries_DashboardFragment dashboardFragment=new Entries_DashboardFragment();
                return dashboardFragment;
            case 2:
                RequestFragment requestFragment=new RequestFragment();
                return requestFragment;

        }
        return null;
    }

    @Override
    public int getCount() {
        return numCount;
    }


}