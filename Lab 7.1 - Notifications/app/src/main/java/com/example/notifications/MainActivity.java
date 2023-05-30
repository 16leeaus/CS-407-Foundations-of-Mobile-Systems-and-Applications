package com.example.notifications;

import android.app.Notification;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static com.example.notifications.App.CHANNEL_1_ID;

public class MainActivity extends AppCompatActivity {

    //public static final String CHANNEL_1_ID = "channel1";
    private static final String TAG = "Log: ";
    private NotificationManagerCompat notificationManager;
    public EditText editTextTitle = findViewById(R.id.title);
    public EditText editTextMessage = findViewById(R.id.message);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate Method entered");

        notificationManager = NotificationManagerCompat.from(this);
    }

    public void sendOnChannel1 (View view) {
        String title = editTextTitle.getText().toString();
        String message = editTextMessage.getText().toString();
        Log.d(TAG, "Inside of send method");

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_chat_black_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(1, notification);
    }
}