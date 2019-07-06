package com.feelsokman.stickers.ui.activity.di

import com.feelsokman.stickers.ui.activity.viewmodel.MainViewModelFactory
import com.feelsokman.stickers.usecase.GetStringFromStorageUseCase
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {
    @Provides
    internal fun providesMainViewModelFactory(getStringFromStorageUseCase: GetStringFromStorageUseCase) =
        MainViewModelFactory(getStringFromStorageUseCase)
}