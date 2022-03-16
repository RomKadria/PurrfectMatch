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

    @Query("select * from ChatMessage where receivingId = :otherPetId or sendingId = :otherPetId order by messageTime asc")
    List<ChatMessage> getAllChatMessages(String otherPetId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(ChatMessage... chatMessages);

    @Delete
    void delete(ChatMessage chatMessage);
}
