package com.example.kernel.cache;

//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import java.io.IOException;
//
//import androidx.room.Room;
//import androidx.room.migration.Migration;
//import androidx.room.testing.MigrationTestHelper;
//import androidx.sqlite.db.SupportSQLiteDatabase;
//import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;
//import androidx.test.InstrumentationRegistry;
//import androidx.test.runner.AndroidJUnit4;

//@RunWith(AndroidJUnit4.class)
//public class MigrationTest {
//    private static final String TEST_DB = "migration-test";
//
//    @Rule
//    public MigrationTestHelper helper;
//
//    public MigrationTest() {
//        helper = new MigrationTestHelper(InstrumentationRegistry.getInstrumentation(),
//                AppDatabase.class.getCanonicalName(),
//                new FrameworkSQLiteOpenHelperFactory());
//    }
//
//    @Test
//    public void migrateAll() throws IOException {
//        // Create earliest version of the database.
//        SupportSQLiteDatabase db = helper.createDatabase(TEST_DB, 3);
//        db.close();
//
//        // Open latest version of the database. Room will validate the schema
//        // once all migrations execute.
//        AppDatabase appDb = Room.databaseBuilder(
//                InstrumentationRegistry.getInstrumentation().getTargetContext(),
//                AppDatabase.class,
//                TEST_DB)
//                .addMigrations(ALL_MIGRATIONS)
//                .build();
//        appDb.getOpenHelper().getWritableDatabase();
//        appDb.close();
//    }
//
//    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
//        @Override
//        public void migrate(SupportSQLiteDatabase database) {
//            database.execSQL("ALTER TABLE user ADD COLUMN phone TEXT");
//        }
//    };
//
//    // Array of all migrations
//    private static final Migration[] ALL_MIGRATIONS = new Migration[]{
//            MIGRATION_2_3};
//}