package com.webgroup.yarik.detipapamama;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class Filter {

    private static final String TAG = "Filter";
    private ArrayList<CheckBox> checboxList = new ArrayList<>();
    private LinearLayout filterBlock;

    public Filter(View v){
        filterBlock = (LinearLayout) v.findViewById(R.id.filter_block);
        CheckBox cb = (CheckBox) v.findViewWithTag("section_checkbox");

        Log.i(TAG, "Checkbox "+cb.getText().toString());
    }

}
