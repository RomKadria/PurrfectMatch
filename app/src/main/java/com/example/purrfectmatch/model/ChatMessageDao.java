package com.example.purrfectmatch.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ChatMessageDao {
    @Query("select * from ChatMessage where sendingId = :petId")
    List<ChatMessage> getAllChats(String petId);

    @Query("select * from ChatMessage where sendingId = :sendingId and receivingId = :receivingId")
    List<ChatMessage> getAllChatMessages(String sendingId, String receivingId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Pet... pets);

    @Delete
    void delete(Pet pet);
}
