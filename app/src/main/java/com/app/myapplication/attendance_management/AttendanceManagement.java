package com.app.myapplication.attendance_management;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.app.myapplication.R;
import com.app.myapplication.attendance_management.Fragments.Adapters.PageAdapter_Attendance;
import com.google.android.material.tabs.TabLayout;

public class AttendanceManagement extends AppCompatActivity implements TabLayout.OnTabSelectedListener{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_management_activity);

        toolbar=(Toolbar)findViewById(R.id.toolbar_attendance);
        toolbar.setTitle("Records Management");
        setSupportActionBar(toolbar);

        tabLayout=findViewById(R.id.tablayout_attendance);
        tabLayout.addTab(tabLayout.newTab().setText("Attendance Dashboard"));
        tabLayout.addTab(tabLayout.newTab().setText("Set Attendance"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);

        viewPager=findViewById(R.id.viewpager_attendance);
        PageAdapter_Attendance adapter=new PageAdapter_Attendance(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        tabLayout.setOnTabSelectedListener(this);


    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
