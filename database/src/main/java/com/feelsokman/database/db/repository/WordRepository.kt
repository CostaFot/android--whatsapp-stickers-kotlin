package com.feelsokman.database.db.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.feelsokman.database.db.dao.word.WordDao
import com.feelsokman.database.db.dao.word.entity.Word
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class WordRepository(private val wordDao: WordDao) {

    val allWords: LiveData<List<Word>> = wordDao.getAllWords()

    @WorkerThread
    fun insert(word: Word) {
        wordDao.insert(word)
    }

    @WorkerThread
    fun deleteAllCoroutine() {
        wordDao.deleteAll()
    }

    @WorkerThread
    fun deleteWord(word: Word) {
        wordDao.delete(word)
    }

    fun getWordRx(keyWeAreLookingFor: Int): Word {
        return wordDao.getWord(keyWeAreLookingFor)
    }

    fun deleteAllRx() {
        Single.fromCallable { wordDao.deleteAll() }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
}