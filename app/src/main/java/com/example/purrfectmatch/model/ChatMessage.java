package com.example.purrfectmatch.model;

import java.util.Date;

public class ChatMessage {
    String messageText;
    private String sendingUserId;
    private String receivingUserId;
    private long messageTime;

    public ChatMessage(String messageText, String sendingUserId, String receivingUserId) {
        this.messageText = messageText;
        this.sendingUserId = sendingUserId;
        this.receivingUserId = receivingUserId;

        // Initialize to current time
        messageTime = new Date().getTime();
    }

    public ChatMessage() {

    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getSendingUserId() {
        return sendingUserId;
    }

    public void setSendingUserId(String sendingUserId) {
        this.sendingUserId = sendingUserId;
    }

    public String getReceivingUserId() {
        return receivingUserId;
    }

    public void setReceivingUserId(String receivingUserId) {
        this.receivingUserId = receivingUserId;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
