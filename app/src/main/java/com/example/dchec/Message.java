package com.example.dchec;

public class Message {
    private String Sender;
    private String Receiver;
    private String context;

    public Message(){

    }

    public Message(String sender, String receiver, String context) {
        Sender = sender;
        Receiver = receiver;
        this.context = context;
    }

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }

    public String getReceiver() {
        return Receiver;
    }

    public void setReceiver(String receiver) {
        Receiver = receiver;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
