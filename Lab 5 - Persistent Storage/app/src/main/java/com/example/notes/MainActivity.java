package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        this.sharedPreferences = getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);

        if (this.sharedPreferences.getString("username", "") != "") {
            goToNotesActivity();
            //setContentView(R.layout.activity_notes);
        }
        else {
            setContentView(R.layout.activity_main);
        }
    }

    public void goToNotesActivity() {
        Intent intent = new Intent(this, NotesActivity.class);
        startActivity(intent);
    }

    public void handleLogin(View view) {
        EditText usernameText = (EditText) findViewById(R.id.usernameTextField);
        String username = usernameText.getText().toString();

        this.sharedPreferences.edit().putString("username", username).apply();
        goToNotesActivity();
    }
}