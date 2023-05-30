package com.example.notes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    //EditText editText = (EditText) findViewById(R.id.editNote);

    int note_id = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Get EditText view:
        EditText editText = findViewById(R.id.editNote);

        // Get Intent:
        Intent intent = getIntent();
        // Get the value of integer "note_id" from intent:
        // Initialise class variable "note_id" with value from intent:
        note_id = intent.getIntExtra("note_id", note_id);

        if (note_id != -1) {
            Note note = NotesActivity.notes.get(note_id);
            String noteContent = note.getContent();
            // Use editText.setText() to display the contents of the note on screen.
            editText.setText(noteContent);
        }
    }

    public void saveMethod(View view) {
        // 1. Get editText view and the content the user entered:
        EditText editText = findViewById(R.id.editNote);
        String content = editText.getText().toString();

        // 2. Initialise SQLiteDatabase instance:
        Context context = getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("notes", Context.MODE_PRIVATE,null);

        // 3. Initialise DBHelper class:
        DBHelper dbHelper = new DBHelper(sqLiteDatabase);

        // 4. Set username in the following variable by fetching it from SharedPreferences:
        this.sharedPreferences = getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        // 5. Save information to database:
        String title;
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String date = dateFormat.format(new Date());

        if (note_id == -1) {
            // Add new note:
            title = "NOTE_" + (NotesActivity.notes.size() + 1);
            dbHelper.saveNotes(username, title, content, date);
        } else {
            // Update note:
            title = "NOTE_" + (note_id + 1);
            dbHelper.updateNote(title, date, content, username);
        }
        Intent intent = new Intent(this, NotesActivity.class);
        startActivity(intent);
    }
}

