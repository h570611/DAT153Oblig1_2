package com.jonas.dat153v2.utils;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.jonas.dat153v2.database.GameObject;

import java.util.List;

public class GameObjectViewModel extends AndroidViewModel {
    private GameObjectRepository repository;
    private LiveData<List<GameObject>> allGameObjects;


    public GameObjectViewModel(@NonNull Application application) {
        super(application);
        repository = new GameObjectRepository(application);
        allGameObjects = repository.getAllGameObjects();
    }

    public void insert(GameObject gameObject) {repository.insert(gameObject);}


    public void delete(GameObject gameObject) {repository.delete(gameObject);}

    public LiveData<List<GameObject>> getAllGameObjects(){return allGameObjects;}
}
