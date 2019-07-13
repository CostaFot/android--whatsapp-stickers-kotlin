package com.costafot.stickers.di.module

import android.content.ContentResolver
import android.content.pm.PackageManager
import com.costafot.stickers.contentprovider.StickerProviderHelper
import com.costafot.stickers.usecase.ContentFileParser
import com.costafot.stickers.usecase.FetchStickerAssetUseCase
import com.costafot.stickers.usecase.IntentResolverUseCase
import com.costafot.stickers.usecase.StickerPackLoaderUseCase
import com.costafot.stickers.usecase.StickerPackValidator
import com.costafot.stickers.usecase.UriResolverUseCase
import com.costafot.stickers.usecase.WhiteListCheckUseCase
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
        scheduler: Scheduler,
        stickerProviderHelper: StickerProviderHelper,
        packageManager: PackageManager
    ): WhiteListCheckUseCase = WhiteListCheckUseCase(scheduler, stickerProviderHelper, packageManager)

    @Provides
    internal fun providesIntentResolverUseCase(
        @Named(AppModule.CONTENT_PROVIDER_AUTHORITY) providerAuthority: String
    ): IntentResolverUseCase = IntentResolverUseCase(providerAuthority)
}