package com.feelsokman.database.db.dao.users

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.feelsokman.database.db.dao.users.entity.User

@Dao
interface UserDao {

    @Query("SELECT * from user_table ORDER BY name ASC")
    fun getAllUsers(): LiveData<List<User>>

    @Insert
    fun insert(user: User)

    @Query("DELETE FROM user_table")
    fun deleteAll()

    @Delete
    fun delete(user: User)

    @Query("SELECT * from user_table WHERE `name` =:name")
    fun getUser(name: String): User
}