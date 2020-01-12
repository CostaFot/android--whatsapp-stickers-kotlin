package com.costafot.stickers.toaster

import android.app.Application
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.costafot.stickers.R
import es.dmoral.toasty.Toasty

object Toaster {

    fun show(context: Context, toastMessage: ToastMessage) {
        toast(context, toastMessage)
    }
}

private fun toast(context: Context, toastMessage: ToastMessage) {
    when (toastMessage) {
        is ToastMessage.Error -> {
            errorToast(context, toastMessage)
        }
        is ToastMessage.Success -> {
            successToast(context, toastMessage)
        }
        is ToastMessage.Info -> {
            infoToast(context, toastMessage)
        }
    }
}

private fun errorToast(context: Context, toastMessage: ToastMessage.Error) =
    when (toastMessage.hasResource()) {
        true -> Toasty.error(context, context.getString(toastMessage.resourceId)).show()
        else -> Toasty.error(context, toastMessage.message.toString()).show()
    }

private fun successToast(context: Context, toastMessage: ToastMessage) {
    when (toastMessage.hasResource()) {
        true -> Toasty.success(context, context.getString(toastMessage.resourceId)).show()
        else -> Toasty.success(context, toastMessage.message.toString()).show()
    }
}

private fun infoToast(context: Context, toastMessage: ToastMessage) {
    when (toastMessage.hasResource()) {
        true -> Toasty.info(context, context.getString(toastMessage.resourceId)).show()
        else -> Toasty.info(context, toastMessage.message.toString()).show()
    }
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
