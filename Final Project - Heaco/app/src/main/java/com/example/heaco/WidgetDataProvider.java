package com.example.heaco;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    List<String> displayTasks = new ArrayList<>();
    Context context;
    Intent intent;

    public WidgetDataProvider(WidgetService widgetService, Intent intent) {
        this.intent = intent;
    }

    @Override
    public void onCreate() {

        // Get user information to pull from appropriate place in database.
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;

        String uid = mAuth.getCurrentUser().getUid();

        db.collection("tasks")
                .whereArrayContains("users", uid)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        List<String> displayTasks = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : value) {
                            HeacoTask heacoTask = doc.toObject(HeacoTask.class);
                            String displayString = String.format(
                                    "Title: %s\nDescription: %s\nComplete: %s",
                                    heacoTask.getTitle(),
                                    heacoTask.getDescription(),
                                    heacoTask.getComplete()
                            );
                            displayTasks.add(displayString);
                        }
                    }
                });
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        Log.d("LOG:", "Array size is " + displayTasks.size());
        return displayTasks.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.display_widget);
        remoteViews.setTextViewText(R.id.WidgetListView, displayTasks.get(position));

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
