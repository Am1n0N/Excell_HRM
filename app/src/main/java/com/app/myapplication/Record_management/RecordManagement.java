package com.app.myapplication.Record_management;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.app.myapplication.R;
import com.app.myapplication.Record_management.Fragments.Adapters.PageAdapter_record;
import com.google.android.material.tabs.TabLayout;

public class RecordManagement extends AppCompatActivity implements TabLayout.OnTabSelectedListener{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_management_activity);

        toolbar=(Toolbar)findViewById(R.id.toolbar_record);
        toolbar.setTitle("Manage Records");
        setSupportActionBar(toolbar);

        tabLayout=findViewById(R.id.tablayout_record);
        tabLayout.addTab(tabLayout.newTab().setText("Add Entry"));
        tabLayout.addTab(tabLayout.newTab().setText("Entries Dashboard"));
        tabLayout.addTab(tabLayout.newTab().setText("Requests Dashboard "));
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);

        viewPager=findViewById(R.id.viewpager_record);
        PageAdapter_record adapter=new PageAdapter_record(getSupportFragmentManager(),tabLayout.getTabCount());
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
