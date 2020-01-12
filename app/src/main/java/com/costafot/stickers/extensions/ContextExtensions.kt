package com.costafot.stickers.extensions

import android.content.Context
import com.costafot.stickers.toaster.ToastMessage
import com.costafot.stickers.toaster.Toaster

fun Context.toast(toastMessage: ToastMessage) {
    Toaster.show(this, toastMessage)
}
