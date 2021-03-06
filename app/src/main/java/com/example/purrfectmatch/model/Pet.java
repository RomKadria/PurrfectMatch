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
    String petUrl;
    Long updateDate = new Long(0);

    double latitude = 0.0;
    double longitude = 0.0;

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }

    public Pet() {
    }

    public Pet(String email, String name, int age, String address, String description, String password, String petUrl, double latitude, double longitude) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.description = description;
        this.email = email;
        this.password = password;
        this.petUrl = petUrl;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPetUrl(String petUrl) {
        this.petUrl = petUrl;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public String getPassword() {
        return password;
    }

    public String getPetUrl() {
        return petUrl;
    }

    public String getEmail() {
        return email;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Long getUpdateDate() {
        return updateDate;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("email", email);
        json.put("name", name);
        json.put("age", age);
        json.put("address", address);
        json.put("description", description);
        json.put("updateDate", FieldValue.serverTimestamp());
        json.put("password", password);
        json.put("petUrl", petUrl);
        json.put("latitude", latitude);
        json.put("longitude", longitude);
        return json;
    }

    public static Pet create(Map<String, Object> json) {
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
        Pet pet = new Pet(email, name, age, address, description, password, petUrl, latitude, longitude);
        pet.setUpdateDate(updateDate);
        return pet;
    }
}

