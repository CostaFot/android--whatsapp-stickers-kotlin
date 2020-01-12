package com.costafot.stickers

import androidx.appcompat.app.AppCompatDelegate
import com.costafot.stickers.di.component.AppComponent
import com.costafot.stickers.di.component.DaggerAppComponent
import com.costafot.stickers.toaster.initToaster
import com.facebook.drawee.backends.pipeline.Fresco
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber

class StickerApplication : DaggerApplication() {

    companion object {
        lateinit var component: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Timber.d("Timber initialised")
        } else {
            Timber.e("You should not be seeing this!")
        }
        Timber.e("onCreate")
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        initToaster()
        Fresco.initialize(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        component = DaggerAppComponent.builder().application(this).build()

        return component
    }
}
