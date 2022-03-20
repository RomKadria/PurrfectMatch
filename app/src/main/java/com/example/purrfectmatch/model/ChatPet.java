package com.example.purrfectmatch.model;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.room.Entity;

import com.google.firebase.Timestamp;

import java.util.Map;

@Entity
public class ChatPet extends Pet {
    String connectedEmail = "";

    public ChatPet(){
        super();
    }
    public ChatPet( String email, String name, int age, String address, String description, String password, String petUrl, String connectedEmail) {
        super(email, name, age, address, description, password, petUrl);
        this.connectedEmail = connectedEmail;
    }

    public void setConnectedEmail(String connectedEmail) {
        this.connectedEmail = connectedEmail;
    }

    public String getConnectedEmail() { return connectedEmail; }

    public static ChatPet create(Map<String, Object> json, String connectedEmail) {
        String email = (String) json.get("email");
        String name = (String) json.get("name");
        int age = Integer.parseInt(json.get("age").toString());
        String address = (String) json.get("address");
        String description = (String) json.get("description");
        String password = (String) json.get("password");
        String petUrl = (String) json.get("petUrl");
        Timestamp ts = (Timestamp)json.get("updateDate");
        Long updateDate = ts.getSeconds();
        ChatPet pet = new ChatPet(email,name,age,address,description, password, petUrl, connectedEmail);
        pet.setUpdateDate(updateDate);
        return pet;
    }

    public static void setLocalLastUpdated(Long timestamp) {
        SharedPreferences.Editor ed = MyApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE).edit();
        ed.putLong("chatPetsLastUpdateDate", timestamp);
        ed.commit();
    }

    public static Long getLocalLastUpdated() {
        SharedPreferences sp = MyApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE);
        return sp.getLong("chatPetsLastUpdateDate",0);
    }
}
