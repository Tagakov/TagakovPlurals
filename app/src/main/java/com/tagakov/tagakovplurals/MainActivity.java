package com.tagakov.tagakovplurals;

import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import com.tagakov.tagakovplurals.ui.ScaleInOutTransformer;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        getSupportActionBar().setTitle("Tagakov's test-task");

        PagerTabStrip tabStrip = (PagerTabStrip) findViewById(R.id.pager_title_strip);
        tabStrip.setDrawFullUnderline(false);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setPageTransformer(false, new ScaleInOutTransformer());
        pager.setAdapter(new SimplePagerAdapter(getSupportFragmentManager(), this));
    }

}
