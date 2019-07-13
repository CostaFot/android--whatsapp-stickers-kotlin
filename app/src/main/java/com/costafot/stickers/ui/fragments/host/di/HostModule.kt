package com.costafot.stickers.ui.fragments.host.di

import com.costafot.stickers.ui.fragments.host.viewmodel.HostViewModelFactory
import com.costafot.stickers.usecase.StickerPackLoaderUseCase
import dagger.Module
import dagger.Provides

@Module
class HostModule {

    @Provides
    internal fun providesHostViewModelFactory(stickerPackLoaderUseCase: StickerPackLoaderUseCase) =
        HostViewModelFactory(stickerPackLoaderUseCase)
}