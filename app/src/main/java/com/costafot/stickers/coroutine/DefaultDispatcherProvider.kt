package com.costafot.stickers.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DefaultDispatcherProvider(
    override val ui: CoroutineDispatcher = Dispatchers.Main,
    override val default: CoroutineDispatcher = Dispatchers.Default,
    override val io: CoroutineDispatcher = Dispatchers.IO,
    override val unconfined: CoroutineDispatcher = Dispatchers.Unconfined
) : DispatcherProvider
