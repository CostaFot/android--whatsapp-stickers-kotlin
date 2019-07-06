package com.feelsokman.androidtemplate.usecase

import com.feelsokman.domain.error.DataSourceErrorKind
import com.feelsokman.net.domain.error.DataSourceError
import com.feelsokman.net.domain.usecases.BaseDisposableUseCase
import com.feelsokman.storage.Storage
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class GetStringFromStorageUseCase(private val scheduler: Scheduler, private val storage: Storage) :
    BaseDisposableUseCase() {

    override val compositeDisposable: CompositeDisposable = CompositeDisposable()
    override var latestDisposable: Disposable? = null

    fun getStringFromStorage(callback: Callback<String>) {
        latestDisposable?.dispose()
        latestDisposable =
            Single.fromCallable { accessStorage(storage) }
                .subscribeOn(scheduler)
                .doOnSubscribe { compositeDisposable.add(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { stringFromStorage -> callback.onSuccess(stringFromStorage) },
                    { error -> callback.onError(DataSourceError(error.localizedMessage, DataSourceErrorKind.BAD_REQUEST)) }
                )
    }

    private fun accessStorage(storage: Storage): String {
        return storage.getSampleString()
    }

    override fun stopAllBackgroundWork() {
        compositeDisposable.clear()
    }
}