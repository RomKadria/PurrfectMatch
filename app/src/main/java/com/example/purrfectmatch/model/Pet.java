package com.example.purrfectmatch.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

@Entity
public class Pet {
    final public static String COLLECTION_NAME = "pets";
    @PrimaryKey
    @NonNull
    String id = "";
    String name = "";
    String email = "";
    String password= "";
    Long updateDate = new Long(0);

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }

    public Pet(){}
    public Pet(String name, String id, String email, String password) {
        this.name = name;
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) { this.email = email; }

    public void setPassword(String password) { this.password = password; }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getEmail() { return email; }

    public String getPassword() { return password; }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("id",id);
        json.put("name",name);
        json.put("email",email);
        json.put("password",password);
        json.put("updateDate", FieldValue.serverTimestamp());
        return json;
    }

    public static Pet create(Map<String, Object> json) {
        String id = (String) json.get("id");
        String name = (String) json.get("name");
        String email = (String) json.get("email");
        String password = (String) json.get("password");
        Timestamp ts = (Timestamp)json.get("updateDate");
        Long updateDate = ts.getSeconds();

        Pet pet = new Pet(name,id,email,password);
        pet.setUpdateDate(updateDate);
        return pet;
    }
    //TODO:...
    public Long getUpdateDate() {
        return updateDate;
    }
}

