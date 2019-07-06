package com.feelsokman.androidtemplate.ui.activity.di

import com.feelsokman.androidtemplate.ui.activity.viewmodel.MainViewModelFactory
import com.feelsokman.androidtemplate.usecase.GetStringFromStorageUseCase
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {
    @Provides
    internal fun providesMainViewModelFactory(getStringFromStorageUseCase: GetStringFromStorageUseCase) =
        MainViewModelFactory(getStringFromStorageUseCase)
}