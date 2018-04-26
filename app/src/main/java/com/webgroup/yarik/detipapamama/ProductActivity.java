package com.webgroup.yarik.detipapamama;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;


public class ProductActivity extends SingleFragmentActivity {

    private static final String TAG = "ProductActivity";
    private static final String EXTRA_PRODUCT_ID =
            "com.webgroup.yarik.detipapamama.product_id";

    @Override
    protected Fragment createFragment() {
        String product_id = (String) getIntent()
                .getSerializableExtra(EXTRA_PRODUCT_ID);
        Log.i(TAG,"ProductActivity createFragment");
        return ProductFragment.newInstance(product_id);
    }


    public static Intent newIntent(Context packageContext, String product_id) {
        Intent intent = new Intent(packageContext, ProductActivity.class);
        intent.putExtra(EXTRA_PRODUCT_ID, product_id);
        Log.i(TAG,"ProductActivity newIntent");
        return intent;
    }

}