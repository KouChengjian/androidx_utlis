package com.example.kernel.cache.dao;

import com.example.kernel.entity.po.UserEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Single;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    Single<List<UserEntity>> getAll();

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    List<UserEntity> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    UserEntity findByName(String first, String last);

    @Insert
    Single<Long> insert(UserEntity userBean);

    @Update
    Single<Integer> update(UserEntity userBean);

    @Delete
    Single<Integer> delete(UserEntity userBean);
}
