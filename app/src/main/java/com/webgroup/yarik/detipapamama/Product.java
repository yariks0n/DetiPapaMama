package com.webgroup.yarik.detipapamama;

import android.content.Context;

public class Product {

    private String mName;
    private String mPrice;
    private String mOldPrice;
    private int mId;
    private String mUrl;
    private String imgUrl;

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getPrice() {
        return mPrice;
    }

    public String getPriceFormat() {
        if(mPrice == null)
            return "";
        return mPrice+" "+Settings.getForamtRubl();
    }

    public void setPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    public String getOldPrice() {
        return mOldPrice;
    }

    public String getOldPriceFormat() {
        if(mOldPrice == null)
            return "";
        return mOldPrice+" "+Settings.getForamtRubl();
    }

    public void setOldPrice(String mOldPrice) {
        this.mOldPrice = mOldPrice;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
