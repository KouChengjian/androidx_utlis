package com.example.kernel.entity.po;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class UserEntity {
    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "first_name")
    public String firstName;

    @ColumnInfo(name = "last_name")
    public String lastName;

    @ColumnInfo(name = "user_name")
    public String userName;

    @Ignore
    @ColumnInfo(name = "password")
    public String password;

//    @ColumnInfo(name = "phone")
//    public String phone;
}
