package com.example.purrfectmatch.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

@Entity
public class ChatMessage {
    final public static String COLLECTION_NAME = "messages";

    @PrimaryKey
    String sendingId;
    String receivingId;
    String textMessage;
    String imgUrl;

    Long updateDate = new Long(0);

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }

    public ChatMessage(String sendingId, String receivingId, String textMessage, String imgUrl) {
        this.sendingId = sendingId;
        this.receivingId = receivingId;
        this.textMessage = textMessage;
        this.imgUrl = imgUrl;
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

    public Long getUpdateDate() {
        return updateDate;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("sendingId", sendingId);
        json.put("receivingId", receivingId);
        json.put("textMessage", textMessage);
        json.put("imgUrl", imgUrl);
        json.put("updateDate", FieldValue.serverTimestamp());
        return json;
    }

    public static ChatMessage create(Map<String, Object> json) {
        String sendingId = (String) json.get("sendingId");
        String receivingId = (String) json.get("receivingId");
        String textMessage = (String) json.get("textMessage");
        String imgUrl = (String) json.get("imgUrl");
        Timestamp ts = (Timestamp) json.get("updateDate");
        Long updateDate = ts.getSeconds();
        ChatMessage message = new ChatMessage(sendingId, receivingId, textMessage, imgUrl);
        message.setUpdateDate(updateDate);
        return message;
    }
}
