package com.jonas.dat153v2.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {GameObject.class}, version = 1)
public abstract class GameObjectDatabase extends RoomDatabase {

    private static GameObjectDatabase instance;

    public abstract GameObjectDao gameObjectDao();

    public static synchronized GameObjectDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    GameObjectDatabase.class, "gameObject_database")
                    .fallbackToDestructiveMigration()
                    .build();

        }
        return instance;
    }
}
