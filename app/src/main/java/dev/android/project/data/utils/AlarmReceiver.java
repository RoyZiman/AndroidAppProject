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

import dev.android.project.MainActivity;
import dev.android.project.R;
import dev.android.project.data.models.User;
import dev.android.project.data.providers.DBNotificationManager;
import dev.android.project.data.providers.DBProductsManager;
import dev.android.project.data.providers.DBStorageManager;

/**
 * BroadcastReceiver that handles scheduled alarms and displays notifications to the user.
 * <p>
 * Provides methods to schedule an alarm and handle the broadcast when the alarm is triggered.
 */
public class AlarmReceiver extends BroadcastReceiver
{
    /**
     * Schedules a notification to be triggered after a specified delay.
     *
     * @param context The application context.
     * @param triggerDelayS The delay in seconds after which the notification should be triggered. Please note
     *         that the delay is not precise because AlarmManager.set() is used and does not guarantee exact timing.
     */
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

    /**
     * Called when the scheduled alarm is received.
     * <p>
     * Checks if the user is logged in, creates a notification channel if necessary, builds a notification, and displays
     * it if there are unread notifications.
     *
     * @param context The application context.
     * @param intent The received intent.
     */
    @Override
    public void onReceive(Context context, Intent intent)
    {

        Log.i("AlarmReceiver", "Alarm received! Trying to send notification");

        if (!User.isLoggedIn())
            return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(
                    "alarm_channel", "Alarms", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context,
                "alarm_channel")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.ic_notification);


        Intent activityIntent =
                new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                activityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent).setAutoCancel(true);


        DBNotificationManager.getUnreadNotificationsForUser().addOnSuccessListener(
                messages -> {

                    if (messages == null || messages.isEmpty())
                        return;

                    builder.setContentTitle("You Have " + messages.size() + " Unread Notifications");


                    String productId = messages.get(0).getProductId();

                    DBProductsManager.getProduct(productId).addOnSuccessListener(
                            product ->
                            {
                                builder.setContentText("New Buy Offer for " + product.getTitle());

                                DBStorageManager.getProductPreview(productId)
                                                .addOnSuccessListener(bitmap -> {
                                                    builder.setLargeIcon(bitmap);
                                                    sendNotification(context, builder);
                                                });
                            });
                });
    }

    /**
     * Sends the built notification using the NotificationManager.
     *
     * @param context The application context.
     * @param builder The notification builder containing notification details.
     */
    private static void sendNotification(Context context, NotificationCompat.Builder builder)
    {

        NotificationManager notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1001, builder.build());
    }

}
