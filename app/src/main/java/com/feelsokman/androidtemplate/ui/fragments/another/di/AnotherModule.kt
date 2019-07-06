package com.feelsokman.androidtemplate.ui.fragments.another.di

import com.feelsokman.androidtemplate.ui.fragments.another.viewmodel.AnotherViewModelFactory
import com.feelsokman.androidtemplate.usecase.GetStringFromStorageUseCase
import dagger.Module
import dagger.Provides

@Module
class AnotherModule {

    @Provides
    internal fun providesAnotherViewModelFactory(getStringFromStorageUseCase: GetStringFromStorageUseCase) =
        AnotherViewModelFactory(getStringFromStorageUseCase)
}