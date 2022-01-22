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
    String email = "";
    String name = "";
    int age = 0;
    String address = "";
    String description = "";
    String password = "";
    String phoneNumber = "";
    String petUrl;
    Long updateDate = new Long(0);

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }

    public Pet(){}
    public Pet( String email, String name, int age, String address, String description, String password, String phoneNumber, String petUrl) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.description = description;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.petUrl = petUrl;
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

    public void setPassword(String password) { this.password = password; }

    public void setPetUrl(String petUrl) {
        this.petUrl = petUrl;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getName() { return name; }

    public int getAge() { return age; }

    public String getAddress() { return address; }

    public String getDescription() { return description; }

    public String getPassword() {
        return password;
    }

    public String getPetUrl() {
        return petUrl;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Long getUpdateDate() {
        return updateDate;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("email", email);
        json.put("name",name);
        json.put("age",age);
        json.put("address",address);
        json.put("description",description);
        json.put("updateDate", FieldValue.serverTimestamp());
        json.put("password", password);
        json.put("phoneNumber", phoneNumber);
        json.put("petUrl", petUrl);
        return json;
    }

    public static Pet create(Map<String, Object> json) {
        String email = (String) json.get("email");
        String name = (String) json.get("name");
        int age = Integer.parseInt(json.get("age").toString());
        String address = (String) json.get("address");
        String description = (String) json.get("description");
        String password = (String) json.get("password");
        String phoneNumber = (String) json.get("phoneNumber");
        String petUrl = (String) json.get("petUrl");
        Timestamp ts = (Timestamp)json.get("updateDate");
        Long updateDate = ts.getSeconds();
        Pet pet = new Pet(email,name,age,address,description, password, phoneNumber, petUrl);
        pet.setUpdateDate(updateDate);
        return pet;
    }
}

