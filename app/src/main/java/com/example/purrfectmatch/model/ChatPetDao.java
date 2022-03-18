package com.example.purrfectmatch.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ChatPetDao {
    @Query("select * from ChatPet")
    List<ChatPet> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(ChatPet... chatPets);
}
