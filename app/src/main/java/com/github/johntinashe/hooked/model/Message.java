package com.github.johntinashe.hooked.model;

import android.support.annotation.Nullable;

public class Message {

    private String lastMessage;
    private String userName;
    private @Nullable String thumbImage;

    public Message() {
    }

    public Message(String lastMessage, String userName, @Nullable String thumbImage) {
        this.lastMessage = lastMessage;
        this.userName = userName;
        this.thumbImage = thumbImage;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Nullable
    public String getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(@Nullable String thumbImage) {
        this.thumbImage = thumbImage;
    }
}
