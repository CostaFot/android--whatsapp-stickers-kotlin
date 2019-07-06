package com.feelsokman.net.domain.usecases

import com.feelsokman.net.domain.error.DataSourceError
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseDisposableUseCase {

    abstract val compositeDisposable: CompositeDisposable
    abstract var latestDisposable: Disposable?

    interface Callback<T> {

        fun onLoadingStarted()

        fun onSuccess(result: T)

        fun onError(error: DataSourceError)
    }

    abstract fun stopAllBackgroundWork()
}
