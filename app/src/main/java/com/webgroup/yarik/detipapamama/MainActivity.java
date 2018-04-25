package com.webgroup.yarik.detipapamama;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return CatalogFragment.newInstance();
    }

}
