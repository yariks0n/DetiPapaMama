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

        TextPagerAdapter mTextPagerAdapter = new TextPagerAdapter(
                getSupportFragmentManager(), getData());

        ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mTextPagerAdapter);

        // mViewPager.setCurrentItem(10);
    }

    private List<String> getData() {
        List<String> data = new ArrayList<>();
        for (int i = 1; i < 30; i++) {
            data.add("Item number " + i);
        }

        return data;
    }
}

