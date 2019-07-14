package com.costafot.stickers.ui.activity.di

import com.costafot.stickers.ui.activity.viewmodel.MainViewModelFactory
import com.costafot.stickers.usecase.ActionResolverUseCase
import com.costafot.stickers.usecase.IntentResolverUseCase
import com.costafot.stickers.usecase.StickerPackLoaderUseCase
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {
    @Provides
    internal fun providesMainViewModelFactory(
        stickerPackLoaderUseCase: StickerPackLoaderUseCase,
        intentResolverUseCase: IntentResolverUseCase,
        actionResolverUseCase: ActionResolverUseCase
    ) = MainViewModelFactory(stickerPackLoaderUseCase, intentResolverUseCase, actionResolverUseCase)
}