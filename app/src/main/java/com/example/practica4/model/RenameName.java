package com.example.practica4.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RenameName {
    @PrimaryKey(autoGenerate = true)
    public int uid;
    @ColumnInfo(name = "history_of_rename")
    public String name;

    public RenameName(String name) {
        this.name = name;
    }
}
