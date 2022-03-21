package com.example.purrfectmatch.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
public class ChatMessage {
    final public static String COLLECTION_NAME = "messages";

    @PrimaryKey
    @NonNull
    String sendingId;
    String receivingId;
    String textMessage;
    String imgUrl;
    long messageTime;
    String id;



    public ChatMessage() {
    }

    public ChatMessage(String sendingId, String receivingId, String textMessage, String imgUrl, String id) {
        this.sendingId = sendingId;
        this.receivingId = receivingId;
        this.textMessage = textMessage;
        this.imgUrl = imgUrl;
        this.messageTime = new Date().getTime();
        this.id = id;
    }

    public ChatMessage(String sendingId, String receivingId, String textMessage, String imgUrl, long messageTime, String id) {
        this.sendingId = sendingId;
        this.receivingId = receivingId;
        this.textMessage = textMessage;
        this.imgUrl = imgUrl;
        this.messageTime = messageTime;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReceivingId() {
        return receivingId;
    }

    public void setReceivingId(String receivingId) {
        this.receivingId = receivingId;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getSendingId() {
        return sendingId;
    }

    public void setSendingId(String sendingId) {
        this.sendingId = sendingId;
    }


    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("sendingId", sendingId);
        json.put("receivingId", receivingId);
        json.put("textMessage", textMessage);
        json.put("imgUrl", imgUrl);
        json.put("messageTime", messageTime);
        return json;
    }

    public static ChatMessage create(Map<String, Object> json, String id) {
        String sendingId = (String) json.get("sendingId");
        String receivingId = (String) json.get("receivingId");
        String textMessage = (String) json.get("textMessage");
        String imgUrl = (String) json.get("imgUrl");
        Timestamp ts = (Timestamp) json.get("messageTime");
        Long messageTime = ts.getSeconds();
        ChatMessage message = new ChatMessage(sendingId, receivingId, textMessage, imgUrl, messageTime, id);
        return message;
    }
}
