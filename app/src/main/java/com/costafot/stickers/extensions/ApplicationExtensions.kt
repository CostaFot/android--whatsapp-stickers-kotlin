package com.costafot.stickers.extensions

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.costafot.stickers.BuildConfig
import com.costafot.stickers.R
import com.facebook.drawee.backends.pipeline.Fresco
import es.dmoral.toasty.Toasty
import timber.log.Timber

fun Application.enableVectorSupport() {
    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
}

fun Application.initToaster() {
    Toasty.Config.getInstance().apply {
        setToastTypeface(ResourcesCompat.getFont(this@initToaster, R.font.finger_paint)!!)
        setErrorColor(ContextCompat.getColor(this@initToaster, R.color.error))
        setInfoColor(ContextCompat.getColor(this@initToaster, R.color.primary_dark))
        setSuccessColor(ContextCompat.getColor(this@initToaster, R.color.primary))
        setWarningColor(ContextCompat.getColor(this@initToaster, R.color.accent))
        setTextColor(ContextCompat.getColor(this@initToaster, R.color.white))
        tintIcon(true)
        apply()
    }
}

fun Application.initLogger() {
    if (BuildConfig.DEBUG) {
        Timber.plant(Timber.DebugTree())
        logInfo { "Timber is initialised" }
    } else {
        logError { "You should not be seeing this!" }
    }
}

fun Application.initFresco() {
    Fresco.initialize(this)
}
