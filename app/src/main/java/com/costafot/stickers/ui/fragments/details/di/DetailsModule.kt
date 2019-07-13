package com.costafot.stickers.ui.fragments.details.di

import com.costafot.stickers.ui.fragments.details.viewmodel.DetailsViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class DetailsModule {

    @Provides
    internal fun providesDetailsViewModelFactory() =
        DetailsViewModelFactory()
}