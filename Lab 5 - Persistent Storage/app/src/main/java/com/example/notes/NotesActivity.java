package com.example.notes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class NotesActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    public static ArrayList<Note> notes = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        // 1. Display welcome message. Fetch username from SharedPreferences:
        this.sharedPreferences = getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        TextView welcomeText = findViewById(R.id.welcomeTextItem);
        welcomeText.setText("Welcome " + username + "!");

        // 2. Get SQLiteDatabase instance:
        Context context = getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("notes", Context.MODE_PRIVATE,null);
        DBHelper dbHelper = new DBHelper(sqLiteDatabase);

        // 3. Initiate the "notes" class variable using readNotes method implemented in the DBHelper
        // class. Use the username you got from SharedPreferences as a Param in readNotes method.
        notes = dbHelper.readNotes(username);

        // 4.Create an ArrayList<String> object by iterating over notes objects:
        ArrayList<String> displayNotes = new ArrayList<>();
        for (Note note : notes) {
            displayNotes.add(String.format("Title: %s\nDate: %s", note.getTitle(), note.getDate()));
        }

        // 5. Use ListView view to display notes on screen.
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, displayNotes);
        ListView listView = findViewById(R.id.notesListItem);
        listView.setAdapter(adapter);

        // 6. Add onItemClickListener for ListView item, a note in this case:
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Initialise intent to take user to 3rd activity:
                Intent intent = new Intent(getApplicationContext(), EditActivity.class);
                // Add position of the item that was clicked as note_id:
                intent.putExtra("note_id", position);
                startActivity(intent);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logoutItem:
                handleLogout();
                return true;
            case R.id.addNoteItem:
                goToEditActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void goToEditActivity() {
        Intent intent = new Intent(this, EditActivity.class);
        startActivity(intent);
    }

    public void handleLogout() {
        this.sharedPreferences.edit().remove("username").apply();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}