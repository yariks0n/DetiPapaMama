package com.webgroup.yarik.detipapamama;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class GalleryPagerAdapter extends FragmentStatePagerAdapter {

    String[] data;

    public GalleryPagerAdapter(FragmentManager fm, String[] data) {
        super(fm);
        this.data = data;
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new GalleryFragment();

        Bundle args = new Bundle();
        args.putString(GalleryFragment.ARG_IMG_URL, data[i]);
        args.putInt(GalleryFragment.ARG_POSITION, i+1);
        args.putInt(GalleryFragment.ARG_COUNT, getCount());

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Img " + (position + 1);
    }
}