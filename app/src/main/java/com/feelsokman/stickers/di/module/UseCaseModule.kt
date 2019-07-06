package com.feelsokman.stickers.di.module

import com.feelsokman.stickers.usecase.GetStringFromStorageUseCase
import com.feelsokman.storage.Storage
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler

@Module
class UseCaseModule {

    @Provides
    internal fun providesGetStringFromStorageUseCase(
        scheduler: Scheduler,
        storage: Storage
    ): GetStringFromStorageUseCase = GetStringFromStorageUseCase(scheduler, storage)
}