package com.costafot.stickers.ui.activity.di

import com.costafot.stickers.ui.activity.viewmodel.MainViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {
    @Provides
    internal fun providesMainViewModelFactory() =
        MainViewModelFactory()
}