package com.webgroup.yarik.detipapamama;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Sections {

    private List<Section> mSections;

    private static Sections sSections;

    public static Sections get(Context context) {
        if (sSections == null) {
            sSections = new Sections(context);
        }
        return sSections;
    }

    private Sections(Context context) {
        mSections = new ArrayList<>();
        for(int i = 1; i < 5; i++){
            Section section = new Section();
            section.setName("Section "+i);
            mSections.add(section);
        }

        Log.i("Sections","Size "+mSections.size());
    }

    public List<Section> getSections(){
        return mSections;
    }

}
