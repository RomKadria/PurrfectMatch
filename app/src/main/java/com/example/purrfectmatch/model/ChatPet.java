package com.example.purrfectmatch.model;

import androidx.room.Entity;

import com.google.firebase.Timestamp;

import java.util.Map;

@Entity
public class ChatPet extends Pet {
    String connectedEmail = "";

    public ChatPet() {
        super();
    }

    public ChatPet(String email, String name, int age, String address, String description, String password, String petUrl, double latitude, double longitude, String connectedEmail) {
        super(email, name, age, address, description, password, petUrl, latitude, longitude);
        this.connectedEmail = connectedEmail;
    }

    public void setConnectedEmail(String connectedEmail) {
        this.connectedEmail = connectedEmail;
    }

    public String getConnectedEmail() {
        return connectedEmail;
    }

    public static ChatPet create(Map<String, Object> json, String connectedEmail) {
        String email = (String) json.get("email");
        String name = (String) json.get("name");
        int age = Integer.parseInt(json.get("age").toString());
        String address = (String) json.get("address");
        String description = (String) json.get("description");
        String password = (String) json.get("password");
        String petUrl = (String) json.get("petUrl");
        Timestamp ts = (Timestamp) json.get("updateDate");
        Long updateDate = ts.getSeconds();
        double latitude = Double.parseDouble(json.get("latitude").toString());
        double longitude = Double.parseDouble(json.get("longitude").toString());
        ChatPet pet = new ChatPet(email, name, age, address, description, password, petUrl, latitude, longitude, connectedEmail);
        pet.setUpdateDate(updateDate);
        return pet;
    }
}
