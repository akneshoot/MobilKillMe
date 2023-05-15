package com.example.practica4.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DAO {
    @Query("SELECT * FROM RenameName")
    List<RenameName> getAll();

    @Insert
    void insertAll(RenameName... users);
}
