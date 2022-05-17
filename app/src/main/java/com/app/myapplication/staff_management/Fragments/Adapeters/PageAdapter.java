package com.app.myapplication.staff_management.Fragments.Adapeters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app.myapplication.staff_management.Fragments.Add_employeeFragment;
import com.app.myapplication.staff_management.Fragments.ContractsFragment;
import com.app.myapplication.staff_management.Fragments.EmployeesFragment;

public class PageAdapter extends FragmentPagerAdapter {
    private int numCount;

    public PageAdapter(@NonNull FragmentManager fm, int numCount) {
        super(fm);
        this.numCount = numCount;

    }

    public PageAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                Add_employeeFragment Add=new Add_employeeFragment();
                return Add;
            case 1:
                ContractsFragment contracts=new ContractsFragment();
                return contracts;
            case 2:
                EmployeesFragment employee=new EmployeesFragment();
                return employee;
        }
        return null;
    }

    @Override
    public int getCount() {
        return numCount;
    }


}
