package com.example.purrfectmatch.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Pet.class, ChatMessage.class}, version = 1)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract PetDao petDao();
    public abstract ChatMessageDao chatMessageDao();
}

public class AppLocalDb{
    static public AppLocalDbRepository db =
            Room.databaseBuilder(MyApplication.getContext(),
                    AppLocalDbRepository.class,
                    "purrfectMatch.db")
                    .fallbackToDestructiveMigration()
                    .build();
}
