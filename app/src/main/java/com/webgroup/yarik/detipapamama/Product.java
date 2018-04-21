package com.webgroup.yarik.detipapamama;

import android.graphics.Bitmap;

public class Product {

    private String mName;
    private String mPrice;
    private String mOldPrice;
    private int mId;
    private Bitmap mImg;

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmPrice() {
        return mPrice;
    }

    public void setmPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    public String getmOldPrice() {
        return mOldPrice;
    }

    public void setmOldPrice(String mOldPrice) {
        this.mOldPrice = mOldPrice;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public Bitmap getmImg() {
        return mImg;
    }

    public void setmImg(Bitmap mImg) {
        this.mImg = mImg;
    }
}
