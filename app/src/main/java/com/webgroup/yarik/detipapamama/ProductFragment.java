package com.webgroup.yarik.detipapamama;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.UUID;

public class ProductFragment extends Fragment {
    private static final String TAG = "Fragment";
    private TextView mProductName;
    private Product mProduct;
    private static final String ARG_PRODUCT_ID = "product_id";

    public static ProductFragment newInstance(String id){
        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCT_ID, id);
        ProductFragment fragment = new ProductFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String product_id = (String) getArguments().getSerializable(ARG_PRODUCT_ID);

        mProduct = new Product();
        mProduct.setId(product_id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product, container, false);

        mProductName = (TextView) v.findViewById(R.id.productName);
        mProductName.setText(mProduct.getId());

        mProductName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(),GalleryActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }

}
