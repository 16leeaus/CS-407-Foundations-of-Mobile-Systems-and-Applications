package com.example.heaco;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HeacoTask {
    private String title;
    private String description;
    private boolean complete;
    private List<String> users;

    public HeacoTask() {}

    public HeacoTask(String title,String description,String uid) {
        this(title,description,false, Arrays.asList(uid));


    }

    public HeacoTask(String title,String description, boolean complete, List<String> users) {
        this.title = title;
        this.description = description;
        this.complete = complete;
        this.users = users;
    }

    public String getTitle() {return this.title;}
    public String getDescription() {return this.description;}
    public boolean getComplete() {return this.complete;}
    public List<String> getUsers() {return this.users;}
}
