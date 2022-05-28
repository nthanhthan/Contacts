package com.example.contacts;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;
@Dao
public interface ContactDAO {
    @Query("SELECT *FROM Contact")
    List<contact> getAll();
    @Insert
    void insert(contact...contacts);
    @Query("UPDATE Contact SET name=:name , email=:email, mobile=:mobile,avt=:avt WHERE id=:id")
    void update(int id,String name, String email, String mobile, String avt );
}
