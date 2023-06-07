package com.neverwin.uzeed.uzeed.Model;

public class Sender {

    private String to;
    private Notification notification;
    private String priority = "high";
    private String restricted_package_name="";
    private Data data;
    public Sender() {
    }

    public Sender(String to, Notification notification,Data data) {
        this.to = to;
        this.notification = notification;
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getRestricted_package_name() {
        return restricted_package_name;
    }

    public void setRestricted_package_name(String restricted_package_name) {
        this.restricted_package_name = restricted_package_name;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


}
