package models;

import java.util.Date;

public class Message {

    private Date date;
    private int sender;
    private int receiver;
    private String text;

    public Message() {

    }

    public Message(Date date, int sender, int receiver, String text) {
        this.date = date;
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public int getSender() {
        return sender;
    }

    public int getReceiver() {
        return receiver;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return date + "," + sender + "," + receiver + "," + text;
    }
}
