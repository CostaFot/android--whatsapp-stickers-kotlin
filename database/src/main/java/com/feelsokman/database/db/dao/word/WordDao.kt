package com.feelsokman.database.db.dao.word

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.feelsokman.database.db.dao.word.entity.Word

@Dao
interface WordDao {

    @Query("SELECT * from word_table ORDER BY word ASC")
    fun getAllWords(): LiveData<List<Word>>

    @Insert
    fun insert(word: Word)

    @Query("DELETE FROM word_table")
    fun deleteAll()

    @Delete
    fun delete(word: Word)

    @Query("SELECT * from word_table WHERE `primaryKey` =:keyWeAreLookingFor")
    fun getWord(keyWeAreLookingFor: Int): Word
}