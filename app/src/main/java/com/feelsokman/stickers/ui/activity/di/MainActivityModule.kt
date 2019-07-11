package com.feelsokman.stickers.ui.activity.di

import com.feelsokman.stickers.ui.activity.viewmodel.MainViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {
    @Provides
    internal fun providesMainViewModelFactory() =
        MainViewModelFactory()
}