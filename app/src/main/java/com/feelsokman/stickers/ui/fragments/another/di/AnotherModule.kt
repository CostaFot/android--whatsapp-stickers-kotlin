package com.feelsokman.stickers.ui.fragments.another.di

import com.feelsokman.stickers.ui.fragments.another.viewmodel.AnotherViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class AnotherModule {

    @Provides
    internal fun providesAnotherViewModelFactory() =
        AnotherViewModelFactory()
}