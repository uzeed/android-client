package com.neverwin.uzeed.uzeed.Model;

public class Notification {
    private String title;
    private String body;
    private String sound = "default";
    private String icon = "fcm_push_icon";


    public Notification(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public Notification() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
