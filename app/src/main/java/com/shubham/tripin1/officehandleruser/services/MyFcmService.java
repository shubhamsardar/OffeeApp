package com.shubham.tripin1.officehandleruser.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import com.shubham.tripin1.officehandleruser.Activities.OrderHistoryActivity;
import com.shubham.tripin1.officehandleruser.R;


/**
 * Created by Tripin1 on 7/4/2017.
 */

public class MyFcmService extends FirebaseMessagingService {


    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

          if (remoteMessage.getData().size() > 0) {
            android.util.Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            String type = remoteMessage.getData().get("type");
            android.util.Log.d(TAG, "type" + type);
            if(type != null) {
                if (type.equals("2")) {
                    String cost = remoteMessage.getData().get("orderCost");
                    android.util.Log.d(TAG, "Cost : " + cost);
                    sendNotification(cost);
                }
            }
          }


        super.onMessageReceived(remoteMessage);
    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, OrderHistoryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Your order completed ₹"+messageBody)
                .setContentText("Tap to see your history ")
                .setAutoCancel(false)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1, notificationBuilder.build());
    }
}
