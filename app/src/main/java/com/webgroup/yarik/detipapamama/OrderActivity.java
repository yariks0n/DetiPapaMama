package com.webgroup.yarik.detipapamama;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class OrderActivity extends SingleFragmentActivity {

    private static final String TAG = "OrderActivity";
    private static final String EXTRA_PRODUCT_ID =
            "com.webgroup.yarik.detipapamama.product_id";

    @Override
    protected Fragment createFragment() {
        String product_id = (String) getIntent()
                .getSerializableExtra(EXTRA_PRODUCT_ID);
        return OrderFragment.newInstance(product_id);
    }

    public static Intent newIntent(Context packageContext, String product_id) {
        Intent intent = new Intent(packageContext, OrderActivity.class);
        intent.putExtra(EXTRA_PRODUCT_ID, product_id);
        return intent;
    }

    @Override
    public void onBackPressed(){
        finish();
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

}
