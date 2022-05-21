package com.app.myapplication.Pto_management;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.app.myapplication.R;
import com.app.myapplication.Record_management.Fragments.Adapters.PageAdapter_record;
import com.google.android.material.tabs.TabLayout;

public class PtoManagement extends AppCompatActivity implements TabLayout.OnTabSelectedListener{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pto_management_activity);

        toolbar=(Toolbar)findViewById(R.id.toolbar_PTO);
        toolbar.setTitle("Manage PTOs");
        setSupportActionBar(toolbar);

        tabLayout=findViewById(R.id.tablayout_PTO);
        tabLayout.addTab(tabLayout.newTab().setText("Add PTO"));
        tabLayout.addTab(tabLayout.newTab().setText("PTO requests"));
        tabLayout.addTab(tabLayout.newTab().setText("PTOs"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);

        viewPager=findViewById(R.id.viewpager_PTO);
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
