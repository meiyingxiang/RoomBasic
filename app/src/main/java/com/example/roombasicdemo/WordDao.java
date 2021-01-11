package com.example.roombasicdemo;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

//Database access object

/**
 * 操作数据库的方法
 */
@Dao
public interface WordDao {
    @Insert
    void insertWords(Word... words);//多个

    @Update
    void update(Word... words);

    /**
     * 删除某一个表
     *
     * @param words
     */
    @Delete
    void deleteWords(Word... words);

    /**
     * 删除所有表
     */
    @Query("DELETE FROM WORD")
    void deleteAllWords();

    /**
     * 查询所有表并降序排列
     */
    @Query("SELECT * FROM WORD ORDER BY ID DESC")
    LiveData<List<Word>> getAllWords();
//    List<Word> getAllWords();

    @Query("SELECT * FROM WORD WHERE english_word LIKE :patten ORDER BY ID DESC")
    LiveData<List<Word>> findWordsWithPatten(String patten);
}
