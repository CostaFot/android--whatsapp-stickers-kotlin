package com.feelsokman.androidtemplate.ui.fragments.host.di

import com.feelsokman.androidtemplate.ui.fragments.host.viewmodel.HostViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class HostModule {

    @Provides
    internal fun providesHostViewModelFactory() = HostViewModelFactory()
}