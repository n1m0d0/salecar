package com.example.salecar;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Log.w("message", "" + message);
        Log.d("TAG", "From: " + message.getFrom());

        // Check if message contains a data payload.
        if (message.getData().size() > 0) {
            Log.d("TAG", "Message data payload: " + message.getData());
        }

        // Check if message contains a notification payload.
        if (message.getNotification() != null) {
            Log.d("TAG", "Message Notification Title: " + message.getNotification().getTitle());
            Log.d("TAG", "Message Notification Body: " + message.getNotification().getBody());
        }
    }
}
