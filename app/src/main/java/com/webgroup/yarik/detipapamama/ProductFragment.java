package com.webgroup.yarik.detipapamama;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
import java.util.UUID;

public class ProductFragment extends Fragment {
    private static final String TAG = "ProductFragment";
    private Product mProduct;
    private String productID;
    private static final String ARG_PRODUCT_ID = "product_id";
    private ThumbnailDownloader mThumbnailDownloader;

    private ConstraintLayout productBlock;
    private ProgressBar detailDownload;
    private TextView mProductName;
    private ImageView mProductImage;

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
        productID = product_id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product, container, false);

        productBlock = (ConstraintLayout) v.findViewById(R.id.detailProductBlock);
        detailDownload = (ProgressBar) v.findViewById(R.id.detailDownload);

        mProductName = (TextView) v.findViewById(R.id.productName);
        mProductName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),GalleryActivity.class);
                startActivity(intent);
            }
        });

        mProductImage = (ImageView) v.findViewById(R.id.productImg);

        showDownloadProduct();
        new FetchTask().execute();
        return v;
    }

    private void showDownloadProduct(){
        if(productBlock != null)
            productBlock.setVisibility(View.INVISIBLE);

        if(detailDownload != null)
            detailDownload.setVisibility(View.VISIBLE);

    }

    private void hideDownloadProduct(){
        if(productBlock != null)
            productBlock.setVisibility(View.VISIBLE);

        if(detailDownload != null)
            detailDownload.setVisibility(View.INVISIBLE);
    }


    private class FetchTask extends AsyncTask<Void,Void,Product> {

        @Override
        protected Product doInBackground(Void... params) {
            return new Fetchr().fetchProductDetail(productID);
        }

        @Override
        protected void onPostExecute(Product item) {
            setProductInfo(item);
        }
    }

    private void setProductInfo(Product item){
        mProduct = item;

        if(mProduct != null){
            if(mProduct.getId() != null) {

                mProductName.setText(mProduct.getName());
                new DownloadImageTask(mProductImage).execute(mProduct.getImgUrl());

                hideDownloadProduct();
            }
        }

    }

}
