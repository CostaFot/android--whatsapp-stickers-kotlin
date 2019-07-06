package com.feelsokman.androidtemplate.di.module

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.feelsokman.androidtemplate.BuildConfig
import com.feelsokman.net.net.resolver.NetworkResolver
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