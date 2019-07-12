package com.feelsokman.stickers.di.module

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.content.UriMatcher
import android.content.res.AssetManager
import android.content.res.Resources
import com.feelsokman.stickers.BuildConfig
import com.feelsokman.stickers.R
import com.feelsokman.stickers.contentprovider.StickerProviderHelper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.squareup.otto.Bus
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule {

    companion object {
        const val CONTENT_PROVIDER_AUTHORITY = "key.content.provider.authority"
        const val PACKAGE_NAME = "key.package.name"
    }

    @Provides
    @Singleton
    fun providesContext(application: Application): Context {
        return application
    }

    @Provides
    @Singleton
    fun providesResources(application: Application): Resources {
        return application.resources
    }

    @Provides
    @Singleton
    fun providesAssetManager(application: Application): AssetManager {
        return application.assets
    }

    @Provides
    @Named(CONTENT_PROVIDER_AUTHORITY)
    fun providesContentProviderAuthority(resources: Resources): String {
        return resources.getString(R.string.content_provider_authority)
    }

    @Provides
    @Named(PACKAGE_NAME)
    fun providesPackageName(application: Application): String {
        return application.packageName
    }

    @Provides
    fun providesStickerProviderHelper(
        @Named(CONTENT_PROVIDER_AUTHORITY) providerAuthority: String,
        @Named(PACKAGE_NAME) packageName: String,
        contentResolver: ContentResolver
    ): StickerProviderHelper {
        if (!providerAuthority.startsWith(packageName)) {
            throw IllegalStateException("Your authority $providerAuthority or the content provider should start with your package name: $packageName")
        }
        return StickerProviderHelper(providerAuthority, contentResolver)
    }

    @Provides
    fun providesApplicationContentResolver(application: Application): ContentResolver {
        return application.contentResolver
    }

    @Provides
    fun providesUriMatcher(): UriMatcher {
        return UriMatcher(UriMatcher.NO_MATCH)
    }

    @Provides
    @Singleton
    fun providesBus(): Bus = Bus()

    @Provides
    fun providesDebugFlag(): Boolean {
        return BuildConfig.DEBUG
    }

    @Provides
    fun providesGson(): Gson {
        return GsonBuilder().setPrettyPrinting().create()
    }

    @Provides
    internal fun providesExecutionScheduler() = Schedulers.io()
}