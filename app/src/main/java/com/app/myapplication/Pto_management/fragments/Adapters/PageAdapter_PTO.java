package com.app.myapplication.Pto_management.fragments.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app.myapplication.Pto_management.fragments.Add_PtoFragment;
import com.app.myapplication.Pto_management.fragments.PTOs_fragments;
import com.app.myapplication.Pto_management.fragments.Pto_RequestsFragment;

public class PageAdapter_PTO extends FragmentPagerAdapter {
    private int numCount;

    public PageAdapter_PTO(@NonNull FragmentManager fm, int numCount) {
        super(fm);
        this.numCount = numCount;

    }

    public PageAdapter_PTO(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                Add_PtoFragment add_PtoFragment=new Add_PtoFragment();
                return add_PtoFragment;
            case 1:
                Pto_RequestsFragment pto_RequestsFragment=new Pto_RequestsFragment();
                return pto_RequestsFragment;
            case 2:
                PTOs_fragments pTOs_fragments=new PTOs_fragments();
                return pTOs_fragments;

        }
        return null;
    }

    @Override
    public int getCount() {
        return numCount;
    }


}