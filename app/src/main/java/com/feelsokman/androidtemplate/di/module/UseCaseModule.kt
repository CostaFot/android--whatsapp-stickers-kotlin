package com.feelsokman.androidtemplate.di.module

import com.feelsokman.androidtemplate.usecase.GetStringFromStorageUseCase
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