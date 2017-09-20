package com.tabian.tabfragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import Adapter.SectionsPageAdapter;

public class MainActivity extends AppCompatActivity implements Tab2Fragment.OnSelectedButtonListener
{
    SectionsPageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager)
    {
        adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab1Fragment(), "Weather");
        adapter.addFragment(new Tab2Fragment(), "City");
        viewPager.setAdapter(adapter);
    }


    @Override
    public void transitContent(String str)
    {
        Tab1Fragment tab1Fragment = (Tab1Fragment) adapter.getItem(0);
        if (tab1Fragment!=null)
        {
               tab1Fragment.setCityName(str);
        }
    }
}
