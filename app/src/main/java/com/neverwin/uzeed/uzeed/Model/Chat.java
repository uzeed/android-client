package com.neverwin.uzeed.uzeed.Model;

public class Chat {

    private String sender;
    private String receiver;
    private String message;
    private String datetime;

    public Chat(String sender, String receiver, String message, String datetime) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.datetime = datetime;
    }

    public Chat() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
