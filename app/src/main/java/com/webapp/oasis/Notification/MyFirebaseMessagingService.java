package com.webapp.oasis.Notification;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import com.webapp.oasis.Admin.AdmiinHomeScreen;
import com.webapp.oasis.LoginActivity;
import com.webapp.oasis.R;
import com.webapp.oasis.Utilities.SessionManager;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "101";
    private static final String CHANNEL_NAME = "Bestmarts";
    private final int ADMIN_CHANNEL_ID =102;
    SessionManager sessionManager;
    int count;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        sessionManager=new SessionManager(getApplicationContext());
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            sendNotification(remoteMessage);
        }else{
            sendNotification(remoteMessage);
        }

    }
    public MyFirebaseMessagingService() {
        super();
    }
    @SuppressLint("LongLogTag")
    private void sendNotification(RemoteMessage remoteMessage) {

        if (!isAppIsInBackground(getApplicationContext())) {
            //foreground app

            Log.e("remoteMessage foreground", remoteMessage.getData().toString());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                Log.e("remoteMessage foreground123",json.getString("title"));

                String title = json.getString("title");
                String message = json.getString("message");

                Intent notificationIntent = new Intent(getApplicationContext(), LoginActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                        notificationIntent, 0);


                NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

                NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                inboxStyle.addLine(message);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
                notificationBuilder.setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.appiconlogo)
                        .setNumber(10)
                        .setStyle(inboxStyle)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.appiconlogo)
                        .setContentTitle(title)
                        .setContentText(message);
                notificationManager.notify(0, notificationBuilder.build());


            } catch (JSONException e) {
                e.printStackTrace();
            }
            /*String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();*/

        }else{
            Log.e("remoteMessage background", remoteMessage.getData().toString());
          /*  String title = data.get("title");
            String body = data.get("body");*/
            JSONObject json = null;
            try {
                json = new JSONObject(remoteMessage.getData().toString());

                String title = json.getString("title");
                String message = json.getString("message");

                Intent notificationIntent = new Intent(getApplicationContext(), AdmiinHomeScreen.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                        notificationIntent, 0);

                NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

                NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                inboxStyle.addLine(message);


                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
                notificationBuilder.setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.appiconlogo)
                        .setNumber(10)
                        .setStyle(inboxStyle)
                        .setSmallIcon(R.drawable.appiconlogo)
                        .setContentTitle(title)
                        .setContentIntent(pendingIntent)
                        .setContentText(message);
                notificationManager.notify(0, notificationBuilder.build());


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.Q)
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    @SuppressLint("NewApi")
    private void sendNotification1(RemoteMessage remoteMessage) {
        if (!isAppIsInBackground(getApplicationContext())) {
            //foreground app
            Log.e("remoteMessage", remoteMessage.getData().toString());
            /*String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();*/
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());

                String title = json.getString("title");
                String message = json.getString("message");

                Intent notificationIntent = new Intent(getApplicationContext(), AdmiinHomeScreen.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                        notificationIntent, 0);

                Uri defaultsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                OreoNotification oreoNotification = new OreoNotification(this);
                Notification.Builder builder = oreoNotification.getOreoNotification(title, message, pendingIntent, defaultsound, String.valueOf(R.mipmap.ic_launcher));

                int i = 0;
                oreoNotification.getManager().notify(i, builder.build());


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else{
            Log.e("remoteMessage", remoteMessage.getData().toString());
           /* String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("body");*/
            JSONObject json = null;

            try {
                json = new JSONObject(remoteMessage.getData().toString());

                String title = json.getString("title");
                String message = json.getString("message");

                Intent notificationIntent = new Intent(getApplicationContext(), AdmiinHomeScreen.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                        notificationIntent, 0);

                Uri defaultsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                OreoNotification oreoNotification = new OreoNotification(this);
                Notification.Builder builder = oreoNotification.getOreoNotification(title, message, pendingIntent, defaultsound, String.valueOf(R.mipmap.ic_launcher));

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                int i = 0;
                oreoNotification.getManager().notify(i, builder.build());

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN = = == = = =",s);
    }
}
