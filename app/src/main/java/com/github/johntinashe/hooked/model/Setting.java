package com.github.johntinashe.hooked.model;

import android.support.annotation.Nullable;

import com.google.firebase.firestore.GeoPoint;

public class Setting {

    private boolean notification;
    private int radius;
    private String gender;
    @Nullable private GeoPoint location;


    public Setting() {
    }

    public Setting(boolean notification, int radius, String gender, @Nullable GeoPoint location) {
        this.notification = notification;
        this.radius = radius;
        this.gender = gender;
        this.location = location;
    }

    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Nullable
    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(@Nullable GeoPoint location) {
        this.location = location;
    }
}
