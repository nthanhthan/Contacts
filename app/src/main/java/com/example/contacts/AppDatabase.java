package com.example.contacts;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
@Database(entities = {contact.class},version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ContactDAO contactDAO();
    public static AppDatabase instance;
    public  static AppDatabase getInstance(Context context){
        if(instance==null){
          instance = Room.databaseBuilder(context,
                    AppDatabase.class, "Contactapp").build();
        }
        return instance;
    }
}
