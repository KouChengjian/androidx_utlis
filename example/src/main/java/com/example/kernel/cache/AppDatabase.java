package com.example.kernel.cache;

import android.content.Context;

import com.example.kernel.cache.dao.UserDao;
import com.example.kernel.entity.po.UserEntity;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

/**
 * Created with Android Studio.
 * User: kcj
 * Date: 2020/04/28 14:30
 * Description: 修改以下实体类，都需要修改数据库版本迁移
 */
@Database(entities = {UserEntity.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "android_room_dev.db")
                            .allowMainThreadQueries()
                            .addMigrations(MIGRATION_2_3)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

//    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
//        @Override
//        public void migrate(SupportSQLiteDatabase database) {
//            database.execSQL("ALTER TABLE user ADD COLUMN user_name INTEGER");
//            database.execSQL("ALTER TABLE user ADD COLUMN user_name TEXT");
//        }
//    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE user ADD COLUMN phone TEXT");
        }
    };

}
