package com.costafot.stickers.extensions

import com.costafot.stickers.usecase.BaseDisposableUseCase
import io.reactivex.disposables.CompositeDisposable

fun CompositeDisposable.isEmpty(): Boolean {
    return size() == 0
}

fun BaseDisposableUseCase.hasNoMemoryLeaks(): Boolean {
    return compositeDisposable.isEmpty()
}