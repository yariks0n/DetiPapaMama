package com.webgroup.yarik.detipapamama;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class GalleryFragment extends Fragment {

    public static final String ARG_IMG_URL = "iim_url";
    public static final String ARG_POSITION = "item_position";
    public static final String ARG_COUNT = "item_count";
    private ImageView closeBtn;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(
                R.layout.gallery_item, container, false);
        Bundle args = getArguments();


        new DownloadImageTask(((ImageView) rootView.findViewById(R.id.img))).execute(args.getString(ARG_IMG_URL));

        ((TextView) rootView.findViewById(R.id.txtCount)).setText(
                args.getInt(ARG_POSITION) + " / " + args.getInt(ARG_COUNT));

        closeBtn = (ImageView) rootView.findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.fade_out,R.anim.fade_in);
            }
        });

        return rootView;
    }
}
