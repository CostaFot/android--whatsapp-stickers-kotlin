package com.feelsokman.stickers.di.module

import android.content.Context
import com.feelsokman.stickers.contentprovider.utils.StickerPackValidator
import com.feelsokman.stickers.usecase.FetchStickerAssetUseCase
import com.feelsokman.stickers.usecase.GetStringFromStorageUseCase
import com.feelsokman.stickers.usecase.StickerPackLoaderUseCase
import com.feelsokman.stickers.usecase.UriResolverUseCase
import com.feelsokman.storage.Storage
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler

@Module
class UseCaseModule {

    @Provides
    internal fun providesStickerPackValidator(fetchStickerAssetUseCase: FetchStickerAssetUseCase): StickerPackValidator =
        StickerPackValidator(fetchStickerAssetUseCase)

    @Provides
    internal fun providesGetStringFromStorageUseCase(
        scheduler: Scheduler,
        storage: Storage
    ): GetStringFromStorageUseCase = GetStringFromStorageUseCase(scheduler, storage)

    @Provides
    internal fun providesStickerPackLoaderUseCase(
        scheduler: Scheduler,
        context: Context,
        uriResolverUseCase: UriResolverUseCase,
        stickerPackValidator: StickerPackValidator
    ): StickerPackLoaderUseCase = StickerPackLoaderUseCase(scheduler, context, uriResolverUseCase, stickerPackValidator)

    @Provides
    internal fun providesUriResolverUseCase(context: Context): UriResolverUseCase = UriResolverUseCase(context.resources)

    @Provides
    internal fun providesFetchStickerAssetUseCase(
        context: Context,
        uriResolverUseCase: UriResolverUseCase
    ): FetchStickerAssetUseCase = FetchStickerAssetUseCase(context, uriResolverUseCase)
}