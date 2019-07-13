package com.costafot.stickers.ui.fragments.host.di

import com.costafot.stickers.ui.fragments.host.viewmodel.HostViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class HostModule {

    @Provides
    internal fun providesHostViewModelFactory() = HostViewModelFactory()
}