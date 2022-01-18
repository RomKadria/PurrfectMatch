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
    String address = "";
    String age = "";
    String about = "";

    Long updateDate = new Long(0);

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }

    public Pet(){}
    public Pet(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("id",id);
        json.put("name",name);
        json.put("updateDate", FieldValue.serverTimestamp());
        return json;
    }

    public static Pet create(Map<String, Object> json) {
        String id = (String) json.get("id");
        String name = (String) json.get("name");
        Timestamp ts = (Timestamp)json.get("updateDate");
        Long updateDate = ts.getSeconds();

        Pet pet = new Pet(name,id);
        pet.setUpdateDate(updateDate);
        return pet;
    }
    //TODO:...
    public Long getUpdateDate() {
        return updateDate;
    }
}

