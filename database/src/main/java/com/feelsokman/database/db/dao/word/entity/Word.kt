package com.feelsokman.database.db.dao.word.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "word_table")
class Word(
    @ColumnInfo(name = "primaryKey") @PrimaryKey(autoGenerate = true) val key: Int,
    @ColumnInfo(name = "word") val word: String
) {

    @Ignore
    constructor(word: String) : this(0, word)
}