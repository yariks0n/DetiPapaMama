package com.webgroup.yarik.detipapamama;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class NewProductService extends IntentService {
    private static final String TAG = "NewProductService";
    private static final long INTERVAL_MS = TimeUnit.MINUTES.toMillis(15);

    public static Intent newIntent(Context context) {
        return new Intent(context, NewProductService.class);
    }

    public NewProductService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (!isNetworkAvailableAndConnected()) {
            return;
        }
        String lastNewProductId = QueryPreferences.getLastNewProductId(this);
        String resultId = new Fetchr().getLastNewProductId();
        if(lastNewProductId != null){
            if(!lastNewProductId.equals(resultId)){
                Resources resources = getResources();
                Intent i = NewProductActivity.newIntent(this);
                PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
                Notification notification = new NotificationCompat.Builder(this)
                        .setTicker(resources.getString(R.string.notify_new_updates_title))
                        .setSmallIcon(R.drawable.ic_notify_new_product)
                        .setContentTitle(resources.getString(R.string.notify_new_updates_title))
                        .setContentText(resources.getString(R.string.notify_new_updates_desc))
                        .setContentIntent(pi)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setAutoCancel(true)
                        .build();
                NotificationManagerCompat notificationManager =
                        NotificationManagerCompat.from(this);
                notificationManager.notify(0, notification);
            }
        }
        QueryPreferences.setLastNewProductId(this, resultId);
    }

    private boolean isNetworkAvailableAndConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable &&
                cm.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
    }

    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent i = NewProductService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
        AlarmManager alarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);
        if (isOn) {
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(), INTERVAL_MS, pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }

    public static boolean isServiceAlarmOn(Context context) {
        Intent i = NewProductService.newIntent(context);
        PendingIntent pi = PendingIntent
                .getService(context, 0, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }


}

