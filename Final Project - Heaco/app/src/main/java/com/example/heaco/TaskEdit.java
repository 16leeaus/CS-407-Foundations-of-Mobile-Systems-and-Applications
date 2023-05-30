package com.example.heaco;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Arrays;
import java.util.List;

public class TaskEdit extends AppCompatActivity {
    private String docId;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    Switch completeSwitch;
    TextView titleBox;
    TextView descBox;
    String path;
    List<String> users;

    // Get current user info:
    String uid = FirebaseAuth.getInstance().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);

        Intent intent = getIntent();
        this.docId = intent.getStringExtra("docId");
        this.path = "tasks/" + this.docId;
        this.db = FirebaseFirestore.getInstance();
        this.completeSwitch = (Switch) findViewById(R.id.completeSwitch);
        this.titleBox = (TextView) findViewById(R.id.titleTextBox);
        this.descBox = (TextView) findViewById(R.id.descTextBox);



        setFields();

    }

    private void setFields() {
        Log.i(TAG, "setFields: path: " + path);
        this.db.document(path).get().addOnCompleteListener(
                new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        Log.i(TAG, "onComplete: " + task.isSuccessful());
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            HeacoTask heacoTask = doc.toObject(HeacoTask.class);
//                            Log.i(TAG, "onComplete: " + heacoTask.getTitle());



                            titleBox.setText(heacoTask.getTitle());
                            descBox.setText(heacoTask.getDescription());
                            completeSwitch.setChecked(heacoTask.getComplete());
                            TaskEdit.this.users = heacoTask.getUsers();
                        }
                    }
                }
        );
    }

    private void saveDocument() {
        Log.i(TAG, "saveDocument: Saving your changes!");
//        Log.i(TAG, "switch: " + completeSwitch.isChecked());
        String desc = this.descBox.getText().toString();
        String title = this.titleBox.getText().toString();
        boolean complete = this.completeSwitch.isChecked();
        HeacoTask heacoTask = new HeacoTask(title,desc,complete, this.users);
        db.document(path).set(heacoTask, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //toast
                Toast.makeText(TaskEdit.this,"Task updated!", Toast.LENGTH_SHORT).show();
                // return
                returnToListActivity();
            }
        });
    }

    private void returnToListActivity() {
        Intent intent = new Intent(this, TasksList.class);
        startActivity(intent);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveButton:
                Log.i(TAG, "onClick: Saving...");
                saveDocument();
                break;
            case R.id.discardButton:
                Log.i(TAG, "onClick: Discarding...");
                returnToListActivity();
                break;
            case R.id.deleteButton:
                Log.i(TAG, "onClick: Deleting...");
                deleteDocument();
            default:
                break;
        }
    }

    private void deleteDocument() {
        Log.i(TAG, "Deleting" + path);
        this.db.document(path).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.i(TAG, "onSuccess: Deleted Document!");
                Toast.makeText(TaskEdit.this,"Task Deleted!", Toast.LENGTH_SHORT).show();
                returnToListActivity();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TaskEdit.this,"Problem deleting task", Toast.LENGTH_SHORT).show();
            }
        });
    }
}