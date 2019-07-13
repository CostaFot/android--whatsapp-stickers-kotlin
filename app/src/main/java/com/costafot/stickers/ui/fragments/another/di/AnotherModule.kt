package com.costafot.stickers.ui.fragments.another.di

import com.costafot.stickers.ui.fragments.another.viewmodel.AnotherViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class AnotherModule {

    @Provides
    internal fun providesAnotherViewModelFactory() =
        AnotherViewModelFactory()
}