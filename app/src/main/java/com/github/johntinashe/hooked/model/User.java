package com.github.johntinashe.hooked.model;

import android.support.annotation.Nullable;

import com.google.firebase.firestore.GeoPoint;

public class User {

    private String username;
    private int age ;
    @Nullable private GeoPoint location;
    @Nullable private double last_login;

    public User() {
    }

    public User(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public User(String username, int age, GeoPoint location, double last_login) {
        this.username = username;
        this.age = age;
        this.location = location;
        this.last_login = last_login;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public double getLast_login() {
        return last_login;
    }

    public void setLast_login(double last_login) {
        this.last_login = last_login;
    }
}
