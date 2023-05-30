package com.example.heaco;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class TaskCreate extends AppCompatActivity {
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_create);

        db  = FirebaseFirestore.getInstance();
    }

    private void returnToListActivity() {
        Intent intent = new Intent(this, TasksList.class);
        startActivity(intent);
    }

    public void saveDataToFirebase() {
        // Get current user info:
        String uid = FirebaseAuth.getInstance().getUid();

        // create the Heaco Task Object
        TextView titleBox = (TextView) findViewById(R.id.titleTextBox);
        String title = titleBox.getText().toString();
        TextView descBox = (TextView) findViewById(R.id.descTextBox);
        String description = descBox.getText().toString();
        Log.i(TAG, "saving: " + title + description);
        HeacoTask newTask = new HeacoTask(title,description,uid);

        // Add the object to firebase, navigate back on success
        db.collection("tasks")
                .add(newTask)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(TaskCreate.this,"Successfully created task", Toast.LENGTH_SHORT).show();
                        returnToListActivity();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        Toast.makeText(TaskCreate.this,"Problem creating task", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveButton:
                Log.i(TAG, "onClick: submitting...");
                saveDataToFirebase();
                break;
            case R.id.discardButton:
                Log.i(TAG, "onClick: Discarding...");
                returnToListActivity();
                break;
            default:
                break;
        }
    }
}