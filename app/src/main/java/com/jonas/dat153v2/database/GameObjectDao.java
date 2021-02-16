package com.jonas.dat153v2.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


import java.util.List;

@Dao
public interface GameObjectDao {

    @Insert
    void insert(GameObject gameObject);

    @Delete
    void delete(GameObject gameObject);

    @Query("SELECT * FROM gameObject_table ORDER BY name DESC")
    LiveData<List<GameObject>> getAllGameObjects();
}
