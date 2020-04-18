package com.example.kernel.cache;

import com.example.kernel.cache.dao.UserDao;
import com.example.kernel.entity.po.UserEntity;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {UserEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
}
