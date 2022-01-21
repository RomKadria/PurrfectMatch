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
    String password = "";
    String email = "";
    String petUrl;
    Long updateDate = new Long(0);

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }

    public Pet(){}
    public Pet(String name, String id, int age, String address, String description, String password, String email, String petUrl) {
        this.name = name;
        this.id = id;
        this.age = age;
        this.address = address;
        this.description = description;
        this.email = email;
        this.password = password;
        this.petUrl = petUrl;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPetUrl() {
        return petUrl;
    }

    public void setPetUrl(String petUrl) {
        this.petUrl = petUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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
        json.put("password", password);
        json.put("email", email);
        json.put("petUrl", petUrl);
        return json;
    }

    public static Pet create(Map<String, Object> json) {
        String id = (String) json.get("id");
        String name = (String) json.get("name");
        int age = (int) json.get("age");
        String address = (String) json.get("address");
        String description = (String) json.get("description");
        String email = (String) json.get("email");
        String password = (String) json.get("password");
        String petUrl = (String) json.get("petUrl");
        Timestamp ts = (Timestamp)json.get("updateDate");
        Long updateDate = ts.getSeconds();
        Pet pet = new Pet(name,id,age,address,description, password, email, petUrl);
        pet.setUpdateDate(updateDate);
        return pet;
    }
}

