package com.example.practica4.model;

import android.content.Context;

import androidx.room.Room;

import com.example.practica4.model.App;
import com.example.practica4.model.DAO;
import com.example.practica4.model.Datasource;
import com.example.practica4.model.RenameName;

import java.util.ArrayList;
import java.util.List;

public class Repository<T> {
    private final Datasource<T> arrayDataSource;

    public App db = null;
    public DAO Dao = null;
    public List<RenameName> historyrename = null;

    public Repository(Datasource<T> arrayDataSource, Context context) {
        this.arrayDataSource = arrayDataSource;
        db = Room.databaseBuilder(context.getApplicationContext(),
                App.class, "history_of_rename").allowMainThreadQueries().build();
        Dao = db.DAO();
        historyrename = db.DAO().getAll();
    }

    public List<T> getAll() {
        List<T> result = new ArrayList<>();
        result.addAll(arrayDataSource.getAll());
        return result;
    }

    public void add(T item) {
        arrayDataSource.add(item);
    }

    public void insert(String name) {
        Dao.insertAll(new RenameName(name));
    }
    public void getHistory(){
        for(RenameName list: historyrename)
            System.out.println(list.name);
    }

}
