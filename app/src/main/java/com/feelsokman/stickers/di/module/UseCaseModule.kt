package com.feelsokman.stickers.di.module

import android.content.ContentResolver
import com.feelsokman.stickers.contentprovider.utils.StickerPackValidator
import com.feelsokman.stickers.usecase.FetchStickerAssetUseCase
import com.feelsokman.stickers.usecase.GetStringFromStorageUseCase
import com.feelsokman.stickers.usecase.StickerPackLoaderUseCase
import com.feelsokman.stickers.usecase.UriResolverUseCase
import com.feelsokman.storage.Storage
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import javax.inject.Named

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
        contentResolver: ContentResolver,
        @Named(AppModule.CONTENT_PROVIDER_AUTHORITY) providerAuthority: String,
        uriResolverUseCase: UriResolverUseCase,
        stickerPackValidator: StickerPackValidator
    ): StickerPackLoaderUseCase =
        StickerPackLoaderUseCase(scheduler, contentResolver, providerAuthority, uriResolverUseCase, stickerPackValidator)

    @Provides
    internal fun providesUriResolverUseCase(@Named(AppModule.CONTENT_PROVIDER_AUTHORITY) providerAuthority: String): UriResolverUseCase =
        UriResolverUseCase(providerAuthority)

    @Provides
    internal fun providesFetchStickerAssetUseCase(
        contentResolver: ContentResolver,
        uriResolverUseCase: UriResolverUseCase
    ): FetchStickerAssetUseCase = FetchStickerAssetUseCase(contentResolver, uriResolverUseCase)
}