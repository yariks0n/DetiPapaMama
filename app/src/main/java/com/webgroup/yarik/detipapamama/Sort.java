package com.webgroup.yarik.detipapamama;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class Sort {

    private static final String TAG = "Sort";
    private Context mContext;
    private CatalogFragment mCatalogFragment;
    private TextView sort_popular, sort_new, sort_price_desc, sort_price_asc;

    private TextView[] sortItems;
    private View parentView;

    public Sort(CatalogFragment c, View v){
        mContext = v.getContext();
        mCatalogFragment = c;
        parentView = v;

        sort_popular = (TextView) parentView.findViewById(R.id.sort_popular);
        sort_popular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSort(v);
            }
        });

        sort_new = (TextView) parentView.findViewById(R.id.sort_new);
        sort_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSort(v);
            }
        });

        sort_price_desc = (TextView) parentView.findViewById(R.id.sort_price_desc);
        sort_price_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSort(v);
            }
        });

        sort_price_asc = (TextView) parentView.findViewById(R.id.sort_price_asc);
        sort_price_asc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSort(v);
            }
        });
    }

    public void setSort(View v){
        updateSortItems();
        String val = v.getTag().toString();
        v.setBackgroundColor(Color.parseColor("#F5F5F5"));
        QueryPreferences.setSortQuery(mContext,val);
        mCatalogFragment.reInitProductList();
        mCatalogFragment.updateItems();
        mCatalogFragment.mSortBlockHide();
    }

    public void initSortItems(){
        sort_popular = (TextView) parentView.findViewById(R.id.sort_popular);
        sortItems[0] = sort_popular;
        sort_new = (TextView) parentView.findViewById(R.id.sort_new);
        sortItems[1] = sort_new;
        for(TextView sort: sortItems){
            sort.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String val = v.getTag().toString();
                    updateSortItems();
                }
            });
        }
        updateSortItems();
    }

    private void updateSortItems() {
        sort_popular.setBackgroundColor(Color.WHITE);
        sort_new.setBackgroundColor(Color.WHITE);
        sort_price_asc.setBackgroundColor(Color.WHITE);
        sort_price_desc.setBackgroundColor(Color.WHITE);
    }

}
