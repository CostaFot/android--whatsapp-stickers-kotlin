package com.feelsokman.stickers.extensions

import com.feelsokman.net.domain.usecases.BaseDisposableUseCase
import io.reactivex.disposables.CompositeDisposable

fun CompositeDisposable.isEmpty(): Boolean {
    return size() == 0
}

fun BaseDisposableUseCase.hasNoMemoryLeaks(): Boolean {
    return compositeDisposable.isEmpty()
}