package dev.android.project.data.utils;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import dev.android.project.R;

public class AlarmReceiver extends BroadcastReceiver
{
    public static void scheduleNotification(Context context, long triggerDelayS)
    {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        // Create an intent to trigger the AlarmReceiver
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Schedule the alarm
        long triggerAt = System.currentTimeMillis() + triggerDelayS * 1000;
        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent);
        Log.i("AlarmReceiver", "Alarm scheduled to trigger in about " + triggerDelayS + " seconds.");
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(
                    "alarm_channel", "Alarms", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        // Build notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "alarm_channel")
                .setContentTitle("Alarm!")
                .setContentText("This is your scheduled notification.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_fab_add_image);


//        Intent activityIntent = new Intent(context, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(
//                context, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
//        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1001, builder.build());

        Log.i("AlarmReceiver", "Alarm received! Triggering notification...");
    }
}