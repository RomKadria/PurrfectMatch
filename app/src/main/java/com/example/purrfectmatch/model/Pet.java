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
    int age = 0;
    String address = "";
    String description = "";
    Long updateDate = new Long(0);

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }

    public Pet(){}
    public Pet(String name, String id, int age, String address, String description) {
        this.name = name;
        this.id = id;
        this.age = age;
        this.address = address;
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() { return name; }

    public String getId() { return id; }

    public int getAge() { return age; }

    public String getAddress() { return address; }

    public String getDescription() { return description; }

    public Long getUpdateDate() {
        return updateDate;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("id",id);
        json.put("name",name);
        json.put("age",age);
        json.put("address",address);
        json.put("description",description);
        json.put("updateDate", FieldValue.serverTimestamp());
        return json;
    }

    public static Pet create(Map<String, Object> json) {
        String id = (String) json.get("id");
        String name = (String) json.get("name");
        int age = (int) json.get("age");
        String address = (String) json.get("address");
        String description = (String) json.get("description");
        Timestamp ts = (Timestamp)json.get("updateDate");
        Long updateDate = ts.getSeconds();

        Pet pet = new Pet(name,id,age,address,description);
        pet.setUpdateDate(updateDate);
        return pet;
    }
}

