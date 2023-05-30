package com.example.heaco;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TasksList extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_list);

        // init the database
        db  = FirebaseFirestore.getInstance();

        // Get current user info:
        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fbUser == null) {
            // User is not auth'd, send them back to login page
            Log.i(TAG, "onCreate: User was NULL");
            Toast.makeText(this,"Please sign in", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        HeacoUser newUser = new HeacoUser(fbUser.getDisplayName(), fbUser.getEmail(), fbUser.getUid());
        String uid = FirebaseAuth.getInstance().getUid();
        db.collection("users").document(uid).set(newUser, SetOptions.merge());


        //display all docs with latest data
        db.collection("tasks")
                .whereArrayContains("users",uid)
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

                ArrayAdapter adapter = new ArrayAdapter(TasksList.this, android.R.layout.simple_list_item_1,displayTasks);
                ListView listView = (ListView) findViewById(R.id.tasksListView);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.i(TAG, "onItemClick: Editing note position: " + position);
                        DocumentSnapshot doc = value.getDocuments().get(position);
                        String docId = doc.getId();
                        Intent intent = new Intent(getApplicationContext(), TaskEdit.class);
                        intent.putExtra("docId",docId );
                        startActivity(intent);
                    }
                });
            }
        });


    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.createTaskFab:
//                Log.i(TAG, "onClick: Creating new task!");
                Intent intent = new Intent(this, TaskCreate.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}