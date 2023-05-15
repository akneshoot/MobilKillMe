package com.example.practica4.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {RenameName.class}, version = 1)
public abstract class App extends RoomDatabase {
    public abstract DAO DAO();
}
