package com.feelsokman.database.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.feelsokman.database.db.dao.users.UserDao
import com.feelsokman.database.db.dao.users.entity.User
import com.feelsokman.database.db.dao.word.WordDao
import com.feelsokman.database.db.dao.word.entity.Word
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Word::class, User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "Word_database"
                ).addCallback(AppDatabaseCallback(scope)).build()
                INSTANCE = instance
                return instance
            }
        }
    }

    private class AppDatabaseCallback(private val scope: CoroutineScope) :
        RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateWordDatabase(database.wordDao())
                    populateUserDatabase(database.userDao())
                }
            }
        }

        fun populateWordDatabase(wordDao: WordDao) {
            wordDao.deleteAll()

            var word = Word("Hello")
            wordDao.insert(word)
            word = Word("World!")
            wordDao.insert(word)
        }

        fun populateUserDatabase(userDao: UserDao) {
            userDao.deleteAll()

            var user = User("Costa")
            userDao.insert(user)
            user = User("Mike")
            userDao.insert(user)
        }
    }
}
