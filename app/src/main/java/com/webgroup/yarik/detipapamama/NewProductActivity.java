package com.webgroup.yarik.detipapamama;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NewProductActivity extends AppCompatActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context, NewProductActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QueryPreferences.clearStoredSections(this);
        QueryPreferences.setSortQuery(this,"sort_new");
        Intent i = MainActivity.newIntent(this);
        startActivity(i);
        finish();
    }
}
