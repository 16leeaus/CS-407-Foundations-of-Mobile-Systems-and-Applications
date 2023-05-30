package com.example.heaco;

public class HeacoUser {
    private String name;
    private String email;
    private String uid;

    public HeacoUser() {}

    public HeacoUser(String name, String email, String uid) {
        this.name = name;
        this.email = email;
        this.uid = uid;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getUid() {
        return this.uid;
    }
}
