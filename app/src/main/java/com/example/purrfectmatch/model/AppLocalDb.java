package com.example.purrfectmatch.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Pet.class}, version = 2)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract PetDao petDao();
}

public class AppLocalDb{
    static public AppLocalDbRepository db =
            Room.databaseBuilder(MyApplication.getContext(),
                    AppLocalDbRepository.class,
                    "purrfectMatch.db")
                    .fallbackToDestructiveMigration()
                    .build();
}
