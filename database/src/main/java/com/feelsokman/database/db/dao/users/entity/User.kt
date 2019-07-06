package com.feelsokman.database.db.dao.users.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
class User(
    @ColumnInfo(name = "primaryKey") @PrimaryKey(autoGenerate = true) val key: Int,
    @ColumnInfo(name = "name") val name: String

) {
    @Ignore
    constructor(name: String) : this(0, name)
}