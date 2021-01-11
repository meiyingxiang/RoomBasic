package com.example.roombasicdemo;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

/**
 * //定义此抽象类为数据库，对应的实体类,可以多个
 * version:数据库版本号
 * exportSchema:true把表示保留版本号历史记录;
 * false:不保留记录，比如内存数据库
 */
@Database(entities = {Word.class}, version = 5, exportSchema = false)
abstract public class WordDatabase extends RoomDatabase {
    private static WordDatabase instance;

    public static synchronized WordDatabase getInstance(Context mContext) {
        if (instance == null) {
            instance = Room.databaseBuilder(mContext.getApplicationContext(), WordDatabase.class, "word_database")
                    .allowMainThreadQueries()//强制允许在主线程执行
//                    .fallbackToDestructiveMigration()
                    .addMigrations(MIGRATION_4_TO_5)
                    .build();
        }
        return instance;
    }

    //当有多个Entity时则写多个方法
    public abstract WordDao getWordDao();

    /**
     * Migration(2,3)
     * 参数是版本2到版本3
     */
    static final Migration MIGRATION_4_TO_5 = new Migration(4, 5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE word ADD COLUMN chinese_invisible INTEGER NOT NULL DEFAULT 1");
        }
    };
}
