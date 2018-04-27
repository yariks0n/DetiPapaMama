package com.webgroup.yarik.detipapamama;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        GalleryPagerAdapter galleryPagerAdapter = new GalleryPagerAdapter(
                getSupportFragmentManager(), getData());

        ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(galleryPagerAdapter);

        // mViewPager.setCurrentItem(10);
    }

    private String[] getData() {
        return Temp.getStringArray();
    }
}

