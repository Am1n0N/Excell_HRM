package com.app.myapplication.attendance_management.Fragments.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app.myapplication.attendance_management.Fragments.Dashboard_AttendanceFragment;
import com.app.myapplication.attendance_management.Fragments.Set_AttendanceFragment;

public class PageAdapter_Attendance extends FragmentPagerAdapter {
    private int numCount;

    public PageAdapter_Attendance(@NonNull FragmentManager fm, int numCount) {
        super(fm);
        this.numCount = numCount;

    }

    public PageAdapter_Attendance(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                Dashboard_AttendanceFragment dashboard=new Dashboard_AttendanceFragment();
                return dashboard;
            case 1:
                Set_AttendanceFragment setAttendanceFragment=new Set_AttendanceFragment();
                return setAttendanceFragment;

        }
        return null;
    }

    @Override
    public int getCount() {
        return numCount;
    }


}