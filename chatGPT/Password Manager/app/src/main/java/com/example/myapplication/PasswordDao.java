package com.example.myapplication;

// PasswordDao.java
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PasswordDao {
    @Query("SELECT * FROM password")
    List<Password> getAll();

    @Insert
    void insert(Password password);

    @Delete
    void delete(Password password);
}
