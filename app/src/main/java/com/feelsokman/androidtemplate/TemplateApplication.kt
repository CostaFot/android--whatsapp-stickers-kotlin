package com.feelsokman.androidtemplate

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.feelsokman.androidtemplate.di.component.AppComponent
import com.feelsokman.androidtemplate.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import es.dmoral.toasty.Toasty
import timber.log.Timber

class TemplateApplication : DaggerApplication() {

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
        initToasty()
    }

    private fun initToasty() {
        Toasty.Config.getInstance()
            .setToastTypeface(ResourcesCompat.getFont(this, R.font.finger_paint)!!)
            .setErrorColor(ContextCompat.getColor(this, R.color.error))
            .setInfoColor(ContextCompat.getColor(this, R.color.primary_dark))
            .setSuccessColor(ContextCompat.getColor(this, R.color.primary))
            .setWarningColor(ContextCompat.getColor(this, R.color.accent))
            .setTextColor(ContextCompat.getColor(this, R.color.white))
            .tintIcon(true)
            .apply()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        component = DaggerAppComponent.builder().application(this).build()

        return component
    }
}