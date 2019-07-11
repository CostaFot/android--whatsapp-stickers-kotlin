package com.feelsokman.stickers.di.module

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.content.UriMatcher
import android.content.res.AssetManager
import android.content.res.Resources
import android.net.ConnectivityManager
import com.feelsokman.net.net.resolver.NetworkResolver
import com.feelsokman.stickers.BuildConfig
import com.feelsokman.stickers.R
import com.feelsokman.stickers.contentprovider.StickerProviderHelper
import com.feelsokman.storage.LocalStorage
import com.feelsokman.storage.Storage
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.squareup.otto.Bus
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule {

    companion object {
        const val BASE_URL = "key.base.url"
        const val CONTENT_PROVIDER_AUTHORITY = "key.content.provider.authority"
        const val PACKAGE_NAME = "key.package.name"
    }

    @Provides
    @Singleton
    internal fun providesCache(context: Context): Cache {
        return Cache(context.cacheDir, 10 * 1024 * 1024)
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
    fun providesFirebaseAnalytics(context: Context): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(context)
    }

    @Provides
    fun providesDebugFlag(): Boolean {
        return BuildConfig.DEBUG
    }

    @Provides
    @Named(BASE_URL)
    fun providesBaseUrl(): String {
        return BuildConfig.serverUrl
    }

    @Provides
    fun providesGson(): Gson {
        return GsonBuilder().setPrettyPrinting().create()
    }

    @Provides
    internal fun providesExecutionScheduler() = Schedulers.io()

    @Provides
    @Singleton
    internal fun providesLocalStorage(context: Context): Storage = LocalStorage(context)

    @Provides
    internal fun providesNetworkResolver(context: Context): NetworkResolver {

        return object : NetworkResolver {
            override fun isConnected(): Boolean {
                val connectivityManager =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
                val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
                return activeNetworkInfo != null && activeNetworkInfo.isConnected
            }
        }
    }

    @Provides
    @Singleton
    internal fun providesOkHttpClient(
        cache: Cache,
        isDebugEnabled: Boolean
    ): OkHttpClient {
        val okHttpBuilder = OkHttpClient().newBuilder()
        val loggingInterceptor = HttpLoggingInterceptor()
        val level = when {
            isDebugEnabled -> HttpLoggingInterceptor.Level.BODY
            else -> HttpLoggingInterceptor.Level.NONE
        }
        loggingInterceptor.level = level
        okHttpBuilder.addInterceptor(loggingInterceptor)
        okHttpBuilder.cache(cache)

        return okHttpBuilder.build()
    }

    @Provides
    internal fun providesRetrofit(
        @Named(BASE_URL) baseUrl: String,
        gson: Gson,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}