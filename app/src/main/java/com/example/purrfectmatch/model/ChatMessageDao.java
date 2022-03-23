package com.example.purrfectmatch.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ChatMessageDao {
    @Query("select * from ChatMessage " +
            "where ((sendingId = :connectedPetId and receivingId = :otherPetId) " +
            "or (sendingId = :otherPetId and receivingId = :connectedPetId)) " +
            "and isDeleted = 0 " +
            "order by messageTime asc")
    List<ChatMessage> getAllChatMessages(String connectedPetId, String otherPetId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(ChatMessage... chatMessages);

    @Delete
    void delete(ChatMessage chatMessage);
}
