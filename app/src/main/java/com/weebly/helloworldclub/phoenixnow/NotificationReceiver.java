package com.weebly.helloworldclub.phoenixnow;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;

/**
 * Created by nairv on 8/30/2016.
 */
public class NotificationReceiver {
// extends BroadcastReceiver {
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        NotificationManager manager = (NotificationManager)
//                context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        Intent repeating_intent = new Intent(context, MainActivity.class);
//        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, repeating_intent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//        builder.setContentIntent(pendingIntent);
//        builder.setContentTitle("PhoenixNow");
//        builder.setContentText("Don't forget to check-in today!");
//        builder.setTicker("PhoenixNow");
//        builder.setSmallIcon(R.drawable.officiallogo);
//        builder.setAutoCancel(true);
//
//        if (!new Memory().getCheckedInStatus()) {
//            Calendar c = Calendar.getInstance();
//            int minute = c.get(Calendar.MINUTE);
//            int hour = c.get(Calendar.HOUR);
//            if (minute == new Memory().getNotificationMinute() && hour == new Memory().getNotificationHour()) {
//                manager.notify(100, builder.build());
//                new Memory().setCheckedInStatus(false);
//            }
//        } else {
//            manager.cancel(100);
//        }
//
//    }
}
