package com.costafot.stickers.toaster

import android.content.Context
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
