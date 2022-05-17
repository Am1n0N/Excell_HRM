package com.app.myapplication.staff_management;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.app.myapplication.R;
import com.app.myapplication.staff_management.Fragments.Adapeters.PageAdapter;
import com.google.android.material.tabs.TabLayout;


public class StaffManagement extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_management_activity);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Staff Management");
        setSupportActionBar(toolbar);

        tabLayout=findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("Add Employee"));
        tabLayout.addTab(tabLayout.newTab().setText("Contracts"));
        tabLayout.addTab(tabLayout.newTab().setText("Employees"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);

        viewPager=findViewById(R.id.viewpager);
        PageAdapter adapter=new PageAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
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
