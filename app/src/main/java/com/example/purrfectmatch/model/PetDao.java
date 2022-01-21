package com.example.purrfectmatch.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PetDao {
//    @Query("select * from Pet")
//    List<Pet> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Pet... pets);

//    @Insert
//    public void insertPet(Pet);


//    @Delete
//    void delete(Pet pet);
}
