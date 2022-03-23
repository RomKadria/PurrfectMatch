package com.example.purrfectmatch.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ChatPetDao {
    @Query("select * from ChatPet where email = :petId")
    List<ChatPet> getById(String petId);

    @Query("select * from ChatPet where connectedEmail = :petId")
    List<ChatPet> getAllConnectedPetChats(String petId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(ChatPet... chatPets);

    @Delete
    void delete(ChatPet chatPet);
}
