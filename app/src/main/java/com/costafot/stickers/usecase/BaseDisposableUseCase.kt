package com.costafot.stickers.usecase

import com.costafot.stickers.usecase.error.DataSourceError
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
