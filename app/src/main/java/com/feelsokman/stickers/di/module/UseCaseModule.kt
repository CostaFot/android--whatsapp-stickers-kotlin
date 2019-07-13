package com.feelsokman.stickers.di.module

import android.content.ContentResolver
import android.content.pm.PackageManager
import com.feelsokman.stickers.contentprovider.StickerProviderHelper
import com.feelsokman.stickers.usecase.ContentFileParser
import com.feelsokman.stickers.usecase.FetchStickerAssetUseCase
import com.feelsokman.stickers.usecase.StickerPackLoaderUseCase
import com.feelsokman.stickers.usecase.StickerPackValidator
import com.feelsokman.stickers.usecase.UriResolverUseCase
import com.feelsokman.stickers.usecase.WhiteListCheckUseCase
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import javax.inject.Named

@Module
class UseCaseModule {

    @Provides
    internal fun providesContentFileParser(): ContentFileParser =
        ContentFileParser()

    @Provides
    internal fun providesStickerPackValidator(fetchStickerAssetUseCase: FetchStickerAssetUseCase): StickerPackValidator =
        StickerPackValidator(fetchStickerAssetUseCase)

    @Provides
    internal fun providesStickerPackLoaderUseCase(
        scheduler: Scheduler,
        stickerProviderHelper: StickerProviderHelper,
        fetchStickerAssetUseCase: FetchStickerAssetUseCase,
        uriResolverUseCase: UriResolverUseCase,
        stickerPackValidator: StickerPackValidator,
        whiteListCheckUseCase: WhiteListCheckUseCase
    ): StickerPackLoaderUseCase = StickerPackLoaderUseCase(
        scheduler,
        stickerProviderHelper,
        fetchStickerAssetUseCase,
        uriResolverUseCase,
        stickerPackValidator,
        whiteListCheckUseCase
    )

    @Provides
    internal fun providesUriResolverUseCase(@Named(AppModule.CONTENT_PROVIDER_AUTHORITY) providerAuthority: String): UriResolverUseCase =
        UriResolverUseCase(providerAuthority)

    @Provides
    internal fun providesFetchStickerAssetUseCase(
        contentResolver: ContentResolver,
        uriResolverUseCase: UriResolverUseCase
    ): FetchStickerAssetUseCase = FetchStickerAssetUseCase(contentResolver, uriResolverUseCase)

    @Provides
    internal fun providesWhiteListCheckUseCase(
        stickerProviderHelper: StickerProviderHelper,
        packageManager: PackageManager
    ): WhiteListCheckUseCase = WhiteListCheckUseCase(stickerProviderHelper, packageManager)
}