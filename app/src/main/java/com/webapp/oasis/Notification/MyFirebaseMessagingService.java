package com.webapp.oasis.Notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.webapp.oasis.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    // Define a notification channel ID and name (required for Android 8.0 and above)
    private static final String CHANNEL_ID = "my_app_notifications"; // Use a unique ID for your app
    private static final String CHANNEL_NAME = "My App Notifications"; // Provide a user-friendly name
    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d("MyFirebaseMessaging", "New FCM Token: " + token);
        sendRegistrationToServer(token);
    }
    public static void sendMessage(String recipientToken, String message) {
        RemoteMessage.Builder messageBuilder = new RemoteMessage.Builder(recipientToken);
        messageBuilder.setMessageId(Integer.toString(124));
        messageBuilder.addData("message", message);

        FirebaseMessaging.getInstance().send(messageBuilder.build());
    }
    private void sendRegistrationToServer(String token) {
        // Implement logic to send the registration token to your server if needed.
        Log.d("HIteshToken", "sendRegistrationToServer: " + token+"   end");
        // You can use an HTTP request or any other method to send the token.
    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle incoming notifications here
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            // Create a notification channel if the Android version is Oreo or above
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                channel.enableLights(true);
                channel.setLightColor(Color.GREEN);
                channel.enableVibration(true);
                channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});

                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }

            // Build the notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable. ic_plusicon)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            // Show the notification
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
            notificationManagerCompat.notify(/*notification_id*/ 1, builder.build());
        }
    }
}
