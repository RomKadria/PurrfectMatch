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

    @Query("select * from ChatMessage-- where (sendingId = :sendingId and receivingId = :receivingId) or (sendingId = :receivingId and receivingId = :sendingId)")
    List<ChatMessage> getAllChatMessages();//(String sendingId, String receivingId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(ChatMessage... chatMessages);

    @Delete
    void delete(ChatMessage chatMessage);
}
