package com.webgroup.yarik.detipapamama;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;

public class Settings {
    public static Context context;
    public static Activity activity;
    public static String getForamtRubl(){
        return context.getString(R.string.rub);
    }

    public static Point getScreenSize(){
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }
}
