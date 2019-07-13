package com.costafot.stickers.ui.activity.di

import com.costafot.stickers.ui.activity.viewmodel.MainViewModelFactory
import com.costafot.stickers.usecase.StickerPackLoaderUseCase
import com.costafot.stickers.usecase.WhiteListCheckUseCase
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {
    @Provides
    internal fun providesMainViewModelFactory(
        stickerPackLoaderUseCase: StickerPackLoaderUseCase,
        whiteListCheckUseCase: WhiteListCheckUseCase
    ) =
        MainViewModelFactory(stickerPackLoaderUseCase, whiteListCheckUseCase)
}