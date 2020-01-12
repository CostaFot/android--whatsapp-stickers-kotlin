package com.costafot.stickers

import com.costafot.stickers.di.component.AppComponent
import com.costafot.stickers.di.component.DaggerAppComponent
import com.costafot.stickers.extensions.enableVectorSupport
import com.costafot.stickers.extensions.initFresco
import com.costafot.stickers.extensions.initLogger
import com.costafot.stickers.extensions.initToaster
import com.costafot.stickers.extensions.logDebug
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class StickerApplication : DaggerApplication() {

    companion object {
        lateinit var component: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        initLogger()
        enableVectorSupport()
        initToaster()
        initFresco()
        logDebug { "Application created!" }
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        component = DaggerAppComponent.builder().application(this).build()

        return component
    }
}
