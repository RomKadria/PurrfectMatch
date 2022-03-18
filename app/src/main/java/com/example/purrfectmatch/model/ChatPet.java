package com.example.purrfectmatch.model;

import androidx.room.Entity;

import com.google.firebase.Timestamp;

import java.util.Map;

@Entity
public class ChatPet extends Pet {
    public ChatPet(){
        super();
    }
    public ChatPet( String email, String name, int age, String address, String description, String password, String petUrl) {
        super(email, name, age, address, description, password, petUrl);
    }

    public static ChatPet create(Map<String, Object> json) {
        String email = (String) json.get("email");
        String name = (String) json.get("name");
        int age = Integer.parseInt(json.get("age").toString());
        String address = (String) json.get("address");
        String description = (String) json.get("description");
        String password = (String) json.get("password");
        String petUrl = (String) json.get("petUrl");
        Timestamp ts = (Timestamp)json.get("updateDate");
        Long updateDate = ts.getSeconds();
        ChatPet pet = new ChatPet(email,name,age,address,description, password, petUrl);
        pet.setUpdateDate(updateDate);
        return pet;
    }
}
