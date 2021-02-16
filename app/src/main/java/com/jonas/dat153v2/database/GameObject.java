package com.jonas.dat153v2.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "gameObject_table")
public class GameObject {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private byte[] image;

    public GameObject(String name, byte[] image) {
        this.name = name;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
