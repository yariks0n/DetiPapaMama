package com.webgroup.yarik.detipapamama;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context, CatalogFragment.class);
    }

    @Override
    protected Fragment createFragment() {
        return CatalogFragment.newInstance();
    }

}
