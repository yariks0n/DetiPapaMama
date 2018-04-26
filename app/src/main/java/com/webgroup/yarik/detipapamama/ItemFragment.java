package com.webgroup.yarik.detipapamama;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ItemFragment extends Fragment {

    public static final String ARG_TEXT = "item_text";
    public static final String ARG_POSITION = "item_position";
    public static final String ARG_COUNT = "item_count";

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(
                R.layout.pager_item, container, false);
        Bundle args = getArguments();

        ((TextView) rootView.findViewById(R.id.text1)).setText(
                args.getString(ARG_TEXT));

        ((TextView) rootView.findViewById(R.id.txtCount)).setText(
                args.getInt(ARG_POSITION) + " / " + args.getInt(ARG_COUNT));

        return rootView;
    }
}
